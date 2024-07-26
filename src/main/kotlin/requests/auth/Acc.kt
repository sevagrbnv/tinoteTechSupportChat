package requests.auth

@kotlinx.serialization.Serializable
data class Acc(
    val acc: AccBody
) {
    @kotlinx.serialization.Serializable
    data class AccBody(
        val user: String = "new",
        val scheme: String = "basic",
        val secret: String,
        val login: Boolean = false,
        val desc: Desc,
        val cred: List<Cred> = listOf(Cred())
    )

    @kotlinx.serialization.Serializable
    data class Desc(
        val public: Public
    )

    @kotlinx.serialization.Serializable
    data class Public(
        val fn: String
    )

    @kotlinx.serialization.Serializable
    data class Cred(
        val meth: String = "email",
        val `val`: String = "mail@mail.mail"
    )
}