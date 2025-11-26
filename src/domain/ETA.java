package domain;

/**
 * Estimated Time of Arrival information.
 */
public class ETA {
    private int durationMinutes;
    private double distanceMeters;
    private String mode;  // "walking", "driving", "transit"

    public ETA() {
    }

    public ETA(int durationMinutes, double distanceMeters, String mode) {
        this.durationMinutes = durationMinutes;
        this.distanceMeters = distanceMeters;
        this.mode = mode;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
