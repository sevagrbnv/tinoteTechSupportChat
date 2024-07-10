package responces.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("data")
    val `data`: Data
) {
    @Serializable
    data class Data(
        @SerialName("content")
        val content: String,
        @SerialName("from")
        val from: String,
        @SerialName("seq")
        val seq: Int,
        @SerialName("topic")
        val topic: String,
        @SerialName("ts")
        val ts: String
    )
}