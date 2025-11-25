package view;

import controller.*;
import domain.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Sign In/Sign Up panel.
 * Simple username-only authentication for demo.
 *
 * @author CPS731 Team 20
 */
public class SignInPanel extends JPanel {

    private ConversationEngine conversationEngine;
    private MobileAppUI parentUI;

    // UI Components
    private JTabbedPane tabbedPane;
    private JTextField signInUsernameField;
    private JTextField signUpUsernameField;
    private JTextField signUpInterestsField;
    private JComboBox<String> signUpBudgetCombo;
    private JTextField signUpRadiusField;
    private JComboBox<String> signUpTransportCombo;

    public SignInPanel(MobileAppUI parent, ConversationEngine engine) {
        this.parentUI = parent;
        this.conversationEngine = engine;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Center panel with tabbed pane
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Travel Assistant!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(welcomeLabel, gbc);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(500, 400));

        // Sign In tab
        JPanel signInTab = createSignInTab();
        tabbedPane.addTab("Sign In", signInTab);

        // Sign Up tab
        JPanel signUpTab = createSignUpTab();
        tabbedPane.addTab("Sign Up", signUpTab);

        gbc.gridy = 1;
        centerPanel.add(tabbedPane, gbc);

        // Guest button at bottom
        JButton guestButton = new JButton("Continue as Guest");
        guestButton.setPreferredSize(new Dimension(200, 35));
        guestButton.addActionListener(e -> continueAsGuest());
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        centerPanel.add(guestButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Create Sign In tab.
     */
    private JPanel createSignInTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(usernameLabel);
        panel.add(Box.createVerticalStrut(10));

        signInUsernameField = new JTextField(20);
        signInUsernameField.setMaximumSize(new Dimension(300, 30));
        signInUsernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(signInUsernameField);
        panel.add(Box.createVerticalStrut(20));

        // Sign In button
        JButton signInButton = new JButton("Sign In");
        signInButton.setPreferredSize(new Dimension(150, 40));
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setBackground(new Color(25, 118, 210));
        signInButton.setForeground(Color.BLACK);
        signInButton.setFont(new Font("Arial", Font.BOLD, 14));
        signInButton.addActionListener(e -> handleSignIn());
        panel.add(signInButton);

        panel.add(Box.createVerticalStrut(20));

        // Info label
        JLabel infoLabel = new JLabel("<html><center>Existing users:<br/>alice, bob, test_user_001, user_hussein</center></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infoLabel);

        return panel;
    }

    /**
     * Create Sign Up tab.
     */
    private JPanel createSignUpTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Username
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        signUpUsernameField = new JTextField(20);
        panel.add(signUpUsernameField, gbc);

        row++;

        // Interests
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Interests:"), gbc);

        gbc.gridx = 1;
        signUpInterestsField = new JTextField("restaurants, museums");
        panel.add(signUpInterestsField, gbc);

        row++;

        // Budget
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Budget:"), gbc);

        gbc.gridx = 1;
        signUpBudgetCombo = new JComboBox<>(new String[]{"low", "medium", "high"});
        signUpBudgetCombo.setSelectedIndex(1);
        panel.add(signUpBudgetCombo, gbc);

        row++;

        // Radius
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Radius (m):"), gbc);

        gbc.gridx = 1;
        signUpRadiusField = new JTextField("5000");
        panel.add(signUpRadiusField, gbc);

        row++;

        // Transport
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel("Transport:"), gbc);

        gbc.gridx = 1;
        signUpTransportCombo = new JComboBox<>(new String[]{"walking", "driving", "transit"});
        panel.add(signUpTransportCombo, gbc);

        row++;

        // Sign Up button
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(150, 40));
        signUpButton.setBackground(new Color(76, 175, 80));
        signUpButton.setForeground(Color.BLACK);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.addActionListener(e -> handleSignUp());
        panel.add(signUpButton, gbc);

        return panel;
    }

    /**
     * Handle Sign In.
     */
    private void handleSignIn() {
        String username = signInUsernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a username",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("[SignIn] Signing in user: " + username);

        // Try to load profile
        UserID userId = new UserID(username);
        Profile profile = conversationEngine.loadProfile(userId);

        if (profile == null) {
            // Profile doesn't exist
            int response = JOptionPane.showConfirmDialog(this,
                "User '" + username + "' not found. Would you like to sign up instead?",
                "User Not Found",
                JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // Switch to sign up tab and pre-fill username
                tabbedPane.setSelectedIndex(1);
                signUpUsernameField.setText(username);
            }
        } else {
            // Profile loaded successfully - go to main menu
            JOptionPane.showMessageDialog(this,
                "Welcome back, " + username + "!",
                "Sign In Successful",
                JOptionPane.INFORMATION_MESSAGE);

            parentUI.switchToMainMenuWithProfile(profile);
        }
    }

    /**
     * Handle Sign Up.
     */
    private void handleSignUp() {
        String username = signUpUsernameField.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a username",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("[SignUp] Creating new user: " + username);

        // Check if user already exists
        UserID userId = new UserID(username);
        Profile existingProfile = conversationEngine.loadProfile(userId);

        if (existingProfile != null) {
            JOptionPane.showMessageDialog(this,
                "Username '" + username + "' already exists. Please sign in instead.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new profile
        Profile newProfile = new Profile();
        newProfile.setUserId(userId);

        // Capture preferences from form
        Preferences prefs = new Preferences();

        // Parse interests
        String interestsText = signUpInterestsField.getText().trim();
        if (!interestsText.isEmpty()) {
            String[] interestsArray = interestsText.split(",");
            List<String> interests = new ArrayList<>();
            for (String interest : interestsArray) {
                interests.add(interest.trim());
            }
            prefs.setInterests(interests);
        } else {
            prefs.setInterests(Arrays.asList("restaurants"));
        }

        prefs.setBudget((String) signUpBudgetCombo.getSelectedItem());

        try {
            int radius = Integer.parseInt(signUpRadiusField.getText().trim());
            prefs.setRadius(radius);
        } catch (NumberFormatException e) {
            prefs.setRadius(5000);
        }

        prefs.setTransportMode((String) signUpTransportCombo.getSelectedItem());
        prefs.setAccessibilityNeeds(false);

        newProfile.setPreferences(prefs);

        // Save to database
        conversationEngine.saveProfile(newProfile);

        // Success!
        JOptionPane.showMessageDialog(this,
            "Account created successfully! Welcome, " + username + "!",
            "Sign Up Successful",
            JOptionPane.INFORMATION_MESSAGE);

        // Go to main menu
        parentUI.switchToMainMenuWithProfile(newProfile);
    }

    /**
     * Continue as guest.
     */
    private void continueAsGuest() {
        System.out.println("[SignIn] Continuing as guest");

        // Start guest session (null userId)
        conversationEngine.startSession(null);

        // Create default preferences
        Preferences prefs = new Preferences();
        prefs.setInterests(Arrays.asList("restaurants"));
        prefs.setBudget("medium");
        prefs.setRadius(5000);
        prefs.setTransportMode("walking");
        prefs.setAccessibilityNeeds(false);

        // Go straight to planning
        parentUI.switchToPlanningPanel(prefs);
    }
}
