package microservices.book.utils.http;

import jdk.incubator.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

public class HttpUtils {

    private String baseUrl;

    public HttpUtils(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String post(String context, String jsonBody) throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(new URI(baseUrl + context))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublisher.fromString(jsonBody))
                .build();
        return send(request);
    }

    public String get(String context) throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(new URI(baseUrl + context))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();
        return send(request);
    }

    private String send(HttpRequest request) {
        try {
            HttpResponse<String> response  = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandler.asString());
            assertThat(response.statusCode()).isEqualTo(200);
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
