import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect

// пример использования
suspend fun main() {

    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        val client = TinodeClient("127.0.0.1", 6060, "AQAAAAABAAAoeOI7tA3HsYvdzDhYhZJy")
        val details = "+7-XXX-XXX-XX-XX, 0123456789"

        scope.launch {
            client.start()
        }

        client.dispatch(TinodeAction.Auth("admin", details, "admin", "qwerty123"))
        println("TinodeAction.Auth")

        client.dispatch(TinodeAction.CreateChats)
        println("TinodeAction.CreateChats")

        while (client.chats.size == 0 ){
            println(client.chats.size)
        }

        println("TinodeAction.GetMessages")
        client.dispatch(TinodeAction.GetMessages(client.chats[0].id))
        println("TinodeAction.GetMessages")

        //client.dispatch(TinodeAction.CloseConnection)

        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!${client.token}")
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!${client.chats}")

        delay(10000)

        //scope.launch {
            client.messagesFlow.collect() {
                println(it)
            }
        //}

    }

    job.join()

}
