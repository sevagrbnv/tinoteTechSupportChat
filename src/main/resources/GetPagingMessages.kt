
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPagingMessages(
    @SerialName("get")
    val `get`: Get
) {
    @Serializable
    data class Get(
        @SerialName("data")
        val `data`: Data,
        @SerialName("topic")
        val topic: String,
        @SerialName("what")
        val what: String = "data"
    ) {
        @Serializable
        data class Data(
            @SerialName("before")
            val before: Int,
            @SerialName("limit")
            val limit: Int,
            @SerialName("since")
            val since: Int
        )
    }
}