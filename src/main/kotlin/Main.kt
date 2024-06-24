import kotlinx.coroutines.*

// пример использования
fun main() {

    val client = TinodeClient("127.0.0.1", 6060, "AQAAAAABAAAoeOI7tA3HsYvdzDhYhZJy")

    runBlocking {
        client.auth("adm", "admin==","qwerty123")

        client.close()
    }
}
