package requests.chats

@kotlinx.serialization.Serializable
data class GetAvailableChats(
    val sub: Sub = Sub()
) {

    @kotlinx.serialization.Serializable
    data class Sub(
        val topic: String = "me",
        val get: What = What()
    )

    @kotlinx.serialization.Serializable
    data class What(
        val what: String = "sub"
    )
}