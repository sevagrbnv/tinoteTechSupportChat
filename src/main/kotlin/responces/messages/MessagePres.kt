package responces.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagePres(
    @SerialName("pres")
    val pres: Pres
) {
    @Serializable
    data class Pres(
        @SerialName("src")
        val src: String,
        @SerialName("topic")
        val topic: String,
//        @SerialName("what")
//        val what: String
    )
}