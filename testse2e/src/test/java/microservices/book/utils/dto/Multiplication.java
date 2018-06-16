package microservices.book.utils.dto;

public class Multiplication {
    private int factorA;
    private int factorB;

    public Multiplication(int factorA, int factorB) {
        this.factorA = factorA;
        this.factorB = factorB;
    }

    public Multiplication() {
    }

    public int getFactorA() {
        return factorA;
    }

    public int getFactorB() {
        return factorB;
    }
}
