package microservices.book.gamificationservice.client

import microservices.book.gamificationservice.client.dto.MultiplicationResultAttempt

interface MultiplicationResultAttemptClient {
    fun retrieveMultiplicationResultAttemptById(attemptId: Long): MultiplicationResultAttempt?
}