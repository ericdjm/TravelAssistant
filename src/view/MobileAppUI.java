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
    private MainMenuPanel mainMenuPanel;
    private PlanningPanel planningPanel;

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
        mainMenuPanel = new MainMenuPanel(this, conversationEngine);
        planningPanel = new PlanningPanel(this, conversationEngine);

        // Add panels to card layout
        cardsPanel.add(mainMenuPanel, "MAIN_MENU");
        cardsPanel.add(planningPanel, "PLANNING");

        // Show main menu by default
        cardLayout.show(cardsPanel, "MAIN_MENU");
    }

    /**
     * Switch to planning panel with saved preferences.
     */
    public void switchToPlanningPanel(Preferences prefs) {
        planningPanel.setPreferences(prefs);
        cardLayout.show(cardsPanel, "PLANNING");
    }

    /**
     * Switch back to main menu.
     */
    public void switchToMainMenu() {
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
