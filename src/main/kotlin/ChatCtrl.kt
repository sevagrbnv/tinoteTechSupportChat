import com.google.gson.annotations.SerializedName


@kotlinx.serialization.Serializable
data class ChatListSubResponse(
    @SerializedName("code") override val code: Int,
) : Ctrl

interface MetaInter

@kotlinx.serialization.Serializable
data class Meta(
    @SerializedName("sub")
    val sub: List<Sub>
) : MetaInter

@kotlinx.serialization.Serializable
data class ChatMeta(
    val meta: Meta
)

@kotlinx.serialization.Serializable
data class Sub(
    val `public`: PublicMeta,
    val topic: String
)

@kotlinx.serialization.Serializable
data class PublicMeta(
    val fn: String,
    //val note: String,
)