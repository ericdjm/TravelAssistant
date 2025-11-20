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
        // TODO: delegate to placesService
        return null;
    }

    public List<ETA> getETAs(RouteRequest req) {
        // TODO: delegate to transitService
        return null;
    }

    public LatLng geocode(String address) {
        // TODO: delegate to placesService
        return null;
    }

    public Weather getWeather(LatLng loc) {
        // TODO: delegate to weatherService
        return null;
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
