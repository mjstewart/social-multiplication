package microservices.book.multiplication.service;

import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;
    private final UserRepository userRepository;
    private final MultiplicationRepository multiplicationRepository;
    private final MultiplicationResultAttemptRepository attemptRepository;
    private final EventDispatcher eventDispatcher;

    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService,
                                     UserRepository userRepository,
                                     MultiplicationRepository multiplicationRepository,
                                     MultiplicationResultAttemptRepository attemptRepository,
                                     EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.attemptRepository = attemptRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public MultiplicationResultAttempt checkAttempt(MultiplicationResultAttempt attempt) {
        Assert.isTrue(!attempt.isCorrect(), "You can't send an attempted marked as correct");

        /*
         * Given the supplied argument is generated through a REST call, the domain entities are not from JPA
         * which is why we use the repositories to check. If nothing exists then a new entity is persisted.
         */
        User user = userRepository.findByAlias(attempt.getUser().getAlias()).orElse(attempt.getUser());
        Multiplication multiplication = multiplicationRepository
                .findByFactorAAndFactorB(
                        attempt.getMultiplication().getFactorA(),
                        attempt.getMultiplication().getFactorB()
                ).orElse(attempt.getMultiplication());

        boolean isCorrect = attempt.getResultAttempt() ==
                attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                user, multiplication, attempt.getResultAttempt(), isCorrect
        );

        MultiplicationResultAttempt savedCheckedAttempt = attemptRepository.save(checkedAttempt);

        eventDispatcher.send(
                new MultiplicationSolvedEvent(
                        savedCheckedAttempt.getId(),
                        savedCheckedAttempt.getUser().getId(),
                        savedCheckedAttempt.isCorrect()
                )
        );

        return savedCheckedAttempt;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    @Override
    public Optional<MultiplicationResultAttempt> getResultAttempt(Long id) {
        return attemptRepository.findById(id);
    }
}
