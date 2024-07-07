package responces

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatListSubResponseCtrl(
    val ctrl: ChatListSubResponse
) {
    @Serializable
    data class ChatListSubResponse(
        @SerializedName("code") override val code: Int,
    ) : Ctrl
}


@Serializable
data class ChatMeta(
    val meta: Meta
) {
    @Serializable
    data class Meta(
        @SerializedName("sub")
        val sub: List<Sub>
    ) {
        @Serializable
        data class Sub(
            val `public`: PublicMeta,
            val topic: String
        ) {
            @Serializable
            data class PublicMeta(
                val fn: String,
                //val note: String,
            )
        }
    }
}

@Serializable
data class EmptyChatList(
    @SerialName("ctrl")
    val ctrl: Ctrl
) {
    @Serializable
    data class Ctrl(
        @SerialName("code")
        val code: Int,
        @SerialName("params")
        val params: Params,
        @SerialName("text")
        val text: String,
        @SerialName("topic")
        val topic: String,
        @SerialName("ts")
        val ts: String
    ) {
        @Serializable
        data class Params(
            @SerialName("what")
            val what: String
        )
    }
}

@Serializable
data class ChatCreateResult(
    @SerialName("ctrl")
    val ctrl: Ctrl
) {
    @Serializable
    data class Ctrl(
        @SerialName("code")
        val code: Int,
        @SerialName("params")
        val params: Params,
        @SerialName("text")
        val text: String,
        @SerialName("topic")
        val topic: String,
        @SerialName("ts")
        val ts: String
    ) {
        @Serializable
        data class Params(
            @SerialName("acs")
            val acs: Acs,
            @SerialName("tmpname")
            val tmpname: String
        ) {
            @Serializable
            data class Acs(
                @SerialName("given")
                val given: String,
                @SerialName("mode")
                val mode: String,
                @SerialName("want")
                val want: String
            )
        }
    }
}