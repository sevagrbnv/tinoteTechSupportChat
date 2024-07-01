import domain.Topic
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import requests.CreateChat
import requests.GetAvailableChats
import responces.ChatListSubResponseCtrl
import utils.Base64
import java.net.http.HttpResponse

class ChatManager(
    private val session: DefaultClientWebSocketSession,
    private val json: Json,
) {

    private val chatList = listOf<ChatConfig>(ChatConfig("ТехПоддержка", listOf()))

    suspend fun DefaultClientWebSocketSession.setupChats() {
        send(json.encodeToString(GetAvailableChats()))

        val resultResponse = incoming.receive() as Frame.Text
        println(resultResponse.readText())

        val responseCtrl = json.decodeFromString<ChatListSubResponseCtrl>(resultResponse.readText())
        if (responseCtrl.ctrl.code == HttpStatusCode.OK.value) {
            val subscribeResponse = incoming.receive() as Frame.Text
            println(subscribeResponse.readText())

            val metaChatList = json.decodeFromString<ChatMeta>(subscribeResponse.readText())
            val topicList = metaChatList.meta.sub.map { it.toTopic() }

            val metaChatNameList = metaChatList.meta.sub.map { it.public.fn }
            println(metaChatNameList)

            chatList.forEach {
                if (!metaChatNameList.contains(it.name)) {
                    createChat(session, it.name)
                }
            }
        }
    }

    private suspend fun createChat(
        session: DefaultClientWebSocketSession,
        name: String,
    ) {
        val public = CreateChat.Sub.Set.Desc.Public(name, "")
        val desc = CreateChat.Sub.Set.Desc(public)
        val set = CreateChat.Sub.Set(desc)
        val sub = CreateChat.Sub(set = set, topic = "new${Base64.encodeToBase64(name)}")
        session.send(json.encodeToString(CreateChat(sub = sub)))

        val resultResponse = session.incoming.receive() as Frame.Text
        println(resultResponse.readText())
    }

    data class ChatConfig(
        val name: String,
        val users: List<String>,
    )
}

fun Sub.toTopic() = Topic (
    id = topic,
    name = public.fn
)