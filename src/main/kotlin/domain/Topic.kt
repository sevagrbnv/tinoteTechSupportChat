package domain

import kotlinx.coroutines.flow.StateFlow

data class Topic(
    val id: String,
    val name: String,
    val user: List<String>?,
    val messages: StateFlow<List<Message>>?
)

