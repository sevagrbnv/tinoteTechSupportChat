import domain.Message
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import requests.messages.GetPagingData
import requests.messages.SendMessageRequest
import requests.messages.SubToMessages
import responces.DefaultCtrl
import responces.messages.Data
import responces.messages.MessageMeta
import responces.messages.MessagePres

class MessageManager(
    private val session: DefaultClientWebSocketSession,
    private val json: Json,
    private val scope: CoroutineScope,
) {

    var shouldBeFinished = atomic(false)

    suspend fun DefaultClientWebSocketSession.subscribeTopic(topicId: String, messageFlow: MutableStateFlow<MutableList<Message>>) {

        val data = SubToMessages.Sub.Get.Data()
        val get = SubToMessages.Sub.Get(data)
        val sub = SubToMessages.Sub(get, topicId)
        val subMessages = SubToMessages(sub)

        send(json.encodeToString(subMessages))

        val resultResponse = incoming.receive() as Frame.Text
//        println(resultResponse.readText())

        val responseCtrl = json.decodeFromString<DefaultCtrl>(resultResponse.readText())

        if (responseCtrl.ctrl.code == HttpStatusCode.OK.value) {
            repeat(4) {
                incoming.receive() as Frame.Text
            }

            var currSeq: Int? = null
            val messageList = mutableListOf<Message>()

            while (!shouldBeFinished.value) {
                //println(shouldBeFinished)


                if (!incoming.isEmpty) {
                    val response = (incoming.receive() as Frame.Text).readText()
                    val jsonElement = json.parseToJsonElement(response)
                    val jsonObject = jsonElement.jsonObject

                    when {
                        "ctrl" in jsonObject -> json.decodeFromString<DefaultCtrl>(response)
                        "meta" in jsonObject -> json.decodeFromString<MessageMeta>(response)
                        "pres" in jsonObject -> json.decodeFromString<MessagePres>(response)
                        "data" in jsonObject -> {
                            val message = json.decodeFromString<Data>(response).toMessage()
                            println(message)

                            when {
                                currSeq == null || message.seq > currSeq -> {
                                    currSeq = message.seq
                                    messageList.add(message)
                                }
                                message.seq < currSeq -> {
                                    messageList.add(0, message)
                                }
                            }
                            messageFlow.emit(messageList)
                        }
                        else -> throw RuntimeException("oaoaoaaoaoa")
                    }
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

    suspend fun DefaultClientWebSocketSession.sendMessage(topicId: String, content: String) {
        val pub = SendMessageRequest.Pub(topic = topicId, content = content)
        send(json.encodeToString(SendMessageRequest(pub)))
    }
}