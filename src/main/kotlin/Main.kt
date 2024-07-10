import kotlinx.coroutines.*

// пример использования
suspend fun main() {

    val client = TinodeClient("127.0.0.1", 6060, "AQAAAAABAAAoeOI7tA3HsYvdzDhYhZJy")
    val details = "+7-XXX-XXX-XX-XX, 0123456789"

    runBlocking(Dispatchers.Default) {
        client.dispatch(TinodeAction.Auth("admin", details, "admin", "qwerty123"))
        println("TinodeAction.Auth")
        client.dispatch(TinodeAction.CreateChats)
        println("TinodeAction.CreateChats")
        client.dispatch(TinodeAction.GetMessages("grpyGOkpuFFLdg"))

        println("TinodeAction.GetMessages")
        client.dispatch(TinodeAction.CloseConnection)
        println("TinodeAction.GetMessages")
        client.start()
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!${client.token}")
    }


}
