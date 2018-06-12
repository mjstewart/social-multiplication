package microservices.book.gamificationservice.client

import microservices.book.gamificationservice.client.dto.MultiplicationResultAttempt
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MultiplicationResultAttemptClientImpl(
        private val restTemplate: RestTemplate,
        @Value("\${api-gateway.url}") private val apiGatewayUrl: String
) : MultiplicationResultAttemptClient {

    override fun retrieveMultiplicationResultAttemptById(attemptId: Long): MultiplicationResultAttempt? {
        return restTemplate.getForObject("$apiGatewayUrl/results/$attemptId",
                MultiplicationResultAttempt::class.java)
    }
}