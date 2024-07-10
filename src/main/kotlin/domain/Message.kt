package domain

data class Message(
    val from: String,
    val seq: Int,
    val topic: String,
    val ts: String
)
