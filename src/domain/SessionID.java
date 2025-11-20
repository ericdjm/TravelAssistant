package domain;

/**
 * Value object wrapping session identifier.
 */
public class SessionID {
    private String value;

    public SessionID(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionID sessionID = (SessionID) o;
        return value.equals(sessionID.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "SessionID{" + value + "}";
    }
}
