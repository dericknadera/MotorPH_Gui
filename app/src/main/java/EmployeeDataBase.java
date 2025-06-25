package com.example.MotorPH.Gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeDataBase {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String status;
    private String position;
    private String immediateSupervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemiMonthlyRate;
    private double hourlyRate;

    // Constructor for creating new employee
    public EmployeeDataBase() {}

    // Constructor from CSV data
    public EmployeeDataBase(String[] csvData) {
        if (csvData.length >= 19) {
            this.employeeNumber = csvData[0];
            this.lastName = csvData[1];
            this.firstName = csvData[2];
            this.birthday = csvData[3];
            this.address = csvData[4];
            this.phoneNumber = csvData[5];
            this.sssNumber = csvData[6];
            this.philHealthNumber = csvData[7];
            this.tin = csvData[8];
            this.pagIbigNumber = csvData[9];
            this.status = csvData[10];
            this.position = csvData[11];
            this.immediateSupervisor = csvData[12];

            try {
                this.basicSalary = Double.parseDouble(csvData[13]);
                this.riceSubsidy = Double.parseDouble(csvData[14]);
                this.phoneAllowance = Double.parseDouble(csvData[15]);
                this.clothingAllowance = Double.parseDouble(csvData[16]);
                this.grossSemiMonthlyRate = Double.parseDouble(csvData[17]);
                this.hourlyRate = Double.parseDouble(csvData[18]);
            } catch (NumberFormatException e) {
                // Set default values if parsing fails
                this.basicSalary = 0.0;
                this.riceSubsidy = 0.0;
                this.phoneAllowance = 0.0;
                this.clothingAllowance = 0.0;
                this.grossSemiMonthlyRate = 0.0;
                this.hourlyRate = 0.0;
            }
        }
    }

    // Convert to CSV array
    public String[] toCSVArray() {
        return new String[] {
                employeeNumber, lastName, firstName, birthday, address, phoneNumber,
                sssNumber, philHealthNumber, tin, pagIbigNumber, status, position,
                immediateSupervisor, String.valueOf(basicSalary), String.valueOf(riceSubsidy),
                String.valueOf(phoneAllowance), String.valueOf(clothingAllowance),
                String.valueOf(grossSemiMonthlyRate), String.valueOf(hourlyRate)
        };
    }

    // Salary computation methods
    public PayrollCalculation calculatePay(int month, int year) {
        return new PayrollCalculation(this, month, year);
    }

    // Validation methods
    public static void validateEmployeeNumber(String empNum) throws IllegalArgumentException {
        if (empNum == null || empNum.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee number cannot be empty");
        }
        if (!empNum.matches("\\d{4,6}")) {
            throw new IllegalArgumentException("Employee number must be 4-6 digits");
        }
    }

    public static void validateMonth(String monthStr) throws IllegalArgumentException {
        if (monthStr == null || monthStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Month cannot be empty");
        }
        try {
            int month = Integer.parseInt(monthStr);
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Month must be between 1 and 12");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Month must be a valid number");
        }
    }

    public void validateEmployeeData() throws IllegalArgumentException {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (basicSalary < 0) {
            throw new IllegalArgumentException("Basic salary cannot be negative");
        }
        // Add more validations as needed
    }

    // Getters and Setters
    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSssNumber() { return sssNumber; }
    public void setSssNumber(String sssNumber) { this.sssNumber = sssNumber; }

    public String getPhilHealthNumber() { return philHealthNumber; }
    public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }

    public String getTin() { return tin; }
    public void setTin(String tin) { this.tin = tin; }

    public String getPagIbigNumber() { return pagIbigNumber; }
    public void setPagIbigNumber(String pagIbigNumber) { this.pagIbigNumber = pagIbigNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getImmediateSupervisor() { return immediateSupervisor; }
    public void setImmediateSupervisor(String immediateSupervisor) { this.immediateSupervisor = immediateSupervisor; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getRiceSubsidy() { return riceSubsidy; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }

    public double getPhoneAllowance() { return phoneAllowance; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }

    public double getClothingAllowance() { return clothingAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }

    public double getGrossSemiMonthlyRate() { return grossSemiMonthlyRate; }
    public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format("Employee #%s: %s, %s", employeeNumber, lastName, firstName);
    }

    // Inner class for payroll calculations
    public static class PayrollCalculation {
        private EmployeeDataBase employee;
        private int month;
        private int year;
        private double grossPay;
        private double sssContribution;
        private double philHealthContribution;
        private double pagIbigContribution;
        private double withholdingTax;
        private double totalDeductions;
        private double netPay;

        public PayrollCalculation(EmployeeDataBase employee, int month, int year) {
            this.employee = employee;
            this.month = month;
            this.year = year;
            calculatePayroll();
        }

        private void calculatePayroll() {
            // Basic gross pay calculation
            grossPay = employee.getGrossSemiMonthlyRate() * 2; // Monthly pay

            // Add allowances
            grossPay += employee.getRiceSubsidy();
            grossPay += employee.getPhoneAllowance();
            grossPay += employee.getClothingAllowance();

            // Calculate deductions (simplified calculations)
            sssContribution = calculateSSS(grossPay);
            philHealthContribution = calculatePhilHealth(grossPay);
            pagIbigContribution = calculatePagIbig(grossPay);
            withholdingTax = calculateWithholdingTax(grossPay);

            totalDeductions = sssContribution + philHealthContribution + pagIbigContribution + withholdingTax;
            netPay = grossPay - totalDeductions;
        }

        private double calculateSSS(double grossPay) {
            // Simplified SSS calculation
            if (grossPay <= 3250) return 135;
            else if (grossPay <= 3750) return 157.50;
            else if (grossPay <= 4250) return 180;
            else if (grossPay <= 4750) return 202.50;
            else if (grossPay <= 5250) return 225;
            else if (grossPay <= 5750) return 247.50;
            else if (grossPay <= 6250) return 270;
            else if (grossPay <= 6750) return 292.50;
            else if (grossPay <= 7250) return 315;
            else if (grossPay <= 7750) return 337.50;
            else if (grossPay <= 8250) return 360;
            else if (grossPay <= 8750) return 382.50;
            else if (grossPay <= 9250) return 405;
            else if (grossPay <= 9750) return 427.50;
            else if (grossPay <= 10250) return 450;
            else if (grossPay <= 10750) return 472.50;
            else if (grossPay <= 11250) return 495;
            else if (grossPay <= 11750) return 517.50;
            else if (grossPay <= 12250) return 540;
            else if (grossPay <= 12750) return 562.50;
            else if (grossPay <= 13250) return 585;
            else if (grossPay <= 13750) return 607.50;
            else if (grossPay <= 14250) return 630;
            else if (grossPay <= 14750) return 652.50;
            else if (grossPay <= 15250) return 675;
            else if (grossPay <= 15750) return 697.50;
            else if (grossPay <= 16250) return 720;
            else if (grossPay <= 16750) return 742.50;
            else if (grossPay <= 17250) return 765;
            else if (grossPay <= 17750) return 787.50;
            else if (grossPay <= 18250) return 810;
            else if (grossPay <= 18750) return 832.50;
            else if (grossPay <= 19250) return 855;
            else if (grossPay <= 19750) return 877.50;
            else return 900; // Max contribution
        }

        private double calculatePhilHealth(double grossPay) {
            // Simplified PhilHealth calculation - 1.25% of gross pay, max 1750
            double contribution = grossPay * 0.0125;
            return Math.min(contribution, 1750);
        }

        private double calculatePagIbig(double grossPay) {
            // Simplified Pag-IBIG calculation - 1% of gross pay, max 100
            double contribution = grossPay * 0.01;
            return Math.min(contribution, 100);
        }

        private double calculateWithholdingTax(double grossPay) {
            // Simplified withholding tax calculation
            double taxableIncome = grossPay - sssContribution - philHealthContribution - pagIbigContribution;

            if (taxableIncome <= 20833) return 0;
            else if (taxableIncome <= 33333) return (taxableIncome - 20833) * 0.20;
            else if (taxableIncome <= 66667) return 2500 + (taxableIncome - 33333) * 0.25;
            else if (taxableIncome <= 166667) return 10833.33 + (taxableIncome - 66667) * 0.30;
            else if (taxableIncome <= 666667) return 40833.33 + (taxableIncome - 166667) * 0.32;
            else return 200833.33 + (taxableIncome - 666667) * 0.35;
        }

        // Getters
        public double getGrossPay() { return grossPay; }
        public double getSssContribution() { return sssContribution; }
        public double getPhilHealthContribution() { return philHealthContribution; }
        public double getPagIbigContribution() { return pagIbigContribution; }
        public double getWithholdingTax() { return withholdingTax; }
        public double getTotalDeductions() { return totalDeductions; }
        public double getNetPay() { return netPay; }
        public int getMonth() { return month; }
        public int getYear() { return year; }
    }
}


