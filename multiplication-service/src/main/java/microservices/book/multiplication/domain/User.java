package microservices.book.multiplication.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final String alias;

    public User(String alias) {
        this.alias = alias;
    }

    protected User() {
        this(null);
    }

    public String getAlias() {
        return alias;
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
        User user = (User) o;
        return Objects.equals(alias, user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "alias='" + alias + '\'' +
                '}';
    }
}
