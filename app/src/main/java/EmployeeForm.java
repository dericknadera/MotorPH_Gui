package com.example.MotorPH.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeForm extends JDialog implements ActionListener {
    private MainDashboard parentWindow;
    private EmployeeCSVReader csvReader;
    private String employeeNumber;
    private boolean isEditMode;

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
    private JButton saveButton;
    private JButton cancelButton;
    private JButton clearButton;

    public EmployeeForm(MainDashboard parent, String empNumber) {
        super(parent, true); // Modal dialog
        this.parentWindow = parent;
        this.employeeNumber = empNumber;
        this.isEditMode = (empNumber != null);
        this.csvReader = new EmployeeCSVReader();

        initializeComponents();
        setupLayout();
        setupEventListeners();
        setWindowProperties();

        if (isEditMode) {
            loadEmployeeData();
        } else {
            generateEmployeeNumber();
        }
    }

    private void initializeComponents() {
        // Create form fields
        empNumberField = createTextField(false); // Always read-only
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

        // Set default values
        riceSubsidyField.setText("1500");
        phoneAllowanceField.setText("2000");
        clothingAllowanceField.setText("1000");

        // Create buttons
        saveButton = createButton("Save", new Color(40, 167, 69));
        cancelButton = createButton("Cancel", new Color(108, 117, 125));
        clearButton = createButton("Clear", new Color(255, 193, 7));
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
        return field;
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
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        String title = isEditMode ? "Edit Employee" : "Add New Employee";
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Main form panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Employee Number:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(empNumberField, gbc);

        // Name fields
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("First Name:"), gbc);
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

        // Status and Position
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(statusCombo, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Position:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(positionField, gbc);

        // Supervisor and Basic Salary
        gbc.gridx = 0; gbc.gridy = 7; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Immediate Supervisor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(supervisorField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(createLabel("Basic Salary:"), gbc);
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

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private void setupEventListeners() {
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
        clearButton.addActionListener(this);

        // Add input validation listeners
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
                    JOptionPane.showMessageDialog(this, fieldName + " cannot be negative!",
                            "Invalid Input", JOptionPane.WARNING_MESSAGE);
                } else {
                    field.setBackground(Color.WHITE);
                }
            } catch (NumberFormatException e) {
                field.setBackground(new Color(255, 230, 230));
                JOptionPane.showMessageDialog(this, fieldName + " must be a valid number!",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            field.setBackground(Color.WHITE);
        }
    }

    private void setWindowProperties() {
        String title = isEditMode ? "Edit Employee" : "Add New Employee";
        setTitle(title);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(parentWindow);

        // Focus on first name field
        SwingUtilities.invokeLater(() -> lastNameField.requestFocus());
    }

    private void generateEmployeeNumber() {
        String nextNumber = csvReader.generateNextEmployeeNumber();
        empNumberField.setText(nextNumber);
    }

    private void loadEmployeeData() {
        String[] employeeData = csvReader.findEmployee(employeeNumber);
        if (employeeData != null && employeeData.length >= 19) {
            empNumberField.setText(employeeData[0]);
            lastNameField.setText(employeeData[1]);
            firstNameField.setText(employeeData[2]);
            birthdayField.setText(employeeData[3]);
            addressArea.setText(employeeData[4]);
            phoneField.setText(employeeData[5]);
            sssField.setText(employeeData[6]);
            philHealthField.setText(employeeData[7]);
            tinField.setText(employeeData[8]);
            pagIbigField.setText(employeeData[9]);
            statusCombo.setSelectedItem(employeeData[10]);
            positionField.setText(employeeData[11]);
            supervisorField.setText(employeeData[12]);
            basicSalaryField.setText(employeeData[13]);
            riceSubsidyField.setText(employeeData[14]);
            phoneAllowanceField.setText(employeeData[15]);
            clothingAllowanceField.setText(employeeData[16]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            saveEmployee();
        } else if (e.getSource() == cancelButton) {
            dispose();
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    private void saveEmployee() {
        try {
            // Validate required fields
            validateEmployeeData();

            // Create employee data array
            String[] employeeData = createEmployeeDataArray();

            boolean success;
            if (isEditMode) {
                success = csvReader.updateEmployee(employeeNumber, employeeData);
            } else {
                success = csvReader.addEmployee(employeeData);
            }

            if (success) {
                String message = isEditMode ? "Employee updated successfully!" : "Employee added successfully!";
                JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh parent window
                parentWindow.refreshEmployeeTable();
                dispose();
            } else {
                String message = isEditMode ? "Failed to update employee!" : "Failed to add employee!";
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: " + ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
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

    private String[] createEmployeeDataArray() {
        // Calculate derived values
        double basicSalary = parseDouble(basicSalaryField.getText().trim(), 0);
        double grossSemiMonthly = basicSalary / 2;
        double hourlyRate = basicSalary / (8 * 22); // 8 hours/day, 22 days/month

        return new String[] {
                empNumberField.getText().trim(),
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

    private void clearForm() {
        lastNameField.setText("");
        firstNameField.setText("");
        birthdayField.setText("");
        addressArea.setText("");
        phoneField.setText("");
        sssField.setText("");
        philHealthField.setText("");
        tinField.setText("");
        pagIbigField.setText("");
        statusCombo.setSelectedIndex(0);
        positionField.setText("");
        supervisorField.setText("");
        basicSalaryField.setText("");
        riceSubsidyField.setText("1500");
        phoneAllowanceField.setText("2000");
        clothingAllowanceField.setText("1000");

        if (!isEditMode) {
            generateEmployeeNumber();
        }

        lastNameField.requestFocus();
    }
}


