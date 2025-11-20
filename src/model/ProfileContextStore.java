package model;

import domain.*;
import java.util.Map;

/**
 * «entity» Information Expert for profiles and sessions.
 */
public class ProfileContextStore {

    private Map<UserID, Profile> profiles;      // aggregates Profile
    private Map<SessionID, Session> sessions;   // composes Session

    public void saveProfile(Profile p) {
        // TODO: persist or cache profile
    }

    public Profile loadProfile(UserID userId) {
        // TODO: load profile for given user
        return null;
    }

    public void saveSession(Session s) {
        // TODO: persist session
    }

    public Session loadSession(SessionID sessionId) {
        // TODO: load session by id
        return null;
    }
}
