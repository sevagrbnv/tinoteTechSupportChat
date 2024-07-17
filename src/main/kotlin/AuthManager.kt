import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import requests.auth.Acc
import requests.auth.Hi
import requests.auth.Login

class AuthManager(
    private val session: DefaultClientWebSocketSession,
    private val json: Json,
) {

    private var token: Pair<String, String>? = null

    private var authState = AUTH_STATE.HI

    private enum class AUTH_STATE {
        HI,
        REG,
        LOGIN,
        READY,
    }

    suspend fun manageAuth(action: TinodeAction.Auth): Pair<String, String> {
        while (authState != AUTH_STATE.READY) {
            try {
                when (authState) {
                    AUTH_STATE.HI -> session.hi()
                    AUTH_STATE.LOGIN -> session.login(action.login, action.password)
                    AUTH_STATE.REG -> session.reg(action.name, action.login, action.password)
                    AUTH_STATE.READY -> {}
                }
            } catch (e: Exception) {
                throw RuntimeException(e.message)
            }
        }
        return token ?: throw IllegalStateException("No token")
    }


    private suspend fun DefaultClientWebSocketSession.hi() {
        send(json.encodeToString(Hi()))

        val response = incoming.receive() as Frame.Text
        println(response.readText())
        val responseCtrl = json.decodeFromString<HiResponseCtrl>(response.readText())

        if (responseCtrl.ctrl.code == HttpStatusCode.Created.value) {
            authState = AUTH_STATE.LOGIN
        } else throw RuntimeException("Error in Hi() $responseCtrl")
    }

    private suspend fun DefaultClientWebSocketSession.login(
        login: String,
        password: String,
    ) {
        send(json.encodeToString(Login(base64 = "$login:$password".encodeBase64())))

        val response = incoming.receive() as Frame.Text
        val responseCtrl = json.decodeFromString<LoginResponseCtrl>(response.readText())
        authState = when (responseCtrl.ctrl.code) {
            HttpStatusCode.OK.value -> {
                token = responseCtrl.ctrl.params.token to responseCtrl.ctrl.params.expires
                AUTH_STATE.READY
            }
            HttpStatusCode.BadRequest.value -> AUTH_STATE.LOGIN // 400 неверный пароль
            HttpStatusCode.Unauthorized.value -> AUTH_STATE.REG // 401 нет аккаунта
            else -> throw RuntimeException("Error in login()")
        }
    }

    private suspend fun DefaultClientWebSocketSession.reg(
        name: String,
        login: String,
        password: String,
    ) {
        val public = Acc.Public(name)
        val desc = Acc.Desc(public)
        val acc =
            Acc(
                Acc.AccBody(
                    desc = desc,
                    cred = listOf(Acc.Cred()),
                    secret = "$login:$password".encodeBase64()
                )
            )
        send(json.encodeToString(acc))

        val response = incoming.receive() as Frame.Text
        val responseCtrl = json.decodeFromString<LoginResponseCtrl>(response.readText())
        when (responseCtrl.ctrl.code) {
            HttpStatusCode.OK.value -> authState = AUTH_STATE.LOGIN
            else -> throw RuntimeException("Error in reg()")
        }
    }
}