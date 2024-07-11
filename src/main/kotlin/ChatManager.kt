import domain.Topic
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import requests.chats.AddUser
import requests.chats.CreateChat
import requests.chats.GetAvailableChats
import responces.ChatCreateResult
import responces.ChatListSubResponseCtrl
import responces.ChatMeta
import responces.EmptyChatList
import utils.Base64

class ChatManager
    (
    private val session: DefaultClientWebSocketSession,
    private val json: Json,
) {

    companion object {
        const val TECH_SUPPORT_ACCOUNT = "usrIBd2l9hwhh0"
    }

    private val chatList = listOf(
        ChatConfig("ТехПоддержка", listOf(TECH_SUPPORT_ACCOUNT))
    )

    suspend fun DefaultClientWebSocketSession.setupChats(): List<Topic> {

        send(json.encodeToString(GetAvailableChats()))

        val resultResponse = incoming.receive() as Frame.Text

        val responseCtrl = json.decodeFromString<ChatListSubResponseCtrl>(resultResponse.readText())
        if (responseCtrl.ctrl.code == HttpStatusCode.OK.value) {
            val subscribeResponse = incoming.receive() as Frame.Text

            val metaChatList = try {
                json.decodeFromString<ChatMeta>(subscribeResponse.readText())
            } catch (e: Exception) {
                json.decodeFromString<EmptyChatList>(subscribeResponse.readText())
            }

            val topicList = if (metaChatList is ChatMeta)
                metaChatList.meta.sub.map { it.toTopic() }.toMutableList()
            else mutableListOf()

            val metaChatNameList = if (metaChatList is ChatMeta)
                metaChatList.meta.sub.map { it.public.fn }
            else mutableListOf()

            chatList.forEach {
                if (!metaChatNameList.contains(it.name)) {
                    topicList.add(Topic(it.name, createChat(session, it)))
                }
            }

            println(topicList)
            return topicList
        } else throw RuntimeException("Chat list not available")

    }

    private suspend fun createChat(
        session: DefaultClientWebSocketSession,
        config: ChatConfig,
    ) : String {
        val public = CreateChat.Sub.Set.Desc.Public(config.name, "")
        val desc = CreateChat.Sub.Set.Desc(public)
        val set = CreateChat.Sub.Set(desc)
        val sub = CreateChat.Sub(set = set, topic = "new${Base64.encodeToBase64(config.name)}")
        session.send(json.encodeToString(CreateChat(sub = sub)))

        val resultResponse = session.incoming.receive() as Frame.Text
        val result = json.decodeFromString<ChatCreateResult>(resultResponse.readText())

        if (result.ctrl.code == HttpStatusCode.OK.value) {
            config.users.forEach{
                val sub = AddUser.Set.Sub(it)
                val set = AddUser.Set(sub, topic = result.ctrl.topic)
                val addUser = AddUser(set)
                session.send(json.encodeToString(addUser))
            }

            val resultResponse = session.incoming.receive() as Frame.Text
        } else RuntimeException("Error of creating channel")

        return result.ctrl.topic
    }

    data class ChatConfig(
        val name: String,
        val users: List<String>,
    )
}
