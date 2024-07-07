package requests.auth

@kotlinx.serialization.Serializable
data class Login(
    val base64: String,
    val login: LoginBody = LoginBody(secret = base64)
) {
    @kotlinx.serialization.Serializable
    data class LoginBody(
        val scheme: String = "basic",
        val secret: String
    )
}
