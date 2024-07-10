package responces

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Ctrl { val code: Int }

@Serializable
data class DefaultCtrl(
    @SerialName("ctrl")
    val ctrl: DefaultCtrlImpl
) {
    @Serializable
    data class DefaultCtrlImpl(
        @SerialName("code")
        override val code: Int
    ) : Ctrl
}