package microservices.book.multiplication.event;

import java.io.Serializable;
import java.util.Objects;

/**
 * Event that models the fact that a {@link microservices.book.multiplication.domain.Multiplication} has been
 * solved and provides some further context around that fact.
 *
 * Events should be named as if they have already happened in the past.
 *
 * Also, the consumers can request more info if needed using a REST call with the various id's
 * rather than send 'fat events'. This keeps events immutable and removes the headaches of having to keep track of
 * ordering events in time if events are resent by broker etc.
 */
public class MultiplicationSolvedEvent implements Serializable {
    private Long multiplicationResultAttemptId;
    private Long userId;
    private boolean correct;

    public MultiplicationSolvedEvent(Long multiplicationResultAttemptId, Long userId, boolean correct) {
        this.multiplicationResultAttemptId = multiplicationResultAttemptId;
        this.userId = userId;
        this.correct = correct;
    }

    public MultiplicationSolvedEvent() {
    }

    public Long getMultiplicationResultAttemptId() {
        return multiplicationResultAttemptId;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiplicationSolvedEvent that = (MultiplicationSolvedEvent) o;
        return correct == that.correct &&
                Objects.equals(multiplicationResultAttemptId, that.multiplicationResultAttemptId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(multiplicationResultAttemptId, userId, correct);
    }

    @Override
    public String toString() {
        return "MultiplicationSolvedEvent{" +
                "multiplicationResultAttemptId=" + multiplicationResultAttemptId +
                ", userId=" + userId +
                ", correct=" + correct +
                '}';
    }
}
