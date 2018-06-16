package microservices.book.gateway.config;

import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * https://github.com/Netflix/ribbon/wiki/Working-with-load-balancers#weightedresponsetimerule
 */
//@Configuration
public class DefaultRibbonConfiguration {

    /**
     * Ping - a component running in background to ensure liveness of servers
     * <p>/health is provided by spring actuator that each microservice uses</p>
     */
    @Bean
    public IPing ribbonPing() {
        return new PingUrl(false, "/health");
    }

    /**
     * Rule - a logic component to determine which server to return from a list.
     * AvailabilityFilteringRule is an alternative to RoundRobinRule. It takes into account availability of
     * services based on the ping health check
     */
    @Bean
    public IRule ribbonRule() {
        return new AvailabilityFilteringRule();
    }
}
