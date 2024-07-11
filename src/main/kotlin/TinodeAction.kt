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

    data class SendMessage(
        val topicId: String,
        val content: String
    ) : TinodeAction()

    data class LeaveChat(
        val topicId: String
    ) : TinodeAction()

    object CloseConnection: TinodeAction()
}