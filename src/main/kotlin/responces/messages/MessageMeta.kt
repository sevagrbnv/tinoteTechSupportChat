package responces.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageMeta(
    @SerialName("meta")
    val meta: Meta,
) {
    @Serializable
    data class Meta(
//        @SerialName("desc")
//        val desc: Desc,
//        @SerialName("id")
//        val id: String,
        @SerialName("topic")
        val topic: String,
//        @SerialName("ts")
//        val ts: String
//    ) {
//        @Serializable
//        data class Desc(
//            @SerialName("acs")
//            val acs: Acs,
//            @SerialName("defacs")
//            val defacs: Defacs,
//            @SerialName("online")
//            val online: Boolean,
//            @SerialName("read")
//            val read: Int,
//            @SerialName("recv")
//            val recv: Int,
//            @SerialName("seq")
//            val seq: Int,
//            @SerialName("touched")
//            val touched: String,
//            @SerialName("updated")
//            val updated: String
    ) {
//            @Serializable
//            data class Acs(
//                @SerialName("given")
//                val given: String,
//                @SerialName("mode")
//                val mode: String,
//                @SerialName("want")
//                val want: String
//            )

//            @Serializable
//            data class Defacs(
//                @SerialName("anon")
//                val anon: String,
//                @SerialName("auth")
//                val auth: String
//            )
    }
}