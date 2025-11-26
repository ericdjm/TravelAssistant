package domain;

import java.util.List;

/**
 * Point of Interest domain object.
 */
public class POI {
    private String id;
    private String name;
    private LatLng location;
    private String category;       // e.g., "restaurant", "museum", "park"
    private float rating;          // 0.0 to 5.0
    private String priceLevel;     // "$", "$$", "$$$", "$$$$"
    private List<String> tags;     // e.g., "outdoor", "family-friendly"
    private boolean openNow;
    private String address;

    public POI() {
    }

    public POI(String id, String name, LatLng location, String category, float rating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.category = category;
        this.rating = rating;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
