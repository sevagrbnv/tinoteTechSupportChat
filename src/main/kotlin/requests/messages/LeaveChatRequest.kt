package requests.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaveChatRequest(
    @SerialName("leave")
    val leave: Leave
) {
    @Serializable
    data class Leave(
        @SerialName("topic")
        val topic: String
    )
}