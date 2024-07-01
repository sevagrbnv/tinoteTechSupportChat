import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    private val queue: ArrayDeque<TinodeAction> = ArrayDeque()

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
            while (true) {
                if (queue.isNotEmpty()) {
                    when (val action = queue.firstOrNull()) {
                        is TinodeAction.Auth -> {
                            AuthManager(this, jsonFormat).apply {
                                manageAuth(action)
                            }
                        }
                        TinodeAction.CreateChats -> {
                            ChatManager(this, jsonFormat).apply {
                                setupChats()
                            }
                        }
                        TinodeAction.GetMessages -> {

                        }
                        TinodeAction.SendMessage -> {

                        }
                        else -> {}
                    }
                    queue.removeFirst()
                }
            }
        }
    }

    fun close() {
        client.close()
    }

    fun dispatch(action: TinodeAction) {
        queue.addLast(action)
    }
}