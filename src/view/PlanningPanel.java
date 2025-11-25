package view;

import model.*;
import controller.*;
import domain.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Planning panel for entering preferences and viewing recommendations.
 *
 * @author CPS731 Team 20
 */
public class PlanningPanel extends JPanel {

    private ConversationEngine conversationEngine;
    private MobileAppUI parentUI;
    private List<RecommendationCard> displayedCards;

    // UI Components
    private JTextField interestsField;
    private JComboBox<String> budgetCombo;
    private JTextField radiusField;
    private JComboBox<String> transportCombo;
    private JButton startButton;
    private JButton backButton;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;

    public PlanningPanel(MobileAppUI parent, ConversationEngine engine) {
        this.parentUI = parent;
        this.conversationEngine = engine;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input panel (preferences)
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.WEST);

        // Results panel (cards)
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setBorder(BorderFactory.createTitledBorder("Recommendations"));
        add(resultsScrollPane, BorderLayout.CENTER);
    }

    /**
     * Create input panel with preference fields.
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Preferences"));
        panel.setPreferredSize(new Dimension(280, 0));

        // Back button
        backButton = new JButton("← Back to Main Menu");
        backButton.setMaximumSize(new Dimension(250, 30));
        backButton.addActionListener(e -> parentUI.switchToMainMenu());
        panel.add(backButton);
        panel.add(Box.createVerticalStrut(15));

        // Interests field
        panel.add(new JLabel("Interests (comma separated):"));
        interestsField = new JTextField("restaurants, museums");
        interestsField.setMaximumSize(new Dimension(250, 25));
        panel.add(interestsField);
        panel.add(Box.createVerticalStrut(10));

        // Budget combo
        panel.add(new JLabel("Budget:"));
        budgetCombo = new JComboBox<>(new String[]{"low", "medium", "high"});
        budgetCombo.setMaximumSize(new Dimension(250, 25));
        budgetCombo.setSelectedIndex(1); // Default to medium
        panel.add(budgetCombo);
        panel.add(Box.createVerticalStrut(10));

        // Radius field
        panel.add(new JLabel("Radius (meters):"));
        radiusField = new JTextField("5000");
        radiusField.setMaximumSize(new Dimension(250, 25));
        panel.add(radiusField);
        panel.add(Box.createVerticalStrut(10));

        // Transport mode combo
        panel.add(new JLabel("Transport Mode:"));
        transportCombo = new JComboBox<>(new String[]{"walking", "driving", "transit"});
        transportCombo.setMaximumSize(new Dimension(250, 25));
        panel.add(transportCombo);
        panel.add(Box.createVerticalStrut(20));

        // Start button
        startButton = new JButton("Start Planning");
        startButton.setMaximumSize(new Dimension(250, 40));
        startButton.setBackground(new Color(25, 118, 210));
        startButton.setForeground(Color.BLACK);
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.addActionListener(e -> handleStartPlanning());
        panel.add(startButton);

        return panel;
    }

    /**
     * Set preferences from saved profile.
     */
    public void setPreferences(Preferences prefs) {
        if (prefs == null) return;

        // Set interests
        if (prefs.getInterests() != null) {
            interestsField.setText(String.join(", ", prefs.getInterests()));
        }

        // Set budget
        if (prefs.getBudget() != null) {
            budgetCombo.setSelectedItem(prefs.getBudget());
        }

        // Set radius
        radiusField.setText(String.valueOf(prefs.getRadius()));

        // Set transport
        if (prefs.getTransportMode() != null) {
            transportCombo.setSelectedItem(prefs.getTransportMode());
        }
    }

    /**
     * Handle "Start Planning" button click.
     */
    private void handleStartPlanning() {
        if (conversationEngine == null) {
            JOptionPane.showMessageDialog(this,
                "ConversationEngine not initialized!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Capture preferences from UI
        Preferences prefs = capturePreferences();

        // Clear previous results
        resultsPanel.removeAll();

        // Show loading message
        JLabel loadingLabel = new JLabel("Loading recommendations...");
        loadingLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        resultsPanel.add(loadingLabel);
        resultsPanel.revalidate();
        resultsPanel.repaint();

        // Run planning in background thread to keep UI responsive
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                conversationEngine.startPlanning(prefs);
                return null;
            }

            @Override
            protected void done() {
                // Get recommendations and render
                List<RecommendationCard> cards = conversationEngine.getCurrentRecommendations();
                renderCards(cards);
            }
        };
        worker.execute();
    }

    /**
     * Capture user preferences from UI fields.
     */
    private Preferences capturePreferences() {
        Preferences prefs = new Preferences();

        // Parse interests (comma-separated)
        String interestsText = interestsField.getText().trim();
        if (!interestsText.isEmpty()) {
            String[] interestsArray = interestsText.split(",");
            List<String> interests = new ArrayList<>();
            for (String interest : interestsArray) {
                interests.add(interest.trim());
            }
            prefs.setInterests(interests);
        }

        // Budget
        prefs.setBudget((String) budgetCombo.getSelectedItem());

        // Radius
        try {
            int radius = Integer.parseInt(radiusField.getText().trim());
            prefs.setRadius(radius);
        } catch (NumberFormatException e) {
            prefs.setRadius(5000); // Default
        }

        // Transport mode
        prefs.setTransportMode((String) transportCombo.getSelectedItem());

        // Accessibility (default false for now)
        prefs.setAccessibilityNeeds(false);

        return prefs;
    }

    /**
     * Render recommendation cards in results panel.
     */
    public void renderCards(List<RecommendationCard> cards) {
        this.displayedCards = cards;

        // Clear previous results
        resultsPanel.removeAll();

        if (cards == null || cards.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No recommendations found. Try adjusting your preferences.");
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            resultsPanel.add(noResultsLabel);
            resultsPanel.revalidate();
            resultsPanel.repaint();
            return;
        }

        // Create card UI for each recommendation
        for (RecommendationCard card : cards) {
            JPanel cardPanel = createCardPanel(card);
            resultsPanel.add(cardPanel);
            resultsPanel.add(Box.createVerticalStrut(10));
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    /**
     * Create a card panel for a recommendation.
     */
    private JPanel createCardPanel(RecommendationCard card) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Left: POI info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(card.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel summaryLabel = new JLabel(card.getSummary());
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        summaryLabel.setForeground(Color.GRAY);

        JLabel detailsLabel = new JLabel(String.format("⭐ %.1f | 📍 %.0fm away",
            card.getRating(), card.getDistance()));
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(summaryLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        // Right: View Itinerary button
        JButton viewButton = new JButton("View Itinerary");
        viewButton.setPreferredSize(new Dimension(130, 30));
        viewButton.addActionListener(e -> handleCardClick(card));

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(viewButton, BorderLayout.EAST);

        return panel;
    }

    /**
     * Handle card click - show itinerary.
     */
    private void handleCardClick(RecommendationCard card) {
        if (conversationEngine == null) {
            return;
        }

        // Capture console output for itinerary
        conversationEngine.handleSelectCard(card.getPlaceId());

        // Build itinerary
        Itinerary itinerary = conversationEngine.getRecommendationEngine()
            .buildMicroItinerary(conversationEngine.getCurrentPreferences(), card);

        // Show in dialog
        showSteps(itinerary);
    }

    /**
     * Show itinerary steps in a dialog.
     */
    private void showSteps(Itinerary itinerary) {
        if (itinerary == null || itinerary.getSteps() == null) {
            return;
        }

        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        for (String step : itinerary.getSteps()) {
            sb.append(step).append("\n");
        }
        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Micro-Itinerary",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
