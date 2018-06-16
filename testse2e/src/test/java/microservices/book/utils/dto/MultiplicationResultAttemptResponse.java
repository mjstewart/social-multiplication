package microservices.book.utils.dto;

public class MultiplicationResultAttemptResponse {
    private Long id;

    private User user;

    private Multiplication multiplication;

    private int resultAttempt;
    private boolean correct;

    public MultiplicationResultAttemptResponse(Long id, User user, Multiplication multiplication,
                                               int resultAttempt, boolean correct) {
        this.id = id;
        this.user = user;
        this.multiplication = multiplication;
        this.resultAttempt = resultAttempt;
        this.correct = correct;
    }

    public MultiplicationResultAttemptResponse() {
    }

    public Long getId() {
        return id;
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
}
