package responces.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewData(
    @SerialName("data")
    val `data`: Data
) {
    @Serializable
    data class Data(
        @SerialName("content")
        val content: Content,
        @SerialName("from")
        val from: String,
        @SerialName("head")
        val head: Head,
        @SerialName("seq")
        val seq: Int,
        @SerialName("topic")
        val topic: String,
        @SerialName("ts")
        val ts: String
    ) {
        @Serializable
        data class Content(
            @SerialName("ent")
            val ent: List<Ent>,
            @SerialName("fmt")
            val fmt: List<Fmt>,
            @SerialName("txt")
            val txt: String
        ) {
            @Serializable
            data class Ent(
                @SerialName("data")
                val `data`: Data,
                @SerialName("tp")
                val tp: String
            ) {
                @Serializable
                data class Data(
                    @SerialName("mime")
                    val mime: String,
                    @SerialName("type")
                    val type: String,
                    @SerialName("val")
                    val valX: String
                )
            }

            @Serializable
            data class Fmt(
                @SerialName("at")
                val at: Int,
                @SerialName("key")
                val key: Int,
                @SerialName("len")
                val len: Int,
                @SerialName("tp")
                val tp: String?
            )
        }

        @Serializable
        data class Head(
            @SerialName("mime")
            val mime: String
        )
    }
}