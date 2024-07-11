import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// пример использования
suspend fun main() {

    val client = TinodeClient("127.0.0.1", 6060, "AQAAAAABAAAoeOI7tA3HsYvdzDhYhZJy")
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        val details = "+7-XXX-XXX-XX-XX, 0123456789"

        scope.launch {
            client.start()
        }

        scope.launch {
            client.messagesFlow.collect {
                println(it)
            }
        }

        client.dispatch(TinodeAction.Auth("admin", details, "admin", "qwerty123"))
        println("TinodeAction.Auth")

        client.dispatch(TinodeAction.CreateChats)
        println("TinodeAction.CreateChats")

        while (client.chats.size == 0) {
            println(client.chats.size)
        }
        delay(1000)

        println("TinodeAction.GetMessages")
        client.dispatch(TinodeAction.GetMessages(client.chats[0].id))

        //delay(1000)

        //client.dispatch(TinodeAction.CloseConnection)

        //delay(1000)

        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!${client.chats}")

        delay(1000)
        client.dispatch(TinodeAction.SendMessage(client.chats[0].id, "New test message!!!"))
        client.dispatch(TinodeAction.LeaveChat(client.chats[0].id))
        delay(10_000)
        client.dispatch(TinodeAction.CloseConnection)



        delay(1_000)
    }

    job.join()
}
