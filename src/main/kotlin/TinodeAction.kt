sealed class TinodeAction {

    data class Auth(
        val name: String,
        val details: String?,
        val login: String,
        val password: String
    ) : TinodeAction()

    object CreateChats : TinodeAction()

    object GetMessages : TinodeAction()

    object SendMessage : TinodeAction()
}