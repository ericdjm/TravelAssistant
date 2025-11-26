package model;

import domain.*;
import java.time.Instant;

/**
 * «entity» session state.
 */
public class Session {

    private SessionID sessionId;
    private UserID userId;
    private Instant createdAt;
    private int requestCount;

    public SessionID getSessionId() {
        return sessionId;
    }

    public void setSessionId(SessionID sessionId) {
        this.sessionId = sessionId;
    }

    public UserID getUserId() {
        return userId;
    }

    public void setUserId(UserID userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void incrementRequestCount() {
        requestCount++;
    }
}
