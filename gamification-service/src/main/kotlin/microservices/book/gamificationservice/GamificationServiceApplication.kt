package microservices.book.gamificationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class GamificationServiceApplication

fun main(args: Array<String>) {
    runApplication<GamificationServiceApplication>(*args)
}
