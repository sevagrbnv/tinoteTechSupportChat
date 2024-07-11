package requests.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    @SerialName("pub")
    val pub: Pub
) {
    @Serializable
    data class Pub(
        @SerialName("content")
        val content: String,
        @SerialName("noecho")
        val noecho: Boolean = true,
        @SerialName("topic")
        val topic: String
    )
}