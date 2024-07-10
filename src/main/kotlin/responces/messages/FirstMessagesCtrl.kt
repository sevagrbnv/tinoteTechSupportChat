package responces.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import responces.Ctrl

@Serializable
data class FirstMessagesCtrl(
    @SerialName("ctrl")
    val ctrl: FirstCtrl
) {
    @Serializable
    data class FirstCtrl(
        @SerialName("code")
        override val code: Int
    ) : Ctrl
}