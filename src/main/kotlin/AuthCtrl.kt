import com.google.gson.annotations.SerializedName

interface Ctrl {
    val code: Int
}

@kotlinx.serialization.Serializable
data class HiResponse(
    @SerializedName("code") override val code: Int,
) : Ctrl

@kotlinx.serialization.Serializable
data class LoginResponse(
    @SerializedName("code") override val code: Int,
) : Ctrl

@kotlinx.serialization.Serializable
data class AccResponse(
    @SerializedName("code") override val code: Int,
) : Ctrl

