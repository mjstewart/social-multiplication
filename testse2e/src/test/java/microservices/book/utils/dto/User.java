package microservices.book.utils.dto;

public class User {
    private Long id;
    private String alias;

    public User(Long id, String alias) {
        this.id = id;
        this.alias = alias;
    }

    public User(String alias) {
        this(null, alias);
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }
}
