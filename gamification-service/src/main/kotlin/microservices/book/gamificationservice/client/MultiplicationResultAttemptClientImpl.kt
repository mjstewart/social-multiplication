package microservices.book.gamificationservice.client

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import microservices.book.gamificationservice.client.dto.MultiplicationResultAttempt
import microservices.book.gamificationservice.controller.UserStatsController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MultiplicationResultAttemptClientImpl(
        private val restTemplate: RestTemplate,
        @Value("\${api-gateway.url}") private val apiGatewayUrl: String
) : MultiplicationResultAttemptClient {

    companion object {
        val logger = LoggerFactory.getLogger(UserStatsController::class.java)!!
    }

    @HystrixCommand(fallbackMethod = "defaultResult")
    override fun retrieveMultiplicationResultAttemptById(attemptId: Long): MultiplicationResultAttempt? {
        return restTemplate.getForObject("$apiGatewayUrl/results/$attemptId",
                MultiplicationResultAttempt::class.java)
    }

    private fun defaultResult(attemptId: Long): MultiplicationResultAttempt {
        logger.info("defaultResult triggered by circuit breaker")
        return MultiplicationResultAttempt("fakeAlias", 10,
                10, 100, true)
    }
}