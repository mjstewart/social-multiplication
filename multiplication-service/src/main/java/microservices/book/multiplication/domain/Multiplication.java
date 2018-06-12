package microservices.book.multiplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public final class Multiplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private final int factorA;
    private final int factorB;

    public Multiplication(int factorA, int factorB) {
        this.factorA = factorA;
        this.factorB = factorB;
    }

    protected Multiplication() {
        this(0, 0);
    }

    public int getFactorA() {
        return factorA;
    }

    public int getFactorB() {
        return factorB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multiplication that = (Multiplication) o;
        return factorA == that.factorA &&
                factorB == that.factorB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(factorA, factorB);
    }

    @Override
    public String toString() {
        return "Multiplication{" +
                "factorA=" + factorA +
                ", factorB=" + factorB +
                '}';
    }
}
