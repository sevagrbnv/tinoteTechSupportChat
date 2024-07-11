import domain.Message
import domain.Topic
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class TinodeClient(
    private val host: String,
    private val port: Int,
    private val apiKey: String,
) {

    companion object {
        private const val PATH = "/v0/channels"
        private const val API_HEADER = "X-Tinode-APIKey"
        private const val PING_INTERVAL = 20_000L
    }

    private val inputQueue: ArrayDeque<TinodeAction> = ArrayDeque()

    private var shouldBeFinished = false

    lateinit var token: Pair<String, String>

    val messagesFlow = MutableStateFlow<MutableList<Message>>(mutableListOf())

    val chats = mutableListOf<Topic>()

    private val subScope = CoroutineScope(Dispatchers.Default)

    private val client by lazy {
        HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = PING_INTERVAL
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    private val jsonFormat = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }


    suspend fun start() {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = PATH,
            request = { header(API_HEADER, apiKey) }
        ) {
            while (!shouldBeFinished) {
                if (inputQueue.isNotEmpty()) {
                    val messageManager by lazy {
                        MessageManager(this, jsonFormat, subScope)
                    }

                    when (val action = inputQueue.firstOrNull()) {
                        is TinodeAction.Auth -> {
                            val authManager = AuthManager(this, jsonFormat)
                            token = authManager.manageAuth(action)
                        }
                        TinodeAction.CreateChats -> {
                            ChatManager(this, jsonFormat).apply {
                                chats.addAll(setupChats())
                            }
                        }
                        is TinodeAction.GetMessages -> {
                            subScope.launch {
                                messageManager.apply {
                                    subscribeTopic(action.topicId, messagesFlow)
                                }
                            }

                        }
                        is TinodeAction.GetPagingHistory -> {
                            messageManager.apply {
                                getPagingHistory(action.topicId, action.before, action.limit)
                            }
                        }
                        is TinodeAction.SendMessage -> {
                            messageManager.apply {
                                sendMessage(action.topicId, action.content)
                            }
                        }
                        TinodeAction.CloseConnection -> {
                            shouldBeFinished = true
                            client.close()
                        }
                        else -> {}
                    }
                    inputQueue.removeFirst()
                }
            }
        }
    }

    fun dispatch(action: TinodeAction) {
        inputQueue.addLast(action)
    }
}