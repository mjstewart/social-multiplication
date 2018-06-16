package microservices.book.multiplication.controller;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;
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
        System.out.println("getResultAttempt for " + attemptId);
        ResponseEntity<MultiplicationResultAttempt> result = multiplicationService.getResultAttempt(attemptId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        System.out.println("returning result=" + result);
        return result;
    }
}
