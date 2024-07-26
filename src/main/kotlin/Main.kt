import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


// пример использования
suspend fun main() {

    val client = TinodeClient("127.0.0.1", 6060, "AQAAAAABAAAoeOI7tA3HsYvdzDhYhZJy")
    val scope = CoroutineScope(Dispatchers.Default)
    val details = "+7-XXX-XXX-XX-XX, 0123456789"

    val job = scope.launch {

        launch {
            client.start()
        }

        launch {
            client.chatFlow.collect {
                if (it.isNotEmpty()) {
                    client.dispatch(TinodeAction.GetMessages(client.chatFlow.value[0].id))
                }
            }
        }

        launch {
            client.messagesFlow.collectLatest {
                println(it)
            }
        }

        launch {
            client.dispatch(TinodeAction.Auth("Adolf Hitler", details, "user56", "qwerty123"))

            client.dispatch(TinodeAction.CreateChats)

            delay(4000)

            client.dispatch(
                TinodeAction.SendMessage(
                    topicId = client.chatFlow.value[0].id,
                    "Это файл отправленный через наш клиент",
                    listOf(
                        "D:\\aniver\\vkusvill\\abc.pdf",
                    )
                )
            )
        }

        //client.dispatch(TinodeAction.LeaveChat(client.chatFlow.value[0].id))
        //client.dispatch(TinodeAction.CloseConnection)
    }

    job.join()
}
