package domain;

/**
 * Value object wrapping user identifier.
 */
public class UserID {
    private String value;

    public UserID(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserID userID = (UserID) o;
        return value.equals(userID.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "UserID{" + value + "}";
    }
}
