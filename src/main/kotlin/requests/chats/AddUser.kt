package requests.chats


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddUser(
    @SerialName("set")
    val `set`: Set
) {
    @Serializable
    data class Set(
        @SerialName("sub")
        val sub: Sub,
        @SerialName("topic")
        val topic: String
    ) {
        @Serializable
        data class Sub(
            @SerialName("user")
            val user: String
        )
    }
}