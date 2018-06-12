package microservices.book.multiplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MultiplicationServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(MultiplicationServiceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner eventTester(EventDispatcher eventDispatcher) {
//		return args -> {
//			eventDispatcher.send(new MultiplicationSolvedEvent(23L, 34L, true));
//		};
//	}
}
