package microservices.book.multiplication.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public final class MultiplicationResultAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private final User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "multiplication_id")
    private final Multiplication multiplication;

    private final int resultAttempt;
    private final boolean correct;

    public MultiplicationResultAttempt(User user,
                                       Multiplication multiplication,
                                       int resultAttempt,
                                       boolean correct) {
        this.user = user;
        this.multiplication = multiplication;
        this.resultAttempt = resultAttempt;
        this.correct = correct;
    }

    protected MultiplicationResultAttempt() {
        this(null, null, 0, false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Multiplication getMultiplication() {
        return multiplication;
    }

    public int getResultAttempt() {
        return resultAttempt;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiplicationResultAttempt that = (MultiplicationResultAttempt) o;
        return resultAttempt == that.resultAttempt &&
                correct == that.correct &&
                Objects.equals(user, that.user) &&
                Objects.equals(multiplication, that.multiplication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, multiplication, resultAttempt, correct);
    }

    @Override
    public String toString() {
        return "MultiplicationResultAttempt{" +
                "user=" + user +
                ", multiplication=" + multiplication +
                ", resultAttempt=" + resultAttempt +
                ", correct=" + correct +
                '}';
    }
}
