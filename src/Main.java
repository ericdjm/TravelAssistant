import controller.*;
import model.*;
import service.*;
import view.*;
import javax.swing.SwingUtilities;

/**
 * Main entry point for CPS731 Travel Assistant - Phase 3.
 * Wires all components together and launches the UI.
 *
 * Demonstrates:
 * - GRASP patterns (Controller, Information Expert, Indirection, etc.)
 * - MVC architecture
 * - MySQL persistence
 * - Mock service implementations
 * - Java Swing UI
 *
 * @author CPS731 Team 20 (Hussein, Eric, Omar)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("   CPS731 TRAVEL ASSISTANT - PHASE 3");
        System.out.println("   Full System Implementation");
        System.out.println("=".repeat(70));
        System.out.println();

        // Step 1: Initialize services (database-backed implementations)
        System.out.println("[Main] Initializing services...");
        PlacesService placesService = new PlacesService();
        TransitService transitService = new TransitService();
        WeatherService weatherService = new WeatherService();
        System.out.println();

        // Step 2: Initialize IntegrationLayer
        System.out.println("[Main] Initializing integration layer...");
        IntegrationLayer integrationLayer = new IntegrationLayer();
        integrationLayer.setPlacesService(placesService);
        integrationLayer.setTransitService(transitService);
        integrationLayer.setWeatherService(weatherService);
        System.out.println("✓ IntegrationLayer initialized");
        System.out.println();

        // Step 3: Initialize persistence (ProfileContextStore with MySQL)
        System.out.println("[Main] Initializing persistence layer...");
        ProfileContextStore profileContextStore = new ProfileContextStore();
        profileContextStore.initialize();
        System.out.println();

        // Step 4: Initialize RecommendationEngine
        System.out.println("[Main] Initializing recommendation engine...");
        RecommendationEngine recommendationEngine = new RecommendationEngine();
        recommendationEngine.setIntegrationLayer(integrationLayer);
        System.out.println("✓ RecommendationEngine initialized");
        System.out.println();

        // Step 5: Initialize ConversationEngine (Controller)
        System.out.println("[Main] Initializing conversation engine...");
        ConversationEngine conversationEngine = new ConversationEngine();
        conversationEngine.setRecommendationEngine(recommendationEngine);
        conversationEngine.setIntegrationLayer(integrationLayer);
        conversationEngine.setProfileContextStore(profileContextStore);
        System.out.println("✓ ConversationEngine initialized");
        System.out.println();

        // Step 6: Launch UI
        System.out.println("[Main] Launching UI...");
        System.out.println("=".repeat(70));
        System.out.println();

        SwingUtilities.invokeLater(() -> {
            MobileAppUI ui = new MobileAppUI();
            ui.setConversationEngine(conversationEngine);
            ui.setVisible(true);

            System.out.println("✅ UI launched successfully!");
            System.out.println();
            System.out.println("INSTRUCTIONS:");
            System.out.println("1. Enter your interests (e.g., 'restaurants, museums, parks')");
            System.out.println("2. Select budget, radius, and transport mode");
            System.out.println("3. Click 'Start Planning' to get recommendations");
            System.out.println("4. Click 'View Itinerary' on any card to see details");
            System.out.println();
            System.out.println("Console output will show system activity.");
            System.out.println("=".repeat(70));
        });
    }
}
