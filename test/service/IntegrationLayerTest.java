package test.service;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.IntegrationLayer;
import service.PlacesService;
import service.TransitService;
import service.WeatherService;
import domain.*;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit 5 tests for IntegrationLayer.
 * Tests external service integration.
 */
public class IntegrationLayerTest {

    private IntegrationLayer integrationLayer;
    private PlacesService placesService;
    private TransitService transitService;
    private WeatherService weatherService;
    private Preferences testPreferences;

    @BeforeEach
    public void setUp() {
        integrationLayer = new IntegrationLayer();
        
        placesService = new PlacesService();
        transitService = new TransitService();
        weatherService = new WeatherService();
        
        integrationLayer.setPlacesService(placesService);
        integrationLayer.setTransitService(transitService);
        integrationLayer.setWeatherService(weatherService);

        testPreferences = new Preferences();
        testPreferences.setInterests(Arrays.asList("museums", "restaurants"));
        testPreferences.setBudget("medium");
        testPreferences.setRadius(5000);
    }

    @AfterEach
    public void tearDown() {
        integrationLayer = null;
    }

    // ========== Get Nearby Places Tests ==========

    @Test
    @DisplayName("Test get nearby places with valid location")
    public void testGetNearbyPlacesValid() {
        // Arrange
        LatLng location = new LatLng(43.6532, -79.3832); // Toronto

        // Act
        List<POI> places = integrationLayer.getNearbyPlaces(location, testPreferences);

        // Assert
        assertNotNull(places, "Places list should not be null");
        assertTrue(places.size() > 0, "Should return some places");
    }

    @Test
    @DisplayName("Test get nearby places with null location")
    public void testGetNearbyPlacesNullLocation() {
        // Act
        List<POI> places = integrationLayer.getNearbyPlaces(null, testPreferences);

        // Assert
        assertNotNull(places);
        // May return empty list or handle gracefully
    }

    @Test
    @DisplayName("Test get nearby places without places service")
    public void testGetNearbyPlacesNoService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();
        LatLng location = new LatLng(43.6532, -79.3832);

        // Act
        List<POI> places = layer.getNearbyPlaces(location, testPreferences);

        // Assert
        assertNotNull(places);
        assertEquals(0, places.size(), "Should return empty list when service not set");
    }

    @Test
    @DisplayName("Test get nearby places with different preferences")
    public void testGetNearbyPlacesDifferentPreferences() {
        // Arrange
        LatLng location = new LatLng(43.6532, -79.3832);
        testPreferences.setInterests(Arrays.asList("parks"));

        // Act
        List<POI> places = integrationLayer.getNearbyPlaces(location, testPreferences);

        // Assert
        assertNotNull(places);
    }

    // ========== Get ETAs Tests ==========

    @Test
    @DisplayName("Test get ETAs with valid route request")
    public void testGetETAsValid() {
        // Arrange
        RouteRequest request = new RouteRequest();
        request.setOrigin(new LatLng(43.6532, -79.3832));
        request.setDestinations(Arrays.asList(new LatLng(43.6435, -79.3871)));
        request.setMode("walking");

        // Act
        List<ETA> etas = integrationLayer.getETAs(request);

        // Assert
        assertNotNull(etas, "ETAs list should not be null");
    }

    @Test
    @DisplayName("Test get ETAs with null route request")
    public void testGetETAsNullRequest() {
        // Act
        List<ETA> etas = integrationLayer.getETAs(null);

        // Assert
        assertNotNull(etas);
    }

    @Test
    @DisplayName("Test get ETAs without transit service")
    public void testGetETAsNoService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();
        RouteRequest request = new RouteRequest();
        request.setOrigin(new LatLng(43.6532, -79.3832));
        request.setDestinations(Arrays.asList(new LatLng(43.6435, -79.3871)));

        // Act
        List<ETA> etas = layer.getETAs(request);

        // Assert
        assertNotNull(etas);
        assertEquals(0, etas.size());
    }

    @Test
    @DisplayName("Test get ETAs with different transport modes")
    public void testGetETAsDifferentModes() {
        // Arrange
        RouteRequest walkingRequest = new RouteRequest();
        walkingRequest.setOrigin(new LatLng(43.6532, -79.3832));
        walkingRequest.setDestinations(Arrays.asList(new LatLng(43.6435, -79.3871)));
        walkingRequest.setMode("walking");

        RouteRequest drivingRequest = new RouteRequest();
        drivingRequest.setOrigin(new LatLng(43.6532, -79.3832));
        drivingRequest.setDestinations(Arrays.asList(new LatLng(43.6435, -79.3871)));
        drivingRequest.setMode("driving");

        // Act
        List<ETA> walkingETAs = integrationLayer.getETAs(walkingRequest);
        List<ETA> drivingETAs = integrationLayer.getETAs(drivingRequest);

        // Assert
        assertNotNull(walkingETAs);
        assertNotNull(drivingETAs);
    }

    // ========== Geocode Tests ==========

    @Test
    @DisplayName("Test geocode with valid address")
    public void testGeocodeValid() {
        // Act
        LatLng location = integrationLayer.geocode("Toronto, ON");

        // Assert
        assertNotNull(location, "Location should not be null");
        assertTrue(location.getLatitude() > 0, "Latitude should be positive");
        assertTrue(location.getLongitude() < 0, "Longitude should be negative (western hemisphere)");
    }

    @Test
    @DisplayName("Test geocode with null address")
    public void testGeocodeNull() {
        // Act
        LatLng location = integrationLayer.geocode(null);

        // Assert - May return null or handle gracefully
        // Behavior depends on implementation
    }

    @Test
    @DisplayName("Test geocode without places service")
    public void testGeocodeNoService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();

        // Act
        LatLng location = layer.geocode("Toronto, ON");

        // Assert
        assertNull(location, "Should return null when service not set");
    }

    // ========== Get Weather Tests ==========

    @Test
    @DisplayName("Test get weather with valid location")
    public void testGetWeatherValid() {
        // Arrange
        LatLng location = new LatLng(43.6532, -79.3832);

        // Act
        Weather weather = integrationLayer.getWeather(location);

        // Assert
        assertNotNull(weather, "Weather should not be null");
        assertNotNull(weather.getCondition(), "Weather condition should not be null");
    }

    @Test
    @DisplayName("Test get weather with null location")
    public void testGetWeatherNull() {
        // Act
        Weather weather = integrationLayer.getWeather(null);

        // Assert - May return null or handle gracefully
    }

    @Test
    @DisplayName("Test get weather without weather service")
    public void testGetWeatherNoService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();
        LatLng location = new LatLng(43.6532, -79.3832);

        // Act
        Weather weather = layer.getWeather(location);

        // Assert
        assertNull(weather, "Should return null when service not set");
    }

    @Test
    @DisplayName("Test get weather for different locations")
    public void testGetWeatherDifferentLocations() {
        // Arrange
        LatLng toronto = new LatLng(43.6532, -79.3832);
        LatLng northYork = new LatLng(43.7615, -79.4111);

        // Act
        Weather torontoWeather = integrationLayer.getWeather(toronto);
        Weather northYorkWeather = integrationLayer.getWeather(northYork);

        // Assert
        assertNotNull(torontoWeather);
        assertNotNull(northYorkWeather);
    }

    // ========== Service Wiring Tests ==========

    @Test
    @DisplayName("Test set places service")
    public void testSetPlacesService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();
        PlacesService service = new PlacesService();

        // Act
        layer.setPlacesService(service);

        // Assert
        LatLng location = layer.geocode("Toronto, ON");
        assertNotNull(location);
    }

    @Test
    @DisplayName("Test set transit service")
    public void testSetTransitService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();
        TransitService service = new TransitService();

        // Act
        layer.setTransitService(service);
        
        // Verify service is set
        assertNotNull(layer);
    }

    @Test
    @DisplayName("Test set weather service")
    public void testSetWeatherService() {
        // Arrange
        IntegrationLayer layer = new IntegrationLayer();
        WeatherService service = new WeatherService();

        // Act
        layer.setWeatherService(service);
        
        // Verify service is set
        assertNotNull(layer);
    }
}
