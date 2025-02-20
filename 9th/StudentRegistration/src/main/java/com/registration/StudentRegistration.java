package com.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/StudentRegistration")
public class StudentRegistration extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    String DB_URL = "jdbc:mysql://localhost:3306/student_db?useSSL=false&serverTimezone=UTC";

    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "1234"; // Replace with your MySQL password

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String rollNumber = request.getParameter("rollNumber");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String academicYear = request.getParameter("academicYear");
        String gender = request.getParameter("gender");
        String[] qualifications = request.getParameterValues("qualification");

        // Combine qualifications into a single string
        String qualification = String.join(", ", qualifications);

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Check if roll number already exists
            String checkQuery = "SELECT * FROM students WHERE roll_number = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setInt(1, Integer.parseInt(rollNumber));
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                out.println("<h3 style='color:red;'>This student is already registered!</h3>");
            } else {
                // Insert student data into database
                String insertQuery = "INSERT INTO students (roll_number, first_name, last_name, academic_year, gender, qualification) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setInt(1, Integer.parseInt(rollNumber));
                insertStmt.setString(2, firstName);
                insertStmt.setString(3, lastName);
                insertStmt.setString(4, academicYear);
                insertStmt.setString(5, gender);
                insertStmt.setString(6, qualification);

                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    out.println("<h3 style='color:green;'>Student successfully registered!</h3>");
                } else {
                    out.println("<h3 style='color:red;'>Registration failed. Please try again.</h3>");
                }
                insertStmt.close();
            }

            checkStmt.close();
            con.close();
        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}

// CREATE DATABASE student_db;

// USE student_db;

// CREATE TABLE students (
// roll_number INT PRIMARY KEY,
// first_name VARCHAR(50),
// last_name VARCHAR(50),
// academic_year VARCHAR(20),
// gender VARCHAR(10),
// qualification VARCHAR(100)
// );
