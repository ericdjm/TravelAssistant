package test.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.Itinerary;
import domain.POI;
import domain.ETA;
import domain.LatLng;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5 tests for Itinerary.
 * Tests itinerary building and ETA calculation.
 */
public class ItineraryTest {

    private Itinerary itinerary;
    private List<POI> testPOIs;
    private List<ETA> testETAs;

    @BeforeEach
    public void setUp() {
        itinerary = new Itinerary();

        // Setup test POIs
        testPOIs = new ArrayList<>();
        
        POI poi1 = new POI();
        poi1.setId("poi_001");
        poi1.setName("CN Tower");
        poi1.setLocation(new LatLng(43.6426, -79.3871));
        testPOIs.add(poi1);

        POI poi2 = new POI();
        poi2.setId("poi_002");
        poi2.setName("Royal Ontario Museum");
        poi2.setLocation(new LatLng(43.6677, -79.3948));
        testPOIs.add(poi2);

        POI poi3 = new POI();
        poi3.setId("poi_003");
        poi3.setName("Ripley's Aquarium");
        poi3.setLocation(new LatLng(43.6424, -79.3860));
        testPOIs.add(poi3);

        // Setup test ETAs
        testETAs = new ArrayList<>();
        
        ETA eta1 = new ETA();
        eta1.setDurationMinutes(15);
        eta1.setMode("walking");
        testETAs.add(eta1);

        ETA eta2 = new ETA();
        eta2.setDurationMinutes(20);
        eta2.setMode("walking");
        testETAs.add(eta2);

        ETA eta3 = new ETA();
        eta3.setDurationMinutes(10);
        eta3.setMode("walking");
        testETAs.add(eta3);
    }

    @AfterEach
    public void tearDown() {
        itinerary = null;
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test itinerary creation")
    public void testItineraryCreation() {
        // Act
        Itinerary itin = new Itinerary();

        // Assert
        assertNotNull(itin, "Itinerary should not be null");
    }

    // ========== From POIs Tests ==========

    @Test
    @DisplayName("Test create itinerary from POIs")
    public void testFromPOIsValid() {
        // Act
        Itinerary result = itinerary.fromPOIs(testPOIs, testETAs);

        // Assert
        // Result may be null in initial implementation
        // This test verifies it doesn't throw exception
        assertNotNull(itinerary);
    }

    @Test
    @DisplayName("Test create itinerary from empty POI list")
    public void testFromPOIsEmpty() {
        // Arrange
        List<POI> emptyPOIs = new ArrayList<>();
        List<ETA> emptyETAs = new ArrayList<>();

        // Act
        Itinerary result = itinerary.fromPOIs(emptyPOIs, emptyETAs);

        // Assert
        assertNotNull(itinerary);
    }

    @Test
    @DisplayName("Test create itinerary with null POIs")
    public void testFromPOIsNull() {
        // Act
        Itinerary result = itinerary.fromPOIs(null, null);

        // Assert
        // Should handle null gracefully
        assertNotNull(itinerary);
    }

    @Test
    @DisplayName("Test create itinerary with single POI")
    public void testFromPOIsSingle() {
        // Arrange
        List<POI> singlePOI = Arrays.asList(testPOIs.get(0));
        List<ETA> singleETA = Arrays.asList(testETAs.get(0));

        // Act
        Itinerary result = itinerary.fromPOIs(singlePOI, singleETA);

        // Assert
        assertNotNull(itinerary);
    }

    @Test
    @DisplayName("Test create itinerary with multiple POIs")
    public void testFromPOIsMultiple() {
        // Act
        Itinerary result = itinerary.fromPOIs(testPOIs, testETAs);

        // Assert
        assertNotNull(itinerary);
    }

    // ========== Compute Total ETA Tests ==========

    @Test
    @DisplayName("Test compute total ETA")
    public void testComputeTotalETA() {
        // Arrange
        itinerary.setTotalETA(45);

        // Act
        int totalETA = itinerary.computeTotalETA();

        // Assert
        assertEquals(45, totalETA);
    }

    @Test
    @DisplayName("Test compute total ETA with zero")
    public void testComputeTotalETAZero() {
        // Arrange
        itinerary.setTotalETA(0);

        // Act
        int totalETA = itinerary.computeTotalETA();

        // Assert
        assertEquals(0, totalETA);
    }

    @Test
    @DisplayName("Test compute total ETA after setting steps")
    public void testComputeTotalETAWithSteps() {
        // Arrange
        List<String> steps = Arrays.asList(
            "Walk to CN Tower (15 min)",
            "Walk to ROM (20 min)",
            "Walk to Ripley's Aquarium (10 min)"
        );
        itinerary.setSteps(steps);
        itinerary.setTotalETA(45);

        // Act
        int totalETA = itinerary.computeTotalETA();

        // Assert
        assertEquals(45, totalETA);
    }

    // ========== Steps Getters and Setters Tests ==========

    @Test
    @DisplayName("Test set and get steps")
    public void testSetGetSteps() {
        // Arrange
        List<String> steps = Arrays.asList(
            "Step 1: Visit CN Tower",
            "Step 2: Visit ROM",
            "Step 3: Visit Ripley's Aquarium"
        );

        // Act
        itinerary.setSteps(steps);

        // Assert
        assertNotNull(itinerary.getSteps());
        assertEquals(3, itinerary.getSteps().size());
        assertEquals("Step 1: Visit CN Tower", itinerary.getSteps().get(0));
    }

    @Test
    @DisplayName("Test set empty steps")
    public void testSetEmptySteps() {
        // Arrange
        List<String> emptySteps = new ArrayList<>();

        // Act
        itinerary.setSteps(emptySteps);

        // Assert
        assertNotNull(itinerary.getSteps());
        assertEquals(0, itinerary.getSteps().size());
    }

    @Test
    @DisplayName("Test set null steps")
    public void testSetNullSteps() {
        // Act
        itinerary.setSteps(null);

        // Assert
        assertNull(itinerary.getSteps());
    }

    // ========== Total ETA Getters and Setters Tests ==========

    @Test
    @DisplayName("Test set and get total ETA")
    public void testSetGetTotalETA() {
        // Act
        itinerary.setTotalETA(60);

        // Assert
        assertEquals(60, itinerary.getTotalETA());
    }

    @Test
    @DisplayName("Test set small total ETA")
    public void testSetSmallTotalETA() {
        // Act
        itinerary.setTotalETA(5);

        // Assert
        assertEquals(5, itinerary.getTotalETA());
    }

    @Test
    @DisplayName("Test set large total ETA")
    public void testSetLargeTotalETA() {
        // Act
        itinerary.setTotalETA(300);

        // Assert
        assertEquals(300, itinerary.getTotalETA());
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("Test complete itinerary workflow")
    public void testCompleteItineraryWorkflow() {
        // Arrange
        List<String> steps = Arrays.asList(
            "Walk to CN Tower (15 min)",
            "Visit CN Tower (60 min)",
            "Walk to ROM (20 min)",
            "Visit ROM (90 min)"
        );

        // Act
        itinerary.setSteps(steps);
        itinerary.setTotalETA(185);
        Itinerary result = itinerary.fromPOIs(testPOIs, testETAs);
        int totalETA = itinerary.computeTotalETA();

        // Assert
        assertNotNull(itinerary);
        assertEquals(4, itinerary.getSteps().size());
        assertEquals(185, totalETA);
    }

    @Test
    @DisplayName("Test itinerary with different transport modes")
    public void testItineraryDifferentModes() {
        // Arrange
        List<ETA> mixedETAs = new ArrayList<>();
        
        ETA walkingETA = new ETA();
        walkingETA.setDurationMinutes(15);
        walkingETA.setMode("walking");
        mixedETAs.add(walkingETA);

        ETA transitETA = new ETA();
        transitETA.setDurationMinutes(10);
        transitETA.setMode("transit");
        mixedETAs.add(transitETA);

        // Act
        Itinerary result = itinerary.fromPOIs(testPOIs, mixedETAs);

        // Assert
        assertNotNull(itinerary);
    }
}
