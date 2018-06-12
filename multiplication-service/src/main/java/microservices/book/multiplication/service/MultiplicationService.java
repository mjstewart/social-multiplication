package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;
import java.util.Optional;

public interface MultiplicationService {
    /**
     * Creates a Multiplication with 2 randomly generated factors between 11 and 99.
     *
     * @return A Multiplication with random factors
     */
    Multiplication createRandomMultiplication();

    /**
     * @return {@code true} if the attempt matches the result of the multiplication, otherwise {@code false}.
     */
    MultiplicationResultAttempt checkAttempt(final MultiplicationResultAttempt attempt);

    /**
     * Gets the statistics for a given user.
     *
     * @param userAlias The alias for the given user.
     * @return a list of {@link MultiplicationResultAttempt}s representing the users history of attempting multiplications.
     */
    List<MultiplicationResultAttempt> getStatsForUser(final String userAlias);

    /**
     * Get a {@code MultiplicationResultAttempt} by id
     */
    Optional<MultiplicationResultAttempt> getResultAttempt(final Long id);
}
