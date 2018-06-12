package microservices.book.multiplication.controller;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public final class MultiplicationResultAttemptController {
    private final MultiplicationService multiplicationService;

    public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    public ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt attempt) {
        MultiplicationResultAttempt verifiedAttempt = multiplicationService.checkAttempt(attempt);
        return ResponseEntity.ok(verifiedAttempt);
    }

    @GetMapping
    public ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<MultiplicationResultAttempt> getResultAttempt(@PathVariable Long attemptId) {

        return multiplicationService.getResultAttempt(attemptId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
