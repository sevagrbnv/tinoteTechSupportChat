import domain.Message
import domain.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import responces.ChatMeta
import responces.messages.Data

fun ChatMeta.Meta.Sub.toTopic() = Topic(
    id = topic,
    name = public.fn,
    messages = MutableStateFlow(mutableListOf()),
    user = mutableListOf()
)

fun Data.toMessage() = Message(
    from = data.from,
    seq = data.seq,
    topic = data.topic,
    ts = data.ts
)