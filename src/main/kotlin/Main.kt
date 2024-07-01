import kotlinx.coroutines.*

// пример использования
fun main() {

    val client = TinodeClient("127.0.0.1", 6060, "AQAAAAABAAAoeOI7tA3HsYvdzDhYhZJy")
    val details = "+7-XXX-XXX-XX-XX, 0123456789"

    runBlocking {
        client.dispatch(TinodeAction.Auth("admin", details, "admin","qwerty123"))
        client.dispatch(TinodeAction.CreateChats)
        client.start()

        client.close()
    }
}
