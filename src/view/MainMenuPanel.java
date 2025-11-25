package view;

import controller.*;
import domain.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Main menu panel showing profile and session management.
 *
 * Features:
 * - Load user profile from database
 * - View previous sessions
 * - Start new session with saved preferences
 *
 * @author CPS731 Team 20
 */
public class MainMenuPanel extends JPanel {

    private ConversationEngine conversationEngine;
    private MobileAppUI parentUI;

    // Current user
    private Profile currentProfile;
    private UserID currentUserId;

    // UI Components
    private JLabel welcomeLabel;
    private JPanel profileInfoPanel;
    private JPanel sessionsPanel;
    private JButton loadProfileButton;
    private JButton startNewSessionButton;
    private JButton guestSessionButton;

    public MainMenuPanel(MobileAppUI parent, ConversationEngine engine) {
        this.parentUI = parent;
        this.conversationEngine = engine;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Center panel with everything
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Welcome section
        welcomeLabel = new JLabel("Welcome to Travel Assistant!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Load Profile section
        JPanel loadProfilePanel = createLoadProfilePanel();
        centerPanel.add(loadProfilePanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Profile info (hidden until loaded)
        profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBorder(BorderFactory.createTitledBorder("Your Profile"));
        profileInfoPanel.setVisible(false);
        centerPanel.add(profileInfoPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Previous sessions (hidden until profile loaded)
        sessionsPanel = new JPanel();
        sessionsPanel.setLayout(new BoxLayout(sessionsPanel, BoxLayout.Y_AXIS));
        sessionsPanel.setBorder(BorderFactory.createTitledBorder("Previous Sessions"));
        sessionsPanel.setVisible(false);
        centerPanel.add(sessionsPanel);
        centerPanel.add(Box.createVerticalStrut(20));

        // Action buttons
        JPanel buttonsPanel = createButtonsPanel();
        centerPanel.add(buttonsPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createLoadProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createTitledBorder("Sign In"));

        JLabel label = new JLabel("User ID:");
        JTextField userIdField = new JTextField(15);

        loadProfileButton = new JButton("Load Profile");
        loadProfileButton.addActionListener(e -> {
            String userIdStr = userIdField.getText().trim();
            if (!userIdStr.isEmpty()) {
                loadUserProfile(userIdStr);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please enter a User ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(userIdField);
        panel.add(loadProfileButton);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        startNewSessionButton = new JButton("Start New Session");
        startNewSessionButton.setPreferredSize(new Dimension(180, 40));
        startNewSessionButton.setBackground(new Color(25, 118, 210));
        startNewSessionButton.setForeground(Color.WHITE);
        startNewSessionButton.setFont(new Font("Arial", Font.BOLD, 14));
        startNewSessionButton.setEnabled(false); // Disabled until profile loaded
        startNewSessionButton.addActionListener(e -> startNewSession());

        guestSessionButton = new JButton("Continue as Guest");
        guestSessionButton.setPreferredSize(new Dimension(180, 40));
        guestSessionButton.setFont(new Font("Arial", Font.PLAIN, 14));
        guestSessionButton.addActionListener(e -> startGuestSession());

        panel.add(startNewSessionButton);
        panel.add(guestSessionButton);

        return panel;
    }

    /**
     * Load user profile from database.
     */
    private void loadUserProfile(String userIdStr) {
        System.out.println("[MainMenu] Loading profile for user: " + userIdStr);

        currentUserId = new UserID(userIdStr);
        currentProfile = conversationEngine.loadProfile(currentUserId);

        if (currentProfile == null) {
            // Profile doesn't exist - offer to create one
            int response = JOptionPane.showConfirmDialog(this,
                "Profile not found. Create a new profile for user '" + userIdStr + "'?",
                "Create Profile",
                JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                createNewProfile(userIdStr);
            }
        } else {
            // Profile loaded successfully
            displayProfile(currentProfile);
            loadPreviousSessions(currentUserId);
            startNewSessionButton.setEnabled(true);

            JOptionPane.showMessageDialog(this,
                "Welcome back, " + userIdStr + "!",
                "Profile Loaded",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Create new profile for user.
     */
    private void createNewProfile(String userIdStr) {
        currentUserId = new UserID(userIdStr);
        currentProfile = new Profile();
        currentProfile.setUserId(currentUserId);

        // Set default preferences
        Preferences prefs = new Preferences();
        prefs.setInterests(Arrays.asList("restaurants", "museums"));
        prefs.setBudget("medium");
        prefs.setRadius(5000);
        prefs.setTransportMode("walking");
        prefs.setAccessibilityNeeds(false);

        currentProfile.setPreferences(prefs);

        // Save to database
        conversationEngine.saveProfile(currentProfile);

        // Display
        displayProfile(currentProfile);
        startNewSessionButton.setEnabled(true);

        System.out.println("✓ New profile created for: " + userIdStr);
    }

    /**
     * Display profile information.
     */
    private void displayProfile(Profile profile) {
        profileInfoPanel.removeAll();

        Preferences prefs = profile.getPreferences();

        JLabel userLabel = new JLabel("User: " + profile.getUserId().getValue());
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel interestsLabel = new JLabel("Interests: " + prefs.getInterests());
        JLabel budgetLabel = new JLabel("Budget: " + prefs.getBudget());
        JLabel radiusLabel = new JLabel("Radius: " + prefs.getRadius() + "m");
        JLabel transportLabel = new JLabel("Transport: " + prefs.getTransportMode());

        profileInfoPanel.add(userLabel);
        profileInfoPanel.add(Box.createVerticalStrut(5));
        profileInfoPanel.add(interestsLabel);
        profileInfoPanel.add(budgetLabel);
        profileInfoPanel.add(radiusLabel);
        profileInfoPanel.add(transportLabel);

        profileInfoPanel.setVisible(true);
        profileInfoPanel.revalidate();
        profileInfoPanel.repaint();

        // Update welcome message
        welcomeLabel.setText("Welcome back, " + profile.getUserId().getValue() + "!");
    }

    /**
     * Load previous sessions from database.
     */
    private void loadPreviousSessions(UserID userId) {
        sessionsPanel.removeAll();

        // For now, just show a message
        // In full implementation, we would query sessions table
        JLabel label = new JLabel("Previous sessions: (Feature coming soon)");
        label.setFont(new Font("Arial", Font.ITALIC, 12));
        sessionsPanel.add(label);

        sessionsPanel.setVisible(true);
        sessionsPanel.revalidate();
        sessionsPanel.repaint();
    }

    /**
     * Start new session with user's saved preferences.
     */
    private void startNewSession() {
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(this,
                "Please load a profile first",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("[MainMenu] Starting new session for: " + currentUserId.getValue());

        // Switch to planning panel with saved preferences
        parentUI.switchToPlanningPanel(currentProfile.getPreferences());
    }

    /**
     * Start guest session with default preferences.
     */
    private void startGuestSession() {
        System.out.println("[MainMenu] Starting guest session");

        // Create default preferences
        Preferences prefs = new Preferences();
        prefs.setInterests(Arrays.asList("restaurants"));
        prefs.setBudget("medium");
        prefs.setRadius(5000);
        prefs.setTransportMode("walking");
        prefs.setAccessibilityNeeds(false);

        // Switch to planning panel
        parentUI.switchToPlanningPanel(prefs);
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public UserID getCurrentUserId() {
        return currentUserId;
    }
}
