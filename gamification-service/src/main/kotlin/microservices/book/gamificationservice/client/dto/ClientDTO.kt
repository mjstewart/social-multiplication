package microservices.book.gamificationservice.client.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import microservices.book.gamificationservice.client.MultiplicationResultAttemptDeserializer

@JsonDeserialize(using = MultiplicationResultAttemptDeserializer::class)
data class MultiplicationResultAttempt(
        val userAlias: String,
        val multiplicationFactorA: Int,
        val multiplicationFactorB: Int,
        val resultAttempt: Int,
        val correct: Boolean
)