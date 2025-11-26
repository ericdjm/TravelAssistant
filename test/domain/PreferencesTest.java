package test.domain;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import domain.Preferences;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit 5 tests for Preferences domain object.
 * Tests preference creation and validation.
 */
public class PreferencesTest {

    private Preferences preferences;

    @BeforeEach
    public void setUp() {
        preferences = new Preferences();
    }

    @AfterEach
    public void tearDown() {
        preferences = null;
    }

    // ========== Constructor Tests ==========

    @Test
    @DisplayName("Test default constructor")
    public void testDefaultConstructor() {
        // Arrange & Act
        Preferences prefs = new Preferences();

        // Assert
        assertNotNull(prefs, "Preferences object should not be null");
    }

    @Test
    @DisplayName("Test parameterized constructor")
    public void testParameterizedConstructor() {
        // Arrange
        List<String> interests = Arrays.asList("museums", "parks");
        
        // Act
        Preferences prefs = new Preferences(interests, "medium", 5000, "walking", false);

        // Assert
        assertNotNull(prefs);
        assertEquals("medium", prefs.getBudget());
        assertEquals(5000, prefs.getRadius());
        assertEquals("walking", prefs.getTransportMode());
        assertFalse(prefs.isAccessibilityNeeds());
        assertEquals(2, prefs.getInterests().size());
    }

    // ========== Interests Tests ==========

    @Test
    @DisplayName("Test set and get interests")
    public void testSetGetInterests() {
        // Arrange
        List<String> interests = Arrays.asList("museums", "restaurants", "parks");

        // Act
        preferences.setInterests(interests);

        // Assert
        assertEquals(3, preferences.getInterests().size());
        assertTrue(preferences.getInterests().contains("museums"));
        assertTrue(preferences.getInterests().contains("restaurants"));
    }

    @Test
    @DisplayName("Test set null interests")
    public void testSetNullInterests() {
        // Act
        preferences.setInterests(null);

        // Assert
        assertNull(preferences.getInterests());
    }

    @Test
    @DisplayName("Test set empty interests list")
    public void testSetEmptyInterests() {
        // Arrange
        List<String> emptyList = Arrays.asList();

        // Act
        preferences.setInterests(emptyList);

        // Assert
        assertNotNull(preferences.getInterests());
        assertEquals(0, preferences.getInterests().size());
    }

    // ========== Budget Tests ==========

    @Test
    @DisplayName("Test set and get budget - low")
    public void testSetGetBudgetLow() {
        // Act
        preferences.setBudget("low");

        // Assert
        assertEquals("low", preferences.getBudget());
    }

    @Test
    @DisplayName("Test set and get budget - medium")
    public void testSetGetBudgetMedium() {
        // Act
        preferences.setBudget("medium");

        // Assert
        assertEquals("medium", preferences.getBudget());
    }

    @Test
    @DisplayName("Test set and get budget - high")
    public void testSetGetBudgetHigh() {
        // Act
        preferences.setBudget("high");

        // Assert
        assertEquals("high", preferences.getBudget());
    }

    @Test
    @DisplayName("Test set null budget")
    public void testSetNullBudget() {
        // Act
        preferences.setBudget(null);

        // Assert
        assertNull(preferences.getBudget());
    }

    // ========== Radius Tests ==========

    @Test
    @DisplayName("Test set and get radius")
    public void testSetGetRadius() {
        // Act
        preferences.setRadius(5000);

        // Assert
        assertEquals(5000, preferences.getRadius());
    }

    @Test
    @DisplayName("Test set small radius")
    public void testSetSmallRadius() {
        // Act
        preferences.setRadius(500);

        // Assert
        assertEquals(500, preferences.getRadius());
    }

    @Test
    @DisplayName("Test set large radius")
    public void testSetLargeRadius() {
        // Act
        preferences.setRadius(50000);

        // Assert
        assertEquals(50000, preferences.getRadius());
    }

    @Test
    @DisplayName("Test set zero radius")
    public void testSetZeroRadius() {
        // Act
        preferences.setRadius(0);

        // Assert
        assertEquals(0, preferences.getRadius());
    }

    // ========== Transport Mode Tests ==========

    @Test
    @DisplayName("Test set and get transport mode - walking")
    public void testSetGetTransportModeWalking() {
        // Act
        preferences.setTransportMode("walking");

        // Assert
        assertEquals("walking", preferences.getTransportMode());
    }

    @Test
    @DisplayName("Test set and get transport mode - driving")
    public void testSetGetTransportModeDriving() {
        // Act
        preferences.setTransportMode("driving");

        // Assert
        assertEquals("driving", preferences.getTransportMode());
    }

    @Test
    @DisplayName("Test set and get transport mode - transit")
    public void testSetGetTransportModeTransit() {
        // Act
        preferences.setTransportMode("transit");

        // Assert
        assertEquals("transit", preferences.getTransportMode());
    }

    @Test
    @DisplayName("Test set null transport mode")
    public void testSetNullTransportMode() {
        // Act
        preferences.setTransportMode(null);

        // Assert
        assertNull(preferences.getTransportMode());
    }

    // ========== Accessibility Tests ==========

    @Test
    @DisplayName("Test set and get accessibility needs - true")
    public void testSetGetAccessibilityNeedsTrue() {
        // Act
        preferences.setAccessibilityNeeds(true);

        // Assert
        assertTrue(preferences.isAccessibilityNeeds());
    }

    @Test
    @DisplayName("Test set and get accessibility needs - false")
    public void testSetGetAccessibilityNeedsFalse() {
        // Act
        preferences.setAccessibilityNeeds(false);

        // Assert
        assertFalse(preferences.isAccessibilityNeeds());
    }

    // ========== Integration Tests ==========

    @Test
    @DisplayName("Test set all preferences")
    public void testSetAllPreferences() {
        // Arrange
        List<String> interests = Arrays.asList("museums", "restaurants");

        // Act
        preferences.setInterests(interests);
        preferences.setBudget("medium");
        preferences.setRadius(5000);
        preferences.setTransportMode("walking");
        preferences.setAccessibilityNeeds(true);

        // Assert
        assertEquals(2, preferences.getInterests().size());
        assertEquals("medium", preferences.getBudget());
        assertEquals(5000, preferences.getRadius());
        assertEquals("walking", preferences.getTransportMode());
        assertTrue(preferences.isAccessibilityNeeds());
    }

    @Test
    @DisplayName("Test update preferences")
    public void testUpdatePreferences() {
        // Arrange
        preferences.setBudget("low");
        preferences.setRadius(1000);

        // Act - Update
        preferences.setBudget("high");
        preferences.setRadius(10000);

        // Assert
        assertEquals("high", preferences.getBudget());
        assertEquals(10000, preferences.getRadius());
    }
}
