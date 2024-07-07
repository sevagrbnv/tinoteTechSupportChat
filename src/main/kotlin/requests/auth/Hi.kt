package requests.auth

import kotlinx.serialization.Serializable

@Serializable
data class Hi(
    val hi: HiBody = HiBody(),
) {
    @Serializable
    data class HiBody(
        val ver: String = "0.22.12",
        val ua: String = "TinodeWeb/0.22.13 (YaBrowser/24.4; Win32); tinodejs/0.22.12",
        val lang: String = "ru",
        val platf: String = "web",
    )
}
