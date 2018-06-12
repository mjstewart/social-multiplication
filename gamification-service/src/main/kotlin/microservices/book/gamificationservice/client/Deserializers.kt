package microservices.book.gamificationservice.client

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import microservices.book.gamificationservice.client.dto.MultiplicationResultAttempt

class MultiplicationResultAttemptDeserializer : JsonDeserializer<MultiplicationResultAttempt>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiplicationResultAttempt =
            p.readValueAsTree<JsonNode>().let {
                MultiplicationResultAttempt(
                        it["user"]["alias"].asText(),
                        it["multiplication"]["factorA"].asInt(),
                        it["multiplication"]["factorB"].asInt(),
                        it["resultAttempt"].asInt(),
                        it["correct"].asBoolean()
                )
            }
}