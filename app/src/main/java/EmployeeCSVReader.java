package com.example.MotorPH.Gui;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeCSVReader {
    private static final String EMPLOYEES_FILE = "C:\\Users\\lrrna\\OneDrive\\Documents\\NetBeansProjects\\MotorPH_Gui\\app\\src\\main\\resources\\MotorPH-Employee-Data-Employee-Details.csv";
    private static final String USERS_FILE = "users.csv";

    // Employee CSV Headers
    private static final String[] EMPLOYEE_HEADERS = {
            "Employee Number", "Last Name", "First Name", "Birthday", "Address",
            "Phone Number", "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number",
            "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy",
            "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
    };

    // User CSV Headers
    private static final String[] USER_HEADERS = {"Username", "Password", "Role"};

    public EmployeeCSVReader() {
        initializeFiles();
    }

    private void initializeFiles() {
        // Initialize employees.csv if it doesn't exist
        File employeesFile = new File(EMPLOYEES_FILE);
        if (!employeesFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
                writer.println(String.join(",", EMPLOYEE_HEADERS));
                // Add sample data
                writer.println("10001,Dela Cruz,Juan,1990-05-15,123 Main St Manila,09171234567,12-3456789-0,123456789012,123-456-789-000,123456789012,Regular,Software Developer,Jane Smith,50000,1500,2000,1000,25000,312.50");
                writer.println("10002,Santos,Maria,1985-08-22,456 Oak Ave Quezon City,09187654321,98-7654321-0,987654321098,987-654-321-000,987654321098,Regular,HR Manager,John Doe,55000,1500,2000,1000,27500,343.75");
            } catch (IOException e) {
                System.err.println("Error creating employees.csv: " + e.getMessage());
            }
        }

        // Initialize users.csv if it doesn't exist
        File usersFile = new File(USERS_FILE);
        if (!usersFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
                writer.println(String.join(",", USER_HEADERS));
                // Add sample users
                writer.println("admin,admin123,Administrator");
                writer.println("hr_user,hr123,HR Manager");
                writer.println("employee,emp123,Employee");
            } catch (IOException e) {
                System.err.println("Error creating users.csv: " + e.getMessage());
            }
        }
    }

    public List<String[]> readEmployees() {
        List<String[]> employees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEES_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    employees.add(parseCSVLine(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading employees.csv: " + e.getMessage());
        }
        return employees;
    }

    public String[] findEmployee(String employeeNumber) {
        List<String[]> employees = readEmployees();
        for (String[] employee : employees) {
            if (employee.length > 0 && employee[0].equals(employeeNumber)) {
                return employee;
            }
        }
        return null;
    }

    public boolean addEmployee(String[] employeeData) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE, true))) {
            writer.println(String.join(",", employeeData));
            return true;
        } catch (IOException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(String employeeNumber, String[] newData) {
        List<String[]> employees = readEmployees();
        boolean found = false;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i)[0].equals(employeeNumber)) {
                employees.set(i, newData);
                found = true;
                break;
            }
        }

        if (found) {
            return writeAllEmployees(employees);
        }
        return false;
    }

    public boolean deleteEmployee(String employeeNumber) {
        List<String[]> employees = readEmployees();
        boolean removed = employees.removeIf(emp -> emp[0].equals(employeeNumber));

        if (removed) {
            return writeAllEmployees(employees);
        }
        return false;
    }

    private boolean writeAllEmployees(List<String[]> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
            writer.println(String.join(",", EMPLOYEE_HEADERS));
            for (String[] employee : employees) {
                writer.println(String.join(",", employee));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing employees.csv: " + e.getMessage());
            return false;
        }
    }

    public boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] userData = parseCSVLine(line);
                    if (userData.length >= 2 && userData[0].equals(username) && userData[1].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users.csv: " + e.getMessage());
        }
        return false;
    }

    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());

        return result.toArray(new String[0]);
    }

    public String generateNextEmployeeNumber() {
        List<String[]> employees = readEmployees();
        int maxNumber = 10000;

        for (String[] employee : employees) {
            try {
                int empNum = Integer.parseInt(employee[0]);
                if (empNum > maxNumber) {
                    maxNumber = empNum;
                }
            } catch (NumberFormatException e) {
                // Skip invalid employee numbers
            }
        }

        return String.valueOf(maxNumber + 1);
    }
}


