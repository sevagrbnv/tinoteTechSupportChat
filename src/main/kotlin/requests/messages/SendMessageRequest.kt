package requests.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    @SerialName("pub")
    val pub: Pub,
) {
    @Serializable
    data class Pub(
        @SerialName("content")
        val content: Content,
        @SerialName("noecho")
        val noecho: Boolean = true,
        @SerialName("topic")
        val topic: String,
        @SerialName("head")
        val head: Head = Head()
    ) {

        @Serializable
        data class Head(
            @SerialName("mime")
            val mime: String = "text/x-drafty"
        )
        @Serializable
        data class Content(
            @SerialName("txt")
            val txt: String,
            @SerialName("ent")
            val ent: List<Ent> = emptyList(),
            @SerialName("fmt")
            val fmt: List<Fmt> = emptyList(),
        ) {
            @Serializable
            data class Ent(
                val tp: String,
                val data: DataEnt,
            ) {

                @Serializable
                sealed interface DataEnt {
                    @Serializable
                    data class IM(
                        val mime: String = "image/png",
                        val `val`: String,
                        //val ref: String
                        //val width: Int,
                        //val height: Int,
                    ) : DataEnt

                    @Serializable
                    data class BN(
                        val name: String,
                        val act: String,
                        val `val`: String,
                    ) : DataEnt
                }

            }
            @Serializable
            data class Fmt(
                val at: Int = 0,
                val len: Int = 0,
                val key: Int = 0,
                val tp:String? = null
            )
        }
    }
}