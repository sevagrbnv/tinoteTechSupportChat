sealed class TinodeAction {

    data class Auth(
        val name: String,
        val details: String?,
        val login: String,
        val password: String,
    ) : TinodeAction()

    object CreateChats : TinodeAction()

    data class GetMessages(
        val topicId: String,
    ) : TinodeAction()

    data class GetPagingHistory(
        val topicId: String,
        val before: Int,
        val limit: Int = 24,
    ) : TinodeAction()

    object SendMessage : TinodeAction()

    object CloseConnection: TinodeAction()
}