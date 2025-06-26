🚗 MotorPH_Gui_App
MotorPH GUI App is a Java Swing-based desktop application designed to manage employee records, salary computation, and user login securely using a Graphical User Interface. It is a GUI implementation of a previously console-based application, developed to enhance user experience, data integrity, and operational efficiency.

📌 Features
This application was developed as part of a course project under MO-IT103 (Computer Programming 2) and includes the following main features, corresponding to officially approved change requests:

✅ MPHCR01 – GUI Implementation
Converted the original console-based app into a fully functional GUI using Java Swing.

Input validation and basic exception handling included.

✅ MPHCR02 – Employee Record Viewer
View all employees in a JTable.

View full employee details and salary computation for selected employee.

Add new employees and save to CSV file.

✅ MPHCR03 – Update/Delete Functionality
Update existing employee records.

Delete employee records directly from the table.

Refreshes data upon change.

✅ MPHCR04 – Login System
Secure login using username and password.

Access restricted to authenticated users.

CSV-based credential verification (optional in final version).

📂 File Structure

MotorPH_Gui/
└── app/
    └── src/
        └── main/
            └── java/
                └── (default package)
                    ├── App.java                // Main entry point
                    ├── Login.java             // Login form
                    ├── MainDashboard.java     // Employee data dashboard
                    ├── EmployeeForm.java      // Add employee form
                    ├── UpdateEmployeeForm.java// Update existing employee
                    ├── EmployeeCSVReader.java // Handles CSV read/write
                    ├── EmployeeDetails.java   // Employee data model
                    └── EmployeeDataBase.java  // (optional local DB manager)
⚙️ Technologies Used
Java 23 (OpenJDK)

Java Swing (GUI)

NetBeans 25 IDE

Gradle Build Tool

CSV File Handling

🏁 How to Run
Clone this repository:

bash
Copy
Edit
git clone https://github.com/yourusername/MotorPH_Gui_App.git
cd MotorPH_Gui_App/app
Open the project in NetBeans 25 or compatible IDE.

Build and Run:

arduino
Copy
Edit
./gradlew run
Or run App.java directly from the IDE.

Make sure you have a valid employees.csv file in the root directory. You can create a blank one or use sample data.

📌 Author
Roderick Nadera
🛠 BSIT Marketing Technology |CP2 Output
📚 Mapúa Malayan Digital College

