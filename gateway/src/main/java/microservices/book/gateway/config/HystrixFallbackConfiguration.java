package microservices.book.gateway.config;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The circuit breaker pattern is based on 2 states.
 * Closed circuit = Everything is ok, request can reach final destination and the response is received.
 * Open circuit = If there are errors or a timeout expires, the circuit gets opened which implies 'rerouting' requests
 * somewhere else to avoid overloading the failing service.
 *
 * The fallback provides some default functionality for the circuit when the circuit is tripped.
 * For example, if you wrap a request to a service in a Hystrix Command (which is what zuul does by default)
 * and provide a fallback, than when the circuit is tripped and a request is made to the service the fallback
 * will be invoked instead.
 *
 * This is the general flow.
 * 1. request comes in to zuul gateway.
 * 2. zuul checks if service is healthy
 * 3. if service is down and zuul fails to forward request, it will check if there is a fallback for that specific
 * service using getRoute(). If there is one it will construct and return a default response which is constructed
 * via the ClientHttpResponse object via fallbackResponse().
 */
@Configuration
public class HystrixFallbackConfiguration implements FallbackProvider {


    /**
     * Note: For every route you want to create a fallback for, you need to create another bean like this class.
     * or use a "*" to catch every route.
     */
    @Override
    public String getRoute() {
        // this is the serviceId for the eureka service in application.yml
        return "multiplication-service";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        System.out.println("fallbackResponse: route=" + route + " ,cause=" + cause.getClass().getName());
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            System.out.println("fallbackResponse returning else");
            return response(HttpStatus.OK);
        }
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("{\"factorA\":\"Sorry, Service is Down!\",\"factorB\":\"?\",\"id\":null}".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                // since we're providing a fallback, spring doesn't automatically add cors to headers.
//                headers.setAccessControlAllowCredentials(true);
                headers.setAccessControlAllowOrigin("*");
                return headers;
            }
        };
    }
}
