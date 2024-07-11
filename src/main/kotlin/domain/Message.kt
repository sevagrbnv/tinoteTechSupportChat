package domain

data class Message(
    val content: String,
    val from: String,
    val seq: Int,
    val topic: String,
    val ts: String
)
