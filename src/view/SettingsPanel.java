package view;

import controller.*;
import domain.*;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Settings panel for editing user profile.
 *
 * @author CPS731 Team 20
 */
public class SettingsPanel extends JPanel {

    private ConversationEngine conversationEngine;
    private MobileAppUI parentUI;
    private Profile currentProfile;

    // UI Components
    private JTextField interestsField;
    private JComboBox<String> budgetCombo;
    private JTextField radiusField;
    private JComboBox<String> transportCombo;
    private JCheckBox accessibilityCheckbox;

    public SettingsPanel(MobileAppUI parent, ConversationEngine engine) {
        this.parentUI = parent;
        this.conversationEngine = engine;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> parentUI.switchToMainMenu());
        headerPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Interests
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Interests (comma separated):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        interestsField = new JTextField(30);
        formPanel.add(interestsField, gbc);

        row++;

        // Budget
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Budget:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        budgetCombo = new JComboBox<>(new String[]{"low", "medium", "high"});
        formPanel.add(budgetCombo, gbc);

        row++;

        // Radius
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Radius (meters):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        radiusField = new JTextField(10);
        formPanel.add(radiusField, gbc);

        row++;

        // Transport
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Transport Mode:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        transportCombo = new JComboBox<>(new String[]{"walking", "driving", "transit"});
        formPanel.add(transportCombo, gbc);

        row++;

        // Accessibility
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Accessibility Needs:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        accessibilityCheckbox = new JCheckBox();
        formPanel.add(accessibilityCheckbox, gbc);

        row++;

        // Save button
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);

        JButton saveButton = new JButton("Save Changes");
        saveButton.setPreferredSize(new Dimension(200, 40));
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(e -> saveSettings());
        formPanel.add(saveButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Load profile into form fields.
     */
    public void loadProfile(Profile profile) {
        if (profile == null) return;

        this.currentProfile = profile;
        Preferences prefs = profile.getPreferences();

        // Set form values
        if (prefs.getInterests() != null) {
            interestsField.setText(String.join(", ", prefs.getInterests()));
        }

        if (prefs.getBudget() != null) {
            budgetCombo.setSelectedItem(prefs.getBudget());
        }

        radiusField.setText(String.valueOf(prefs.getRadius()));

        if (prefs.getTransportMode() != null) {
            transportCombo.setSelectedItem(prefs.getTransportMode());
        }

        accessibilityCheckbox.setSelected(prefs.isAccessibilityNeeds());
    }

    /**
     * Save updated settings to database.
     */
    private void saveSettings() {
        if (currentProfile == null) {
            JOptionPane.showMessageDialog(this,
                "No profile loaded",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update preferences from form
        Preferences prefs = currentProfile.getPreferences();

        // Parse interests
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
            JOptionPane.showMessageDialog(this,
                "Invalid radius value",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Transport
        prefs.setTransportMode((String) transportCombo.getSelectedItem());

        // Accessibility
        prefs.setAccessibilityNeeds(accessibilityCheckbox.isSelected());

        // Save to database
        conversationEngine.saveProfile(currentProfile);

        JOptionPane.showMessageDialog(this,
            "Settings saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);

        // Go back to main menu
        parentUI.switchToMainMenuAfterSettings();
    }
}
