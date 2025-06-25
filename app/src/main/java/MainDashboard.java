package com.example.MotorPH.Gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainDashboard extends JFrame implements ActionListener {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private EmployeeCSVReader csvReader;

    // Buttons
    private JButton viewEmployeeButton;
    private JButton newEmployeeButton;
    private JButton updateEmployeeButton;
    private JButton deleteEmployeeButton;
    private JButton refreshButton;
    private JButton logoutButton;

    // Input fields for selected employee
    private JTextField empNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField sssField;
    private JTextField philHealthField;
    private JTextField tinField;
    private JTextField pagIbigField;
    private JTextField birthdayField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField statusField;
    private JTextField positionField;
    private JTextField supervisorField;
    private JTextField basicSalaryField;
    private JTextField riceSubsidyField;
    private JTextField phoneAllowanceField;
    private JTextField clothingAllowanceField;

    // Search field
    private JTextField searchField;

    private String selectedEmployeeNumber = null;

    public MainDashboard() {
        csvReader = new EmployeeCSVReader();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setWindowProperties();
        loadEmployeeData();
    }

    private void initializeComponents() {
        // Create table
        String[] columnNames = {
                "Employee #", "Last Name", "First Name", "SSS #",
                "PhilHealth #", "TIN", "Pag-IBIG #"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.setRowHeight(25);

        // Create row sorter for table
        rowSorter = new TableRowSorter<>(tableModel);
        employeeTable.setRowSorter(rowSorter);

        // Create buttons
        viewEmployeeButton = createButton("View Employee", new Color(0, 123, 255));
        newEmployeeButton = createButton("New Employee", new Color(40, 167, 69));
        updateEmployeeButton = createButton("Update Employee", new Color(255, 193, 7));
        deleteEmployeeButton = createButton("Delete Employee", new Color(220, 53, 69));
        refreshButton = createButton("Refresh", new Color(108, 117, 125));
        logoutButton = createButton("Logout", new Color(52, 58, 64));

        // Initially disable update and delete buttons
        updateEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);
        viewEmployeeButton.setEnabled(false);

        // Create input fields
        empNumberField = createTextField(false); // Read-only for employee number
        lastNameField = createTextField(true);
        firstNameField = createTextField(true);
        sssField = createTextField(true);
        philHealthField = createTextField(true);
        tinField = createTextField(true);
        pagIbigField = createTextField(true);
        birthdayField = createTextField(true);
        addressField = createTextField(true);
        phoneField = createTextField(true);
        statusField = createTextField(true);
        positionField = createTextField(true);
        supervisorField = createTextField(true);
        basicSalaryField = createTextField(true);
        riceSubsidyField = createTextField(true);
        phoneAllowanceField = createTextField(true);
        clothingAllowanceField = createTextField(true);

        // Search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        clearInputFields();
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    private JTextField createTextField(boolean editable) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setEditable(editable);
        if (!editable) {
            field.setBackground(new Color(248, 249, 250));
        }
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        return field;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("MotorPH Employee Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(0, 102, 204));
        searchPanel.add(new JLabel("Search: ") {{ setForeground(Color.WHITE); setFont(new Font("Arial", Font.PLAIN, 12)); }});
        searchPanel.add(searchField);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Employee Records"));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel for table actions
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tableButtonPanel.add(viewEmployeeButton);
        tableButtonPanel.add(newEmployeeButton);
        tableButtonPanel.add(updateEmployeeButton);
        tableButtonPanel.add(deleteEmployeeButton);
        tableButtonPanel.add(refreshButton);

        tablePanel.add(tableButtonPanel, BorderLayout.SOUTH);

        // Employee details panel
        JPanel detailsPanel = createEmployeeDetailsPanel();

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, detailsPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.6);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createEmployeeDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        fieldsPanel.add(new JLabel("Employee #:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(empNumberField, gbc);

        gbc.gridx = 2;
        fieldsPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(lastNameField, gbc);

        gbc.gridx = 4;
        fieldsPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 5;
        fieldsPanel.add(firstNameField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        fieldsPanel.add(new JLabel("SSS #:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(sssField, gbc);

        gbc.gridx = 2;
        fieldsPanel.add(new JLabel("PhilHealth #:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(philHealthField, gbc);

        gbc.gridx = 4;
        fieldsPanel.add(new JLabel("TIN:"), gbc);
        gbc.gridx = 5;
        fieldsPanel.add(tinField, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        fieldsPanel.add(new JLabel("Pag-IBIG #:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(pagIbigField, gbc);

        gbc.gridx = 2;
        fieldsPanel.add(new JLabel("Birthday:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(birthdayField, gbc);

        gbc.gridx = 4;
        fieldsPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 5;
        fieldsPanel.add(phoneField, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 3;
        fieldsPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(addressField, gbc);

        gbc.gridx = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 4;
        fieldsPanel.add(statusField, gbc);

        gbc.gridx = 5;
        fieldsPanel.add(positionField, gbc);

        // Row 5 - Salary info
        gbc.gridx = 0; gbc.gridy = 4;
        fieldsPanel.add(new JLabel("Supervisor:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(supervisorField, gbc);

        gbc.gridx = 2;
        fieldsPanel.add(new JLabel("Basic Salary:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(basicSalaryField, gbc);

        gbc.gridx = 4;
        fieldsPanel.add(new JLabel("Rice Subsidy:"), gbc);
        gbc.gridx = 5;
        fieldsPanel.add(riceSubsidyField, gbc);

        // Row 6
        gbc.gridx = 0; gbc.gridy = 5;
        fieldsPanel.add(new JLabel("Phone Allow:"), gbc);
        gbc.gridx = 1;
        fieldsPanel.add(phoneAllowanceField, gbc);

        gbc.gridx = 2;
        fieldsPanel.add(new JLabel("Clothing Allow:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(clothingAllowanceField, gbc);

        panel.add(fieldsPanel, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventListeners() {
        viewEmployeeButton.addActionListener(this);
        newEmployeeButton.addActionListener(this);
        updateEmployeeButton.addActionListener(this);
        deleteEmployeeButton.addActionListener(this);
        refreshButton.addActionListener(this);
        logoutButton.addActionListener(this);

        // Table selection listener
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });

        // Double-click to view employee
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && employeeTable.getSelectedRow() != -1) {
                    viewSelectedEmployee();
                }
            }
        });

        // Search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
    }

    private void setWindowProperties() {
        setTitle("MotorPH Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);
        List<String[]> employees = csvReader.readEmployees();

        for (String[] employee : employees) {
            if (employee.length >= 7) {
                Object[] row = {
                        employee[0], // Employee Number
                        employee[1], // Last Name
                        employee[2], // First Name
                        employee[6], // SSS Number
                        employee[7], // PhilHealth Number
                        employee[8], // TIN
                        employee[9]  // Pag-IBIG Number
                };
                tableModel.addRow(row);
            }
        }
    }

    private void handleTableSelection() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            // Convert view row to model row (in case table is sorted)
            int modelRow = employeeTable.convertRowIndexToModel(selectedRow);
            selectedEmployeeNumber = (String) tableModel.getValueAt(modelRow, 0);

            // Enable buttons
            viewEmployeeButton.setEnabled(true);
            updateEmployeeButton.setEnabled(true);
            deleteEmployeeButton.setEnabled(true);

            // Load employee details into fields
            loadEmployeeDetails(selectedEmployeeNumber);
        } else {
            selectedEmployeeNumber = null;
            viewEmployeeButton.setEnabled(false);
            updateEmployeeButton.setEnabled(false);
            deleteEmployeeButton.setEnabled(false);
            clearInputFields();
        }
    }

    private void loadEmployeeDetails(String employeeNumber) {
        String[] employeeData = csvReader.findEmployee(employeeNumber);
        if (employeeData != null && employeeData.length >= 19) {
            empNumberField.setText(employeeData[0]);
            lastNameField.setText(employeeData[1]);
            firstNameField.setText(employeeData[2]);
            birthdayField.setText(employeeData[3]);
            addressField.setText(employeeData[4]);
            phoneField.setText(employeeData[5]);
            sssField.setText(employeeData[6]);
            philHealthField.setText(employeeData[7]);
            tinField.setText(employeeData[8]);
            pagIbigField.setText(employeeData[9]);
            statusField.setText(employeeData[10]);
            positionField.setText(employeeData[11]);
            supervisorField.setText(employeeData[12]);
            basicSalaryField.setText(employeeData[13]);
            riceSubsidyField.setText(employeeData[14]);
            phoneAllowanceField.setText(employeeData[15]);
            clothingAllowanceField.setText(employeeData[16]);
        }
    }

    private void clearInputFields() {
        empNumberField.setText("");
        lastNameField.setText("");
        firstNameField.setText("");
        birthdayField.setText("");
        addressField.setText("");
        phoneField.setText("");
        sssField.setText("");
        philHealthField.setText("");
        tinField.setText("");
        pagIbigField.setText("");
        statusField.setText("");
        positionField.setText("");
        supervisorField.setText("");
        basicSalaryField.setText("");
        riceSubsidyField.setText("");
        phoneAllowanceField.setText("");
        clothingAllowanceField.setText("");
    }

    private void filterTable() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewEmployeeButton) {
            viewSelectedEmployee();
        } else if (e.getSource() == newEmployeeButton) {
            openEmployeeForm(null);
        } else if (e.getSource() == updateEmployeeButton) {
            updateSelectedEmployee();
        } else if (e.getSource() == deleteEmployeeButton) {
            deleteSelectedEmployee();
        } else if (e.getSource() == refreshButton) {
            refreshData();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }

    private void viewSelectedEmployee() {
        if (selectedEmployeeNumber != null) {
            new EmployeeDetails(selectedEmployeeNumber).setVisible(true);
        }
    }

    private void openEmployeeForm(String employeeNumber) {
        EmployeeForm form = new EmployeeForm(this, employeeNumber);
        form.setVisible(true);
    }

    private void updateSelectedEmployee() {
        if (selectedEmployeeNumber != null) {
            try {
                // Validate input
                validateEmployeeInput();

                // Create updated employee data
                String[] updatedData = {
                        selectedEmployeeNumber,
                        lastNameField.getText().trim(),
                        firstNameField.getText().trim(),
                        birthdayField.getText().trim(),
                        addressField.getText().trim(),
                        phoneField.getText().trim(),
                        sssField.getText().trim(),
                        philHealthField.getText().trim(),
                        tinField.getText().trim(),
                        pagIbigField.getText().trim(),
                        statusField.getText().trim(),
                        positionField.getText().trim(),
                        supervisorField.getText().trim(),
                        basicSalaryField.getText().trim(),
                        riceSubsidyField.getText().trim(),
                        phoneAllowanceField.getText().trim(),
                        clothingAllowanceField.getText().trim(),
                        "0", // Gross semi-monthly rate (calculated)
                        "0"  // Hourly rate (calculated)
                };

                // Calculate rates
                try {
                    double basicSalary = Double.parseDouble(basicSalaryField.getText().trim());
                    double grossSemiMonthly = basicSalary / 2;
                    double hourlyRate = basicSalary / (8 * 22); // Assuming 8 hours/day, 22 days/month
                    updatedData[17] = String.valueOf(grossSemiMonthly);
                    updatedData[18] = String.valueOf(hourlyRate);
                } catch (NumberFormatException ex) {
                    // Keep default values
                }

                if (csvReader.updateEmployee(selectedEmployeeNumber, updatedData)) {
                    JOptionPane.showMessageDialog(this, "Employee updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update employee!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Validation Error: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validateEmployeeInput() throws IllegalArgumentException {
        if (lastNameField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (firstNameField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        try {
            if (!basicSalaryField.getText().trim().isEmpty()) {
                double salary = Double.parseDouble(basicSalaryField.getText().trim());
                if (salary < 0) {
                    throw new IllegalArgumentException("Basic salary cannot be negative");
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Basic salary must be a valid number");
        }
    }

    private void deleteSelectedEmployee() {
        if (selectedEmployeeNumber != null) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete employee #" + selectedEmployeeNumber + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                if (csvReader.deleteEmployee(selectedEmployeeNumber)) {
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete employee!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refreshData() {
        loadEmployeeData();
        selectedEmployeeNumber = null;
        clearInputFields();
        viewEmployeeButton.setEnabled(false);
        updateEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new Login().setVisible(true);
            });
        }
    }

    public void refreshEmployeeTable() {
        refreshData();
    }
}


