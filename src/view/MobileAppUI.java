package view;

import model.*;
import controller.*;
import domain.*;
import javax.swing.*;
import java.awt.*;

/**
 * Main UI for Travel Assistant.
 * Uses CardLayout to switch between MainMenu and Planning screens.
 *
 * @author CPS731 Team 20
 */
public class MobileAppUI extends JFrame {

    private ConversationEngine conversationEngine;

    // Panels
    private SignInPanel signInPanel;
    private MainMenuPanel mainMenuPanel;
    private PlanningPanel planningPanel;
    private SettingsPanel settingsPanel;

    // CardLayout for switching
    private CardLayout cardLayout;
    private JPanel cardsPanel;

    public MobileAppUI() {
        initUI();
    }

    /**
     * Initialize the Swing UI.
     */
    private void initUI() {
        setTitle("CPS731 Travel Assistant - Phase 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        // Main container with BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout());

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Cards panel with CardLayout
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Note: Panels will be added in setConversationEngine()
        // because they need the conversation engine

        mainContainer.add(cardsPanel, BorderLayout.CENTER);

        add(mainContainer);
    }

    /**
     * Create header panel with title.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 118, 210)); // Blue
        panel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("🗺️ Travel Assistant");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    /**
     * Set conversation engine and initialize panels.
     */
    public void setConversationEngine(ConversationEngine engine) {
        this.conversationEngine = engine;

        // Create panels
        signInPanel = new SignInPanel(this, conversationEngine);
        mainMenuPanel = new MainMenuPanel(this, conversationEngine);
        planningPanel = new PlanningPanel(this, conversationEngine);
        settingsPanel = new SettingsPanel(this, conversationEngine);

        // Add panels to card layout
        cardsPanel.add(signInPanel, "SIGN_IN");
        cardsPanel.add(mainMenuPanel, "MAIN_MENU");
        cardsPanel.add(planningPanel, "PLANNING");
        cardsPanel.add(settingsPanel, "SETTINGS");

        // Show sign in panel by default
        cardLayout.show(cardsPanel, "SIGN_IN");
    }

    /**
     * Switch to planning panel with saved preferences.
     * Note: Session should already be started by caller (MainMenuPanel or SignInPanel).
     */
    public void switchToPlanningPanel(Preferences prefs) {
        planningPanel.setPreferences(prefs);
        cardLayout.show(cardsPanel, "PLANNING");
    }

    /**
     * Switch to main menu with loaded profile.
     * Starts a new session for this user.
     */
    public void switchToMainMenuWithProfile(Profile profile) {
        if (profile != null && profile.getUserId() != null) {
            // Start session for authenticated user
            conversationEngine.startSession(profile.getUserId());
        }

        // Pass profile to main menu
        mainMenuPanel.loadProfileDirectly(profile);

        cardLayout.show(cardsPanel, "MAIN_MENU");
    }

    /**
     * Switch back to main menu.
     * Saves current session if active.
     */
    public void switchToMainMenu() {
        // Save session when going back
        conversationEngine.saveCurrentSession();

        cardLayout.show(cardsPanel, "MAIN_MENU");
    }

    /**
     * Switch to sign in screen (for sign out).
     */
    public void switchToSignIn() {
        cardLayout.show(cardsPanel, "SIGN_IN");
    }

    /**
     * Switch to settings panel.
     */
    public void switchToSettings(Profile profile) {
        settingsPanel.loadProfile(profile);
        cardLayout.show(cardsPanel, "SETTINGS");
    }

    /**
     * Switch back to main menu after settings.
     */
    public void switchToMainMenuAfterSettings() {
        // Reload profile to reflect any changes
        Profile currentProfile = mainMenuPanel.getCurrentProfile();
        if (currentProfile != null && currentProfile.getUserId() != null) {
            Profile refreshedProfile = conversationEngine.loadProfile(currentProfile.getUserId());
            if (refreshedProfile != null) {
                mainMenuPanel.loadProfileDirectly(refreshedProfile);
            }
        }
        cardLayout.show(cardsPanel, "MAIN_MENU");
    }

    /**
     * Main method to launch UI (for testing).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MobileAppUI ui = new MobileAppUI();
            ui.setVisible(true);
        });
    }
}
