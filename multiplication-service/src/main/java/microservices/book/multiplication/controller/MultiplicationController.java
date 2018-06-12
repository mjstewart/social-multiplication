package microservices.book.multiplication.controller;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.service.MultiplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multiplications")
public final class MultiplicationController {

    private final MultiplicationService multiplicationService;
    private final static Logger logger = LoggerFactory.getLogger(MultiplicationController.class);

    public MultiplicationController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @GetMapping("random")
    public ResponseEntity<Multiplication> getRandomMultiplication() {
        // debug to see load balancing in action across multiple instances.
        logger.info("/random called");
        return ResponseEntity.ok(multiplicationService.createRandomMultiplication());
    }
}
