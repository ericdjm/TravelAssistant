package domain;

import java.util.List;

/**
 * Request for route/transit information.
 */
public class RouteRequest {
    private LatLng origin;
    private List<LatLng> destinations;
    private String mode;  // "walking", "driving", "transit"

    public RouteRequest() {
    }

    public RouteRequest(LatLng origin, List<LatLng> destinations, String mode) {
        this.origin = origin;
        this.destinations = destinations;
        this.mode = mode;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public List<LatLng> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<LatLng> destinations) {
        this.destinations = destinations;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
