import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import requests.chats.GetAvailableChats
import requests.messages.GetPagingData
import requests.messages.SubMessages
import responces.ChatListSubResponseCtrl
import responces.Ctrl
import responces.DefaultCtrl
import responces.messages.Data
import responces.messages.MessageMeta
import responces.messages.MessagePres
import javax.naming.LimitExceededException

class MessageManager(
    private val session: DefaultClientWebSocketSession,
    private val json: Json,
    private val scope: CoroutineScope,
) {

    var shouldBeFinished = atomic(false)

    suspend fun DefaultClientWebSocketSession.subscribeTopic(topicId: String) {

        val data = SubMessages.Sub.Get.Data()
        val get = SubMessages.Sub.Get(data)
        val sub = SubMessages.Sub(get, topicId)
        val subMessages = SubMessages(sub)

        send(json.encodeToString(subMessages))

        val resultResponse = incoming.receive() as Frame.Text
//        println(resultResponse.readText())

        val responseCtrl = json.decodeFromString<DefaultCtrl>(resultResponse.readText())

        if (responseCtrl.ctrl.code == HttpStatusCode.OK.value) {
            repeat(4) {
                incoming.receive() as Frame.Text
            }

            while (!shouldBeFinished.value) {
                println(shouldBeFinished)

                if (!incoming.isEmpty) {
                    val response = (incoming.receive() as Frame.Text).readText()
                    val jsonElement = json.parseToJsonElement(response)
                    val jsonObject = jsonElement.jsonObject

                    when {
                        "ctrl" in jsonObject -> json.decodeFromString<DefaultCtrl>(response)
                        "meta" in jsonObject -> json.decodeFromString<MessageMeta>(response)
                        "pres" in jsonObject -> json.decodeFromString<MessagePres>(response)
                        "data" in jsonObject -> {
                            val message = json.decodeFromString<Data>(response)
//
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

        val resultResponse = incoming.receive() as Frame.Text
//        println(resultResponse.readText())

        val responseCtrl = json.decodeFromString<DefaultCtrl>(resultResponse.readText())

        if (responseCtrl.ctrl.code == HttpStatusCode.OK.value) {
            repeat(4) {
                incoming.receive() as Frame.Text
            }

            while (true) {

                if (incoming.isEmpty) {
                } else {
                    val response = (incoming.receive() as Frame.Text).readText()
                    val jsonElement = json.parseToJsonElement(response)
                    val jsonObject = jsonElement.jsonObject
//                    println(jsonObject)

                    when {
                        "ctrl" in jsonObject -> json.decodeFromString<DefaultCtrl>(response)
                        "meta" in jsonObject -> json.decodeFromString<MessageMeta>(response)
                        "pres" in jsonObject -> json.decodeFromString<MessagePres>(response)
                        "data" in jsonObject -> {
                            val message = json.decodeFromString<Data>(response)
//                            println(message)
                        }
                        else -> throw RuntimeException("oaoaoaaoaoa")
                    }
                }
            }

        }

    }
}