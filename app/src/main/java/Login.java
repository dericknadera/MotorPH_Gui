package com.example.MotorPH.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Login extends JFrame implements ActionListener, KeyListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton clearButton;
    private JButton exitButton;
    private EmployeeCSVReader csvReader;
    private JLabel statusLabel;
    private int loginAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    public Login() {
        csvReader = new EmployeeCSVReader();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setWindowProperties();
    }

    private void initializeComponents() {
        // Create components
        JLabel titleLabel = new JLabel("MotorPH Employee System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBackground(new Color(255, 153, 0));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setBackground(new Color(204, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(Color.RED);

        // Add components to frame
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Center panel with form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passwordField, gbc);

        // Status label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(statusLabel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);

        // Instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBackground(new Color(240, 248, 255));
        instructionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(176, 196, 222)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel instructionsTitle = new JLabel("Sample Login Credentials:", SwingConstants.LEFT);
        instructionsTitle.setFont(new Font("Arial", Font.BOLD, 12));

        JTextArea instructionsText = new JTextArea(
                "Username: admin    Password: admin123\n" +
                        "Username: hr_user  Password: hr123\n" +
                        "Username: employee Password: emp123"
        );
        instructionsText.setFont(new Font("Courier", Font.PLAIN, 11));
        instructionsText.setBackground(new Color(240, 248, 255));
        instructionsText.setEditable(false);
        instructionsText.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        instructionsPanel.add(instructionsTitle, BorderLayout.NORTH);
        instructionsPanel.add(instructionsText, BorderLayout.CENTER);

        // Add all panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(instructionsPanel, BorderLayout.SOUTH);
    }

    private void setupLayout() {
        // Layout is already set up in initializeComponents
    }

    private void setupEventListeners() {
        loginButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        usernameField.addKeyListener(this);
        passwordField.addKeyListener(this);

        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }

    private void setWindowProperties() {
        setTitle("MotorPH Employee System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // Center on screen

        // Set minimum size
        Dimension minSize = new Dimension(500, 400);
        setMinimumSize(minSize);

        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            performLogin();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == exitButton) {
            exitApplication();
        }
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password.", Color.RED);
            return;
        }

        // Disable login button temporarily
        loginButton.setEnabled(false);
        showStatus("Authenticating...", Color.BLUE);

        // Simulate authentication delay
        Timer timer = new Timer(1000, e -> {
            boolean isValid = csvReader.validateUser(username, password);

            if (isValid) {
                showStatus("Login successful! Loading main dashboard...", Color.GREEN);

                // Open main dashboard after a short delay
                Timer openTimer = new Timer(1500, event -> {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        new MainDashboard().setVisible(true);
                    });
                });
                openTimer.setRepeats(false);
                openTimer.start();

            } else {
                loginAttempts++;
                if (loginAttempts >= MAX_ATTEMPTS) {
                    showStatus("Maximum login attempts exceeded. Application will close.", Color.RED);
                    Timer exitTimer = new Timer(2000, event -> System.exit(0));
                    exitTimer.setRepeats(false);
                    exitTimer.start();
                } else {
                    showStatus(String.format("Invalid credentials. Attempt %d of %d.",
                            loginAttempts, MAX_ATTEMPTS), Color.RED);
                    clearFields();
                    loginButton.setEnabled(true);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText(" ");
        usernameField.requestFocus();
    }

    private void exitApplication() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            performLogin();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            clearFields();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Clear status when user starts typing
        if (statusLabel.getForeground() == Color.RED) {
            statusLabel.setText(" ");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}