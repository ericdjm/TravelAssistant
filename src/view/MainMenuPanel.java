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
    private JPanel loadProfilePanel;
    private JPanel profileInfoPanel;
    private JPanel sessionsPanel;
    private JPanel loggedInButtonsPanel;
    private JButton loadProfileButton;
    private JButton startNewSessionButton;
    private JButton signOutButton;
    private JButton refreshDataButton;

    public MainMenuPanel(MobileAppUI parent, ConversationEngine engine) {
        this.parentUI = parent;
        this.conversationEngine = engine;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome section at top
        JPanel topPanel = new JPanel();
        welcomeLabel = new JLabel("Welcome to Travel Assistant!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.NORTH);

        // Main content area - horizontal split
        JPanel mainContentPanel = new JPanel(new BorderLayout(15, 0));

        // LEFT SIDE: Profile section
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(350, 0));

        // Load Profile section (shown when not logged in)
        loadProfilePanel = createLoadProfilePanel();
        leftPanel.add(loadProfilePanel);
        leftPanel.add(Box.createVerticalStrut(10));

        // Profile info (hidden until loaded)
        profileInfoPanel = new JPanel();
        profileInfoPanel.setLayout(new BoxLayout(profileInfoPanel, BoxLayout.Y_AXIS));
        profileInfoPanel.setBorder(BorderFactory.createTitledBorder("Your Profile"));
        profileInfoPanel.setVisible(false);
        leftPanel.add(profileInfoPanel);

        mainContentPanel.add(leftPanel, BorderLayout.WEST);

        // CENTER/RIGHT: Sessions section
        sessionsPanel = new JPanel();
        sessionsPanel.setLayout(new BoxLayout(sessionsPanel, BoxLayout.Y_AXIS));
        sessionsPanel.setBorder(BorderFactory.createTitledBorder("Previous Sessions"));
        sessionsPanel.setVisible(false);
        mainContentPanel.add(sessionsPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // Action buttons for logged-in users at bottom
        loggedInButtonsPanel = createLoggedInButtonsPanel();
        loggedInButtonsPanel.setVisible(false); // Hidden until logged in
        add(loggedInButtonsPanel, BorderLayout.SOUTH);
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

    private JPanel createLoggedInButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Start New Session button
        startNewSessionButton = new JButton("Start New Session");
        startNewSessionButton.setPreferredSize(new Dimension(180, 40));
        startNewSessionButton.setBackground(new Color(25, 118, 210));
        startNewSessionButton.setForeground(Color.BLACK);
        startNewSessionButton.setFont(new Font("Arial", Font.BOLD, 14));
        startNewSessionButton.addActionListener(e -> startNewSession());

        // Settings button
        JButton settingsButton = new JButton("Settings");
        settingsButton.setPreferredSize(new Dimension(120, 40));
        settingsButton.setBackground(new Color(76, 175, 80));
        settingsButton.setForeground(Color.BLACK);
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        settingsButton.addActionListener(e -> openSettings());

        // Refresh Data button
        refreshDataButton = new JButton("Refresh Data");
        refreshDataButton.setPreferredSize(new Dimension(150, 40));
        refreshDataButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshDataButton.addActionListener(e -> refreshData());

        // Sign Out button
        signOutButton = new JButton("Sign Out");
        signOutButton.setPreferredSize(new Dimension(120, 40));
        signOutButton.setBackground(new Color(211, 47, 47));
        signOutButton.setForeground(Color.BLACK);
        signOutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signOutButton.addActionListener(e -> signOut());

        panel.add(startNewSessionButton);
        panel.add(settingsButton);
        panel.add(refreshDataButton);
        panel.add(signOutButton);

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

        // Hide load profile section, show logged-in buttons
        loadProfilePanel.setVisible(false);
        loggedInButtonsPanel.setVisible(true);
    }

    /**
     * Load previous sessions from database.
     */
    private void loadPreviousSessions(UserID userId) {
        sessionsPanel.removeAll();

        // Query sessions from database
        ProfileContextStore store = conversationEngine.getProfileContextStore();
        if (store == null) {
            JLabel errorLabel = new JLabel("Unable to load sessions");
            errorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            sessionsPanel.add(errorLabel);
            sessionsPanel.setVisible(true);
            return;
        }

        java.util.List<Session> sessions = store.getSessionsByUser(userId);

        if (sessions.isEmpty()) {
            JLabel noSessionsLabel = new JLabel("No previous sessions found. Start your first session!");
            noSessionsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            noSessionsLabel.setForeground(Color.GRAY);
            sessionsPanel.add(noSessionsLabel);
        } else {
            // Display each session
            for (Session session : sessions) {
                JPanel sessionPanel = createSessionPanel(session);
                sessionsPanel.add(sessionPanel);
                sessionsPanel.add(Box.createVerticalStrut(5));
            }
        }

        sessionsPanel.setVisible(true);
        sessionsPanel.revalidate();
        sessionsPanel.repaint();
    }

    /**
     * Create a panel displaying session info.
     */
    private JPanel createSessionPanel(Session session) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setBackground(Color.WHITE);

        // Session ID (truncated)
        String sessionIdShort = session.getSessionId().getValue();
        if (sessionIdShort.length() > 20) {
            sessionIdShort = sessionIdShort.substring(0, 20) + "...";
        }

        // Format timestamp
        String timestamp = "Unknown";
        if (session.getCreatedAt() != null) {
            java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            timestamp = session.getCreatedAt()
                .atZone(java.time.ZoneId.systemDefault())
                .format(formatter);
        }

        JLabel sessionLabel = new JLabel(String.format(
            "📅 %s | 🔢 %d requests",
            timestamp,
            session.getRequestCount()
        ));
        sessionLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        panel.add(sessionLabel);

        return panel;
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

        // Start session for this user
        conversationEngine.startSession(currentUserId);

        // Switch to planning panel with saved preferences
        parentUI.switchToPlanningPanel(currentProfile.getPreferences());
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public UserID getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Refresh profile and session data from database.
     */
    private void refreshData() {
        if (currentUserId == null) {
            JOptionPane.showMessageDialog(this,
                "No user logged in",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("[MainMenu] Refreshing data for: " + currentUserId.getValue());

        // Reload profile from database
        Profile refreshedProfile = conversationEngine.loadProfile(currentUserId);
        if (refreshedProfile != null) {
            currentProfile = refreshedProfile;
            displayProfile(refreshedProfile);
        }

        // Reload sessions
        loadPreviousSessions(currentUserId);

        JOptionPane.showMessageDialog(this,
            "Data refreshed successfully!",
            "Refresh Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sign out current user.
     */
    private void signOut() {
        System.out.println("[MainMenu] Signing out user: " +
            (currentUserId != null ? currentUserId.getValue() : "unknown"));

        // Clear current user data
        currentProfile = null;
        currentUserId = null;

        // Save current session if any
        conversationEngine.saveCurrentSession();

        // Go back to sign in screen
        parentUI.switchToSignIn();
    }

    /**
     * Open settings page.
     */
    private void openSettings() {
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(this,
                "No profile loaded",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("[MainMenu] Opening settings for: " + currentUserId.getValue());
        parentUI.switchToSettings(currentProfile);
    }

    /**
     * Load profile directly (called when signing in from SignInPanel).
     */
    public void loadProfileDirectly(Profile profile) {
        if (profile == null) return;

        this.currentProfile = profile;
        this.currentUserId = profile.getUserId();

        displayProfile(profile);
        loadPreviousSessions(currentUserId);
    }
}
