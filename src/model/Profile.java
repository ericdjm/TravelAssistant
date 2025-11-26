package model;

import domain.*;

public class Profile {

    private UserID userId;
    private Preferences preferences;

    public UserID getUserId() {
        return userId;
    }

    public void setUserId(UserID userId) {
        this.userId = userId;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
