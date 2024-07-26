import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import requests.messages.GetPagingData
import requests.messages.LeaveChatRequest
import requests.messages.SendMessageRequest
import requests.messages.SubToMessages
import responces.DefaultCtrl
import responces.messages.Data
import responces.messages.MessageMeta
import responces.messages.MessagePres
import responces.messages.NewData
import utils.getMimeType

class MessageManager(
    private val session: DefaultClientWebSocketSession,
    private val json: Json,
    private val scope: CoroutineScope,
) {

    var shouldBeFinished = false

    suspend fun DefaultClientWebSocketSession.subscribeTopic(
        topicId: String,
    ) = flow {

        val data = SubToMessages.Sub.Get.Data()
        val get = SubToMessages.Sub.Get(data)
        val sub = SubToMessages.Sub(get, topicId)
        val subMessages = SubToMessages(sub)

        send(json.encodeToString(subMessages))

        val resultResponse = incoming.receive() as Frame.Text

        val responseCtrl = json.decodeFromString<DefaultCtrl>(resultResponse.readText())

        if (responseCtrl.ctrl.code == HttpStatusCode.OK.value) {

            while (!shouldBeFinished) {

                if (!incoming.isEmpty) {
                    val response = try {
                        (incoming.receive() as Frame.Text).readText()
                    } catch (e: ClosedReceiveChannelException) {
                        break
                    }
                    val jsonElement = json.parseToJsonElement(response)
                    val jsonObject = jsonElement.jsonObject

                    when {
                        "ctrl" in jsonObject -> json.decodeFromString<DefaultCtrl>(response)
                        "meta" in jsonObject -> json.decodeFromString<MessageMeta>(response)
                        "pres" in jsonObject -> json.decodeFromString<MessagePres>(response)
                        "data" in jsonObject -> {
                            val message = json.decodeFromString<NewData>(response).toMessage()
                            //println("~~~~~~~~~~~~~~$message")
                            emit(message)
                        }
                        else -> throw RuntimeException("oaoaoaaoaoa")
                    }
                    println("~~~~~~~~~~~~~~~~~~$jsonElement")
                }
            }
        }
    }

    suspend fun DefaultClientWebSocketSession.getPagingHistory(topicId: String, before: Int, limit: Int = 24) {

        val data = GetPagingData.Get.Data(before, limit, since = 1)
        val get = GetPagingData.Get(data = data, topic = topicId)
        val getPagingMessages = GetPagingData(get)
        send(json.encodeToString(getPagingMessages))
    }

    suspend fun DefaultClientWebSocketSession.sendMessage(
        topicId: String,
        content: String,
        fileList: List<String> = emptyList(),
    ) {
        //val pub = SendMessageRequest.Pub(topic = topicId, content = content)
        //send(json.encodeToString(SendMessageRequest(pub)))
        val entList = fileList.map { filePath ->
            //val filePath = "D:\\aniver\\vkusvill\\Chat.jpg"
            val file = FileSystem.SYSTEM.source(filePath.toPath())
            val base = file.buffer().readByteArray().encodeBase64()
            val mime = getMimeType(filePath)

            val dataEnt = SendMessageRequest.Pub.Content.Ent.DataEnt.IM(
                mime = mime.first,
                `val` = base
            ) as SendMessageRequest.Pub.Content.Ent.DataEnt
            SendMessageRequest.Pub.Content.Ent(tp = mime.second, data = dataEnt)
        }

        val content = SendMessageRequest.Pub.Content(
            txt = "  $content",
            ent = entList,
            fmt = if (entList.isEmpty()) emptyList()
            else listOf(
                SendMessageRequest.Pub.Content.Fmt(len = 1),
                SendMessageRequest.Pub.Content.Fmt(at = 1, len = 1, tp = "BR"),
            )
        )
        val pub = SendMessageRequest.Pub(topic = topicId, content = content)
        val sendMessageRequest = SendMessageRequest(pub)
        val m = json.encodeToString(sendMessageRequest)
        println(m)
        send(m)
    }

    suspend fun DefaultClientWebSocketSession.leaveChat(topicId: String) {
        val leave = LeaveChatRequest.Leave(topicId)
        send(json.encodeToString(LeaveChatRequest(leave)))

    }
}