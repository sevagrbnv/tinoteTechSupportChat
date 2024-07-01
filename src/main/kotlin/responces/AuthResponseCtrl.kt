@kotlinx.serialization.Serializable
data class HiResponseCtrl(
    val ctrl: HiResponse
)

@kotlinx.serialization.Serializable
data class LoginResponseCtrl(
    val ctrl: LoginResponse
)

@kotlinx.serialization.Serializable
data class AccResponseCtrl(
    val ctrl: AccResponse
)
