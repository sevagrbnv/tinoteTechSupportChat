import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import requests.Acc
import requests.Hi
import requests.Login

class TinodeClient(
    private val host: String,
    private val port: Int,
    private val apiKey: String,
) {

    companion object {
        private const val PATH = "/v0/channels"
        private const val API_HEADER = "X-Tinode-APIKey"
        private const val PING_INTERVAL = 20_000L

        private const val TEMP_SECRET = "bG9naW5sb2dpbjpwYXNzd29yZDEyMw=="
    }

    private var userState = USER_STATE.HI

    private val client by lazy {
        HttpClient(CIO) {
            install(WebSockets) {
                pingInterval = PING_INTERVAL
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }

    private val jsonFormat = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        classDiscriminator = "Ctrl"
    }

    suspend fun auth(name: String, login: String, pass: String) {
        client.webSocket(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = PATH,
            request = { header(API_HEADER, apiKey) }
        ) {
            while (userState != USER_STATE.READY) {
                when (userState) {
                    USER_STATE.HI -> {
                        send(jsonFormat.encodeToString(Hi()))

                        val response = incoming.receive() as Frame.Text
                        println(response.readText())
                        val responseCtrl = jsonFormat.decodeFromString<HiResponseCtrl>(response.readText())
                        if (responseCtrl.ctrl.code == HttpStatusCode.Created.value) userState = USER_STATE.LOGIN
                        else throw RuntimeException("Error")
                    }

                    USER_STATE.LOGIN -> {
                        send(jsonFormat.encodeToString(Login(base64 = TEMP_SECRET)))

                        val response = incoming.receive() as Frame.Text
                        println(response.readText())
                        val responseCtrl = jsonFormat.decodeFromString<LoginResponseCtrl>(response.readText())
                        userState = when (responseCtrl.ctrl.code) {
                            HttpStatusCode.OK.value -> USER_STATE.READY
                            HttpStatusCode.BadRequest.value -> USER_STATE.REG // 400 неверный пароль
                            HttpStatusCode.Unauthorized.value -> USER_STATE.REG // 401 нет юзера
                            else -> throw RuntimeException("Error")
                        }
                    }

                    USER_STATE.REG -> {
                        val public = Acc.Public(name)
                        val desc = Acc.Desc(public)
                        val acc = Acc(Acc.AccBody(desc = desc, cred = listOf(Acc.Cred()), secret = TEMP_SECRET))
                        println(acc)
                        send(jsonFormat.encodeToString(acc))

                        val response = incoming.receive() as Frame.Text
                        println(response.readText())
                        val responseCtrl = jsonFormat.decodeFromString<LoginResponseCtrl>(response.readText())
                        userState = when (responseCtrl.ctrl.code) {
                            HttpStatusCode.OK.value -> USER_STATE.LOGIN
                            else -> {throw RuntimeException()}
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun close() {
        client.close()
    }

    private enum class USER_STATE {
        HI,
        REG,
        LOGIN,
        READY
    }
}