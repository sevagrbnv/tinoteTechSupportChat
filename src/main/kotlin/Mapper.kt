import domain.Message
import domain.Topic
import responces.ChatMeta
import responces.messages.Data
import responces.messages.NewData

fun ChatMeta.Meta.Sub.toTopic() = Topic(
    id = topic,
    name = public.fn,
    //messages = MutableStateFlow(mutableListOf()),
    //user = mutableListOf()
)

fun Data.toMessage() = Message(
    content = data.content,
    from = data.from,
    seq = data.seq,
    topic = data.topic,
    ts = data.ts
)

fun NewData.toMessage() = Message(
    content = "${data.content.txt} ent: ${data.content.ent.size}",
    from = data.from,
    seq = data.seq,
    topic = data.topic,
    ts = data.ts
)