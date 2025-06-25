package com.example.MotorPH.Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class EmployeeDetails extends JFrame implements ActionListener {
    private EmployeeCSVReader csvReader;
    private EmployeeDataBase employee;
    private EmployeeDataBase.PayrollCalculation payrollCalc;
    private String employeeNumber;

    // Employee detail components
    private JLabel empNumberLabel;
    private JLabel nameLabel;
    private JLabel birthdayLabel;
    private JLabel addressLabel;
    private JLabel phoneLabel;
    private JLabel sssLabel;
    private JLabel philHealthLabel;
    private JLabel tinLabel;
    private JLabel pagIbigLabel;
    private JLabel statusLabel;
    private JLabel positionLabel;
    private JLabel supervisorLabel;
    private JLabel basicSalaryLabel;

    // Payroll components
    private JComboBox<String> monthCombo;
    private JComboBox<String> yearCombo;
    private JButton computeButton;
    private JButton closeButton;

    // Payroll display labels
    private JLabel grossPayLabel;
    private JLabel sssContribLabel;
    private JLabel philHealthContribLabel;
    private JLabel pagIbigContribLabel;
    private JLabel withholdingTaxLabel;
    private JLabel totalDeductionsLabel;
    private JLabel netPayLabel;

    private JPanel payrollPanel;
    private DecimalFormat currencyFormat;

    public EmployeeDetails(String employeeNumber) {
        this.employeeNumber = employeeNumber;
        this.csvReader = new EmployeeCSVReader();
        this.currencyFormat = new DecimalFormat("₱#,##0.00");

        loadEmployeeData();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setWindowProperties();
    }

    private void loadEmployeeData() {
        String[] employeeData = csvReader.findEmployee(employeeNumber);
        if (employeeData != null) {
            employee = new EmployeeDataBase(employeeData);
        } else {
            JOptionPane.showMessageDialog(null, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initializeComponents() {
        // Employee detail labels
        empNumberLabel = createValueLabel(employee.getEmployeeNumber());
        nameLabel = createValueLabel(employee.getFullName());
        birthdayLabel = createValueLabel(employee.getBirthday());
        addressLabel = createValueLabel(employee.getAddress());
        phoneLabel = createValueLabel(employee.getPhoneNumber());
        sssLabel = createValueLabel(employee.getSssNumber());
        philHealthLabel = createValueLabel(employee.getPhilHealthNumber());
        tinLabel = createValueLabel(employee.getTin());
        pagIbigLabel = createValueLabel(employee.getPagIbigNumber());
        statusLabel = createValueLabel(employee.getStatus());
        positionLabel = createValueLabel(employee.getPosition());
        supervisorLabel = createValueLabel(employee.getImmediateSupervisor());
        basicSalaryLabel = createValueLabel(currencyFormat.format(employee.getBasicSalary()));

        // Month and year selection
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        monthCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        // Generate year options (current year ± 2 years)
        int currentYear = LocalDate.now().getYear();
        String[] years = new String[5];
        for (int i = 0; i < 5; i++) {
            years[i] = String.valueOf(currentYear - 2 + i);
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        yearCombo.setSelectedItem(String.valueOf(currentYear));

        // Buttons
        computeButton = createButton("Compute Salary", new Color(0, 123, 255));
        closeButton = createButton("Close", new Color(108, 117, 125));

        // Payroll display labels (initially empty)
        grossPayLabel = createValueLabel("");
        sssContribLabel = createValueLabel("");
        philHealthContribLabel = createValueLabel("");
        pagIbigContribLabel = createValueLabel("");
        withholdingTaxLabel = createValueLabel("");
        totalDeductionsLabel = createValueLabel("");
        netPayLabel = createValueLabel("");
        netPayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        netPayLabel.setForeground(new Color(0, 128, 0));
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(51, 51, 51));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(new Color(85, 85, 85));
        return label;
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

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Employee Details & Payroll");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Employee details panel
        JPanel detailsPanel = createEmployeeDetailsPanel();

        // Payroll panel
        payrollPanel = createPayrollPanel();

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, detailsPanel, payrollPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createEmployeeDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Employee Information"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Employee basic info
        addDetailRow(fieldsPanel, gbc, 0, "Employee Number:", empNumberLabel);
        addDetailRow(fieldsPanel, gbc, 1, "Full Name:", nameLabel);
        addDetailRow(fieldsPanel, gbc, 2, "Birthday:", birthdayLabel);
        addDetailRow(fieldsPanel, gbc, 3, "Phone Number:", phoneLabel);

        // Address (spans multiple columns)
        gbc.gridx = 0; gbc.gridy = 4;
        fieldsPanel.add(createFieldLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(addressLabel, gbc);

        // Government numbers
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        addDetailRow(fieldsPanel, gbc, 5, "SSS Number:", sssLabel);
        addDetailRow(fieldsPanel, gbc, 6, "PhilHealth Number:", philHealthLabel);
        addDetailRow(fieldsPanel, gbc, 7, "TIN:", tinLabel);
        addDetailRow(fieldsPanel, gbc, 8, "Pag-IBIG Number:", pagIbigLabel);

        // Employment details
        addDetailRow(fieldsPanel, gbc, 9, "Status:", statusLabel);
        addDetailRow(fieldsPanel, gbc, 10, "Position:", positionLabel);
        addDetailRow(fieldsPanel, gbc, 11, "Supervisor:", supervisorLabel);
        addDetailRow(fieldsPanel, gbc, 12, "Basic Salary:", basicSalaryLabel);

        panel.add(fieldsPanel, BorderLayout.CENTER);
        return panel;
    }

    private void addDetailRow(JPanel parent, GridBagConstraints gbc, int row, String label, JLabel value) {
        gbc.gridx = 0; gbc.gridy = row;
        parent.add(createFieldLabel(label), gbc);
        gbc.gridx = 1;
        parent.add(value, gbc);
    }

    private JPanel createPayrollPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "Payroll Computation"));

        // Month/Year selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Select Month:"));
        selectionPanel.add(monthCombo);
        selectionPanel.add(Box.createHorizontalStrut(10));
        selectionPanel.add(new JLabel("Year:"));
        selectionPanel.add(yearCombo);
        selectionPanel.add(Box.createHorizontalStrut(10));
        selectionPanel.add(computeButton);

        // Payroll details panel
        JPanel payrollDetailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Create payroll display fields
        addPayrollRow(payrollDetailsPanel, gbc, 0, "Gross Pay:", grossPayLabel);

        // Add separator
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        payrollDetailsPanel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        addPayrollRow(payrollDetailsPanel, gbc, 2, "SSS Contribution:", sssContribLabel);
        addPayrollRow(payrollDetailsPanel, gbc, 3, "PhilHealth Contribution:", philHealthContribLabel);
        addPayrollRow(payrollDetailsPanel, gbc, 4, "Pag-IBIG Contribution:", pagIbigContribLabel);
        addPayrollRow(payrollDetailsPanel, gbc, 5, "Withholding Tax:", withholdingTaxLabel);

        // Add separator
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        payrollDetailsPanel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        addPayrollRow(payrollDetailsPanel, gbc, 7, "Total Deductions:", totalDeductionsLabel);

        // Add separator
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        payrollDetailsPanel.add(new JSeparator(), gbc);

        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        addPayrollRow(payrollDetailsPanel, gbc, 9, "NET PAY:", netPayLabel);

        // Instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JTextArea instructionsText = new JTextArea(
                "Instructions:\n" +
                        "1. Select the month and year for payroll computation\n" +
                        "2. Click 'Compute Salary' to calculate the employee's pay\n" +
                        "3. The system will display gross pay, deductions, and net pay"
        );
        instructionsText.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionsText.setBackground(panel.getBackground());
        instructionsText.setEditable(false);
        instructionsText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        instructionsPanel.add(instructionsText, BorderLayout.CENTER);

        panel.add(selectionPanel, BorderLayout.NORTH);
        panel.add(payrollDetailsPanel, BorderLayout.CENTER);
        panel.add(instructionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addPayrollRow(JPanel parent, GridBagConstraints gbc, int row, String label, JLabel value) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel fieldLabel = createFieldLabel(label);
        if (label.contains("NET PAY")) {
            fieldLabel.setFont(new Font("Arial", Font.BOLD, 14));
            fieldLabel.setForeground(new Color(0, 128, 0));
        }
        parent.add(fieldLabel, gbc);
        gbc.gridx = 1;
        parent.add(value, gbc);
    }

    private void setupEventListeners() {
        computeButton.addActionListener(this);
        closeButton.addActionListener(this);
    }

    private void setWindowProperties() {
        setTitle("Employee Details - " + employee.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == computeButton) {
            computePayroll();
        } else if (e.getSource() == closeButton) {
            dispose();
        }
    }

    private void computePayroll() {
        try {
            int selectedMonth = monthCombo.getSelectedIndex() + 1; // Convert to 1-12
            int selectedYear = Integer.parseInt((String) yearCombo.getSelectedItem());

            // Validate month and year
            EmployeeDataBase.validateMonth(String.valueOf(selectedMonth));

            // Calculate payroll
            payrollCalc = employee.calculatePay(selectedMonth, selectedYear);

            // Update display
            updatePayrollDisplay();

            // Show success message
            String monthName = (String) monthCombo.getSelectedItem();
            JOptionPane.showMessageDialog(this,
                    String.format("Payroll computed successfully for %s %d!", monthName, selectedYear),
                    "Computation Complete", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error computing payroll: " + ex.getMessage(),
                    "Computation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePayrollDisplay() {
        if (payrollCalc != null) {
            grossPayLabel.setText(currencyFormat.format(payrollCalc.getGrossPay()));
            sssContribLabel.setText(currencyFormat.format(payrollCalc.getSssContribution()));
            philHealthContribLabel.setText(currencyFormat.format(payrollCalc.getPhilHealthContribution()));
            pagIbigContribLabel.setText(currencyFormat.format(payrollCalc.getPagIbigContribution()));
            withholdingTaxLabel.setText(currencyFormat.format(payrollCalc.getWithholdingTax()));
            totalDeductionsLabel.setText(currencyFormat.format(payrollCalc.getTotalDeductions()));
            netPayLabel.setText(currencyFormat.format(payrollCalc.getNetPay()));

            // Change colors based on values
            netPayLabel.setForeground(payrollCalc.getNetPay() > 0 ? new Color(0, 128, 0) : Color.RED);
            totalDeductionsLabel.setForeground(new Color(204, 0, 0));
        }
    }

    // Method to refresh employee data (useful if called from other windows)
    public void refreshEmployeeData() {
        loadEmployeeData();

        // Update labels
        empNumberLabel.setText(employee.getEmployeeNumber());
        nameLabel.setText(employee.getFullName());
        birthdayLabel.setText(employee.getBirthday());
        addressLabel.setText(employee.getAddress());
        phoneLabel.setText(employee.getPhoneNumber());
        sssLabel.setText(employee.getSssNumber());
        philHealthLabel.setText(employee.getPhilHealthNumber());
        tinLabel.setText(employee.getTin());
        pagIbigLabel.setText(employee.getPagIbigNumber());
        statusLabel.setText(employee.getStatus());
        positionLabel.setText(employee.getPosition());
        supervisorLabel.setText(employee.getImmediateSupervisor());
        basicSalaryLabel.setText(currencyFormat.format(employee.getBasicSalary()));

        setTitle("Employee Details - " + employee.getFullName());

        // Clear payroll display
        grossPayLabel.setText("");
        sssContribLabel.setText("");
        philHealthContribLabel.setText("");
        pagIbigContribLabel.setText("");
        withholdingTaxLabel.setText("");
        totalDeductionsLabel.setText("");
        netPayLabel.setText("");
    }
}


