package domain;

/**
 * Search query parameters.
 */
public class Query {
    private LatLng location;
    private Preferences preferences;
    private String searchTerm;
    private int maxResults;

    public Query() {
    }

    public Query(LatLng location, Preferences preferences, String searchTerm, int maxResults) {
        this.location = location;
        this.preferences = preferences;
        this.searchTerm = searchTerm;
        this.maxResults = maxResults;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
}
