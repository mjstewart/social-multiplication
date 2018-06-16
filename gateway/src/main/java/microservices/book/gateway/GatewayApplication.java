package microservices.book.gateway;

import microservices.book.gateway.config.DefaultRibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * The gateway implements the load balancing strategy too using Ribbon. microservices.book.gateway.config.DefaultRibbonConfiguration provides better
 * defaults to determine when a service is alive by pinging a spring actuator /health endpoint
 */
@EnableZuulProxy
@EnableEurekaClient
@EnableCircuitBreaker
// cant get this to work
//@RibbonClients(defaultConfiguration = DefaultRibbonConfiguration.class)
//@RibbonClient(name = "multiplication-service", configuration = DefaultRibbonConfiguration.class)
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
