import kotlinx.serialization.SerialName
import responces.Ctrl

@kotlinx.serialization.Serializable
data class HiResponseCtrl(
    val ctrl: HiResponse
) {
    @kotlinx.serialization.Serializable
    data class HiResponse(
        @SerialName("code") override val code: Int,
    ) : Ctrl
}

@kotlinx.serialization.Serializable
data class LoginResponseCtrl(
    val ctrl: LoginResponse
) {
    @kotlinx.serialization.Serializable
    data class LoginResponse(
        @SerialName("code") override val code: Int,
        @SerialName("params") val params: Params = Params("",""),
    ) : Ctrl {
        @kotlinx.serialization.Serializable
        data class Params(
            @SerialName("token") val token: String,
            @SerialName("expires") val expires: String,
        )
    }
}

@kotlinx.serialization.Serializable
data class AccResponseCtrl(
    val ctrl: AccResponse
) {
    @kotlinx.serialization.Serializable
    data class AccResponse(
        @SerialName("code") override val code: Int,
    ) : Ctrl
}
