import com.google.gson.annotations.SerializedName
import responces.Ctrl

@kotlinx.serialization.Serializable
data class HiResponseCtrl(
    val ctrl: HiResponse
) {
    @kotlinx.serialization.Serializable
    data class HiResponse(
        @SerializedName("code") override val code: Int,
    ) : Ctrl
}

@kotlinx.serialization.Serializable
data class LoginResponseCtrl(
    val ctrl: LoginResponse
) {
    @kotlinx.serialization.Serializable
    data class LoginResponse(
        @SerializedName("code") override val code: Int,
    ) : Ctrl
}

@kotlinx.serialization.Serializable
data class AccResponseCtrl(
    val ctrl: AccResponse
) {
    @kotlinx.serialization.Serializable
    data class AccResponse(
        @SerializedName("code") override val code: Int,
    ) : Ctrl
}
