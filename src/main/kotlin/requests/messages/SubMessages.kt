package requests.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubMessages(
    @SerialName("sub")
    val sub: Sub
) {
    @Serializable
    data class Sub(
        @SerialName("get")
        val `get`: Get,
//        @SerialName("id")
//        val id: String,
        @SerialName("topic")
        val topic: String
    ) {
        @Serializable
        data class Get(
            @SerialName("data")
            val `data`: Data,
//            @SerialName("desc")
//            val desc: Desc,
            @SerialName("what")
            val what: String = "desc sub data del tags"
        ) {
            @Serializable
            data class Data(
                @SerialName("limit")
                val limit: Int = 24
            )

//            @Serializable
//            data class Desc(
//                @SerialName("ims")
//                val ims: String
//            )
        }
    }
}