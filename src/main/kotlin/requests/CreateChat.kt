package requests


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateChat(
    @SerialName("sub")
    val sub: Sub
) {
    @Serializable
    data class Sub(
        //@SerialName("get")
        //val `get`: Get,
        @SerialName("set")
        val `set`: Set,
        @SerialName("topic")
        val topic: String
    ) {
        @Serializable
        data class Get(
            @SerialName("data")
            val `data`: Data,
            @SerialName("what")
            val what: String
        ) {
            @Serializable
            data class Data(
                @SerialName("limit")
                val limit: Int
            )
        }

        @Serializable
        data class Set(
            @SerialName("desc")
            val desc: Desc
        ) {
            @Serializable
            data class Desc(
                @SerialName("public")
                val `public`: Public
            ) {
                @Serializable
                data class Public(
                    @SerialName("fn")
                    val fn: String,
                    @SerialName("note")
                    val note: String = ""
                )
            }
        }
    }
}