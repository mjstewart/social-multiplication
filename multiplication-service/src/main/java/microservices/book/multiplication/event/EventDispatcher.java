package microservices.book.multiplication.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {
    private RabbitTemplate rabbitTemplate;

    private String multiplicationExchange;

    private String multiplicationSolvedRoutingKey;

    private final static Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

    public EventDispatcher(RabbitTemplate rabbitTemplate,
                           @Value("${multiplication.exchange}") String multiplicationExchange,
                           @Value("${multiplication.solved.routing-key}") String multiplicationSolvedRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.multiplicationExchange = multiplicationExchange;
        this.multiplicationSolvedRoutingKey = multiplicationSolvedRoutingKey;
    }

    public void send(MultiplicationSolvedEvent multiplicationSolvedEvent) {
        logger.info("Sending multiplication solved event into rabbit: {}", multiplicationSolvedEvent);

        rabbitTemplate.convertAndSend(
                multiplicationExchange,
                multiplicationSolvedRoutingKey,
                multiplicationSolvedEvent
        );
    }
}
