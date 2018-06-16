package microservices.book.utils.dto;

public class MultiplicationResultAttemptRequest {
    private User user;

    private Multiplication multiplication;

    private int resultAttempt;
    private boolean correct;

    public MultiplicationResultAttemptRequest(User user, Multiplication multiplication,
                                              int resultAttempt, boolean correct) {
        this.user = user;
        this.multiplication = multiplication;
        this.resultAttempt = resultAttempt;
        this.correct = correct;
    }

    public MultiplicationResultAttemptRequest() {
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
