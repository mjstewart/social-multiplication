package microservices.book.gateway.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.context.annotation.Bean;

/**
 * https://github.com/Netflix/ribbon/wiki/Working-with-load-balancers#weightedresponsetimerule
 */
public class RibbonConfiguration {
    /**
     * Ping - a component running in background to ensure liveness of servers
     * <p>/health is provided by spring actuator that each microservice uses</p>
     */
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl(false, "/health");
    }

    /**
     * Rule - a logic component to determine which server to return from a list.
     * AvailabilityFilteringRule is an alternative to RoundRobinRule. It takes into account availability of
     * services based on the ping health check
     */
    @Bean
    public IRule ribbonRule(IClientConfig config) {
        return new AvailabilityFilteringRule();
    }
}
