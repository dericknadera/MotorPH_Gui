package com.example.MotorPH.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UpdateEmployeeForm extends JDialog implements ActionListener {
    private MainDashboard parentWindow;
    private EmployeeCSVReader csvReader;
    private String employeeNumber;
    private String[] originalEmployeeData;

    // Form fields
    private JTextField empNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField birthdayField;
    private JTextArea addressArea;
    private JTextField phoneField;
    private JTextField sssField;
    private JTextField philHealthField;
    private JTextField tinField;
    private JTextField pagIbigField;
    private JComboBox<String> statusCombo;
    private JTextField positionField;
    private JTextField supervisorField;
    private JTextField basicSalaryField;
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;

    // Buttons
    private JButton updateButton;
    private JButton cancelButton;
    private JButton resetButton;

    // Labels to show original values
    private JLabel originalBasicSalaryLabel;
    private JLabel originalPositionLabel;

    public UpdateEmployeeForm(MainDashboard parent, String empNumber) {
        super(parent, "Update Employee - #" + empNumber, true); // Modal dialog
        this.parentWindow = parent;
        this.employeeNumber = empNumber;
        this.csvReader = new EmployeeCSVReader();

        loadOriginalEmployeeData();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setWindowProperties();
        loadEmployeeDataIntoForm();
    }

    private void loadOriginalEmployeeData() {
        originalEmployeeData = csvReader.findEmployee(employeeNumber);
        if (originalEmployeeData == null) {
            JOptionPane.showMessageDialog(this,
                    "Employee #" + employeeNumber + " not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initializeComponents() {
        // Create form fields
        empNumberField = createTextField(false); // Always read-only for updates
        empNumberField.setText(employeeNumber);

        lastNameField = createTextField(true);
        firstNameField = createTextField(true);
        birthdayField = createTextField(true);

        addressArea = new JTextArea(3, 20);
        addressArea.setFont(new Font("Arial", Font.PLAIN, 12));
        addressArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        phoneField = createTextField(true);
        sssField = createTextField(true);
        philHealthField = createTextField(true);
        tinField = createTextField(true);
        pagIbigField = createTextField(true);

        statusCombo = new JComboBox<>(new String[]{"Regular", "Contractual", "Probationary", "Part-time"});
        statusCombo.setFont(new Font("Arial", Font.PLAIN, 12));

        positionField = createTextField(true);
        supervisorField = createTextField(true);
        basicSalaryField = createTextField(true);
        riceSubsidyField = createTextField(true);
        phoneAllowanceField = createTextField(true);
        clothingAllowanceField = createTextField(true);

        // Create buttons
        updateButton = createButton("Update Employee", new Color(255, 193, 7));
        cancelButton = createButton("Cancel", new Color(108, 117, 125));
        resetButton = createButton("Reset to Original", new Color(0, 123, 255));

        // Original value labels (for comparison)
        originalBasicSalaryLabel = new JLabel();
        originalBasicSalaryLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        originalBasicSalaryLabel.setForeground(Color.GRAY);

        originalPositionLabel = new JLabel();
        originalPositionLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        originalPositionLabel.setForeground(Color.GRAY);
    }

    private JTextField createTextField(boolean editable) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setEditable(editable);
        if (!editable) {
            field.setBackground(new Color(248, 249, 250));
        }
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Add change highlighting for editable fields
        if (editable) {
            field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) { highlightChangedField(field); }
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) { highlightChangedField(field); }
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) { highlightChangedField(field); }
            });
        }

        return field;
    }

    private void highlightChangedField(JTextField field) {
        // Highlight fields that have been modified
        field.setBackground(new Color(255, 255, 204)); // Light yellow
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 193, 7));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Update Employee #" + employeeNumber);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel instructionLabel = new JLabel("Modify the fields below and click 'Update Employee' to save changes");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.WHITE);

        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(255, 193, 7));
        headerContent.add(titleLabel, BorderLayout.NORTH);
        headerContent.add(instructionLabel, BorderLayout.SOUTH);

        headerPanel.add(headerContent, BorderLayout.CENTER);

        // Main form panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee Number (read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Employee Number:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(empNumberField, gbc);

        // Name fields
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Last Name: *"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("First Name: *"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(firstNameField, gbc);

        // Birthday and Phone
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Birthday (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(birthdayField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Phone Number:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(phoneField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(addressArea), gbc);

        // Government numbers
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("SSS Number:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(sssField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("PhilHealth Number:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(philHealthField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("TIN:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tinField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Pag-IBIG Number:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(pagIbigField, gbc);

        // Status and Position with original value display
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(statusCombo, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.add(createLabel("Position:"), BorderLayout.NORTH);
        positionPanel.add(originalPositionLabel, BorderLayout.SOUTH);
        formPanel.add(positionPanel, gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(positionField, gbc);

        // Supervisor and Basic Salary with original value display
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Immediate Supervisor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(supervisorField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        JPanel salaryPanel = new JPanel(new BorderLayout());
        salaryPanel.add(createLabel("Basic Salary: *"), BorderLayout.NORTH);
        salaryPanel.add(originalBasicSalaryLabel, BorderLayout.SOUTH);
        formPanel.add(salaryPanel, gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(basicSalaryField, gbc);

        // Allowances
        gbc.gridx = 0; gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Rice Subsidy:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(riceSubsidyField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Phone Allowance:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(phoneAllowanceField, gbc);

        gbc.gridx = 0; gbc.gridy = 9; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Clothing Allowance:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(clothingAllowanceField, gbc);

        // Required fields note
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE;
        JLabel requiredNote = new JLabel("* Required fields");
        requiredNote.setFont(new Font("Arial", Font.ITALIC, 11));
        requiredNote.setForeground(Color.RED);
        formPanel.add(requiredNote, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.add(updateButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        if (text.contains("*")) {
            label.setForeground(new Color(139, 0, 0)); // Dark red for required fields
        }
        return label;
    }

    private void setupEventListeners() {
        updateButton.addActionListener(this);
        cancelButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Add validation listeners for numeric fields
        basicSalaryField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                validateNumericField(basicSalaryField, "Basic Salary");
            }
        });

        riceSubsidyField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                validateNumericField(riceSubsidyField, "Rice Subsidy");
            }
        });

        phoneAllowanceField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                validateNumericField(phoneAllowanceField, "Phone Allowance");
            }
        });

        clothingAllowanceField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                validateNumericField(clothingAllowanceField, "Clothing Allowance");
            }
        });
    }

    private void validateNumericField(JTextField field, String fieldName) {
        String text = field.getText().trim();
        if (!text.isEmpty()) {
            try {
                double value = Double.parseDouble(text);
                if (value < 0) {
                    field.setBackground(new Color(255, 230, 230));
                    showValidationWarning(fieldName + " cannot be negative!");
                } else {
                    field.setBackground(new Color(255, 255, 204)); // Light yellow for modified
                }
            } catch (NumberFormatException e) {
                field.setBackground(new Color(255, 230, 230));
                showValidationWarning(fieldName + " must be a valid number!");
            }
        } else {
            field.setBackground(Color.WHITE);
        }
    }

    private void showValidationWarning(String message) {
        JLabel warningLabel = new JLabel(message);
        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("Arial", Font.ITALIC, 11));

        JOptionPane optionPane = new JOptionPane(warningLabel, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Validation Warning");

        // Auto-close after 3 seconds
        Timer timer = new Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    private void setWindowProperties() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(parentWindow);

        // Set minimum size
        setMinimumSize(new Dimension(700, 600));

        // Focus on first editable field
        SwingUtilities.invokeLater(() -> lastNameField.requestFocus());
    }

    private void loadEmployeeDataIntoForm() {
        if (originalEmployeeData != null && originalEmployeeData.length >= 19) {
            lastNameField.setText(originalEmployeeData[1]);
            firstNameField.setText(originalEmployeeData[2]);
            birthdayField.setText(originalEmployeeData[3]);
            addressArea.setText(originalEmployeeData[4]);
            phoneField.setText(originalEmployeeData[5]);
            sssField.setText(originalEmployeeData[6]);
            philHealthField.setText(originalEmployeeData[7]);
            tinField.setText(originalEmployeeData[8]);
            pagIbigField.setText(originalEmployeeData[9]);
            statusCombo.setSelectedItem(originalEmployeeData[10]);
            positionField.setText(originalEmployeeData[11]);
            supervisorField.setText(originalEmployeeData[12]);
            basicSalaryField.setText(originalEmployeeData[13]);
            riceSubsidyField.setText(originalEmployeeData[14]);
            phoneAllowanceField.setText(originalEmployeeData[15]);
            clothingAllowanceField.setText(originalEmployeeData[16]);

            // Set original value labels
            originalBasicSalaryLabel.setText("Original: ₱" + originalEmployeeData[13]);
            originalPositionLabel.setText("Original: " + originalEmployeeData[11]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            updateEmployee();
        } else if (e.getSource() == cancelButton) {
            confirmCancel();
        } else if (e.getSource() == resetButton) {
            resetToOriginal();
        }
    }

    private void updateEmployee() {
        try {
            // Validate required fields
            validateEmployeeData();

            // Check if any changes were made
            if (!hasChanges()) {
                JOptionPane.showMessageDialog(this,
                        "No changes detected. Please modify some fields before updating.",
                        "No Changes", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Confirm update
            int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to update employee #" + employeeNumber + "?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            // Create updated employee data array
            String[] updatedData = createUpdatedEmployeeDataArray();

            // Update in CSV
            if (csvReader.updateEmployee(employeeNumber, updatedData)) {
                JOptionPane.showMessageDialog(this,
                        "Employee #" + employeeNumber + " updated successfully!",
                        "Update Successful", JOptionPane.INFORMATION_MESSAGE);

                // Refresh parent window
                parentWindow.refreshEmployeeTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update employee #" + employeeNumber + "!",
                        "Update Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Validation Error: " + ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasChanges() {
        if (originalEmployeeData == null) return true;

        return !lastNameField.getText().trim().equals(originalEmployeeData[1]) ||
                !firstNameField.getText().trim().equals(originalEmployeeData[2]) ||
                !birthdayField.getText().trim().equals(originalEmployeeData[3]) ||
                !addressArea.getText().trim().equals(originalEmployeeData[4]) ||
                !phoneField.getText().trim().equals(originalEmployeeData[5]) ||
                !sssField.getText().trim().equals(originalEmployeeData[6]) ||
                !philHealthField.getText().trim().equals(originalEmployeeData[7]) ||
                !tinField.getText().trim().equals(originalEmployeeData[8]) ||
                !pagIbigField.getText().trim().equals(originalEmployeeData[9]) ||
                !statusCombo.getSelectedItem().toString().equals(originalEmployeeData[10]) ||
                !positionField.getText().trim().equals(originalEmployeeData[11]) ||
                !supervisorField.getText().trim().equals(originalEmployeeData[12]) ||
                !basicSalaryField.getText().trim().equals(originalEmployeeData[13]) ||
                !riceSubsidyField.getText().trim().equals(originalEmployeeData[14]) ||
                !phoneAllowanceField.getText().trim().equals(originalEmployeeData[15]) ||
                !clothingAllowanceField.getText().trim().equals(originalEmployeeData[16]);
    }

    private void validateEmployeeData() throws IllegalArgumentException {
        if (lastNameField.getText().trim().isEmpty()) {
            lastNameField.requestFocus();
            throw new IllegalArgumentException("Last name is required");
        }
        if (firstNameField.getText().trim().isEmpty()) {
            firstNameField.requestFocus();
            throw new IllegalArgumentException("First name is required");
        }

        // Validate birthday format
        String birthday = birthdayField.getText().trim();
        if (!birthday.isEmpty()) {
            try {
                LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                birthdayField.requestFocus();
                throw new IllegalArgumentException("Birthday must be in YYYY-MM-DD format");
            }
        }

        // Validate numeric fields
        validateNumericValue(basicSalaryField.getText().trim(), "Basic Salary");
        validateNumericValue(riceSubsidyField.getText().trim(), "Rice Subsidy");
        validateNumericValue(phoneAllowanceField.getText().trim(), "Phone Allowance");
        validateNumericValue(clothingAllowanceField.getText().trim(), "Clothing Allowance");
    }

    private void validateNumericValue(String value, String fieldName) throws IllegalArgumentException {
        if (!value.isEmpty()) {
            try {
                double numValue = Double.parseDouble(value);
                if (numValue < 0) {
                    throw new IllegalArgumentException(fieldName + " cannot be negative");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(fieldName + " must be a valid number");
            }
        }
    }

    private String[] createUpdatedEmployeeDataArray() {
        // Calculate derived values
        double basicSalary = parseDouble(basicSalaryField.getText().trim(), 0);
        double grossSemiMonthly = basicSalary / 2;
        double hourlyRate = basicSalary / (8 * 22); // 8 hours/day, 22 days/month

        return new String[] {
                employeeNumber, // Keep original employee number
                lastNameField.getText().trim(),
                firstNameField.getText().trim(),
                birthdayField.getText().trim(),
                addressArea.getText().trim(),
                phoneField.getText().trim(),
                sssField.getText().trim(),
                philHealthField.getText().trim(),
                tinField.getText().trim(),
                pagIbigField.getText().trim(),
                (String) statusCombo.getSelectedItem(),
                positionField.getText().trim(),
                supervisorField.getText().trim(),
                basicSalaryField.getText().trim(),
                riceSubsidyField.getText().trim(),
                phoneAllowanceField.getText().trim(),
                clothingAllowanceField.getText().trim(),
                String.valueOf(grossSemiMonthly),
                String.valueOf(hourlyRate)
        };
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void confirmCancel() {
        if (hasChanges()) {
            int result = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Are you sure you want to cancel?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    private void resetToOriginal() {
        int result = JOptionPane.showConfirmDialog(this,
                "Reset all fields to original values?",
                "Reset to Original",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            loadEmployeeDataIntoForm();

            // Reset field backgrounds
            Component[] components = getFormComponents(this);
            for (Component comp : components) {
                if (comp instanceof JTextField && ((JTextField) comp).isEditable()) {
                    comp.setBackground(Color.WHITE);
                }
            }
        }
    }

    private Component[] getFormComponents(Container container) {
        java.util.List<Component> components = new java.util.ArrayList<>();
        for (Component comp : container.getComponents()) {
            components.add(comp);
            if (comp instanceof Container) {
                java.util.Collections.addAll(components, getFormComponents((Container) comp));
            }
        }
        return components.toArray(new Component[0]);
    }
}


