package microservices.book.multiplication.service;

import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MultiplicationServiceImplTest {

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @InjectMocks
    private MultiplicationServiceImpl multiplicationService;


    @Test
    public void createRandomMultiplication() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttempt() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication,
                3000, false);

        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication,
                3000, true);

        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(verifiedAttempt.getId(),
                verifiedAttempt.getUser().getId(), true);

        given(userRepository.findByAlias("john")).willReturn(Optional.empty());
        given(attemptRepository.save(verifiedAttempt)).willReturn(verifiedAttempt);

        // when
        boolean attemptedResult = multiplicationService.checkAttempt(attempt).isCorrect();

        assertThat(attemptedResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(userRepository).findByAlias("john");
        verify(multiplicationRepository).findByFactorAAndFactorB(50, 60);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkWrongAttempt() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication,
                3010, false);

        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication,
                3010, false);

        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(verifiedAttempt.getId(),
                verifiedAttempt.getUser().getId(), false);

        given(userRepository.findByAlias("john")).willReturn(Optional.empty());
        given(attemptRepository.save(verifiedAttempt)).willReturn(verifiedAttempt);

        // when
        boolean attemptedResult = multiplicationService.checkAttempt(attempt).isCorrect();

        assertThat(attemptedResult).isFalse();
        verify(attemptRepository).save(verifiedAttempt);
        verify(userRepository).findByAlias("john");
        verify(multiplicationRepository).findByFactorAAndFactorB(50, 60);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveUserStats() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john");

        var attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        var attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);

        var latestAttempts = List.of(attempt1, attempt2);

        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john")).willReturn(latestAttempts);

        // when
        List<MultiplicationResultAttempt> resultAttempts = multiplicationService.getStatsForUser("john");

        // then
        assertThat(resultAttempts).isEqualTo(latestAttempts);
        verify(attemptRepository).findTop5ByUserAliasOrderByIdDesc("john");
    }

    @Test
    public void getResultAttempt() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john");
        Long attemptId = 2L;
        var attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);

        given(attemptRepository.findById(attemptId)).willReturn(Optional.of(attempt1));

        // when
        Optional<MultiplicationResultAttempt> result = multiplicationService.getResultAttempt(attemptId);

        // then
        assertThat(result.get()).isEqualTo(attempt1);
    }
}