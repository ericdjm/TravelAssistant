package service;

import domain.*;
import java.util.List;

/**
 * «service/indirection» stable boundary to external services.
 */
public class IntegrationLayer {

    private IPlacesService placesService;
    private ITransitService transitService;
    private IWeatherService weatherService;

    public List<POI> getNearbyPlaces(LatLng coords, Preferences prefs) {
        if (placesService == null) {
            System.err.println("⚠️ PlacesService not initialized");
            return new java.util.ArrayList<>();
        }
        return placesService.searchPlaces(coords, prefs);
    }

    public List<ETA> getETAs(RouteRequest req) {
        if (transitService == null) {
            System.err.println("⚠️ TransitService not initialized");
            return new java.util.ArrayList<>();
        }
        return transitService.getTransitETAs(req);
    }

    public LatLng geocode(String address) {
        if (placesService == null) {
            System.err.println("⚠️ PlacesService not initialized");
            return null;
        }
        return placesService.geocode(address);
    }

    public Weather getWeather(LatLng loc) {
        if (weatherService == null) {
            System.err.println("⚠️ WeatherService not initialized");
            return null;
        }
        return weatherService.getWeather(loc);
    }

    // Wiring helpers (optional)
    public void setPlacesService(IPlacesService placesService) {
        this.placesService = placesService;
    }

    public void setTransitService(ITransitService transitService) {
        this.transitService = transitService;
    }

    public void setWeatherService(IWeatherService weatherService) {
        this.weatherService = weatherService;
    }
}
