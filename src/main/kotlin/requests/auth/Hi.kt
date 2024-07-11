package requests.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hi(
    @SerialName("hi")
    val hi: HiBody = HiBody(),
) {
    @Serializable
    data class HiBody(
        @SerialName("ver")
        val ver: String = "0.22.12",
        @SerialName("ua")
        val ua: String = "TinodeWeb/0.22.13 (YaBrowser/24.4; Win32); tinodejs/0.22.12",
        @SerialName("lang")
        val lang: String = "ru",
        @SerialName("platf")
        val platf: String = "web",
    )
}
