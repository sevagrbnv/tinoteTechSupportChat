import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
            client.messagesFlow.collect {
                println(it)
            }
        }

        launch {
            client.dispatch(TinodeAction.Auth("admin", details, "admin", "qwerty123"))

            client.dispatch(TinodeAction.CreateChats)

            //client.dispatch(TinodeAction.GetMessages(client.chatFlow.value[0].id))
//            client.dispatch(TinodeAction.GetMessages("grpyGOkpuFFLdg"))

            delay(1000)
        }

        //client.dispatch(TinodeAction.LeaveChat(client.chatFlow.value[0].id))
        //client.dispatch(TinodeAction.CloseConnection)
    }

    job.join()
}
