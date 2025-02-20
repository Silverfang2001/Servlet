package com.emp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RetrieveEmployee")
public class RetrieveEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String DB_URL = "jdbc:mysql://localhost:3306/EmployeeDB";
    String DB_USER = "root";
    String DB_PASSWORD = "1234";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("firstName");

        List<Map<String, String>> employees = new ArrayList<>();
        String message = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Retrieve employee by first name
            String query = "SELECT * FROM employees WHERE first_name LIKE ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + firstName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> emp = new HashMap<>();
                emp.put("emp_id", rs.getString("emp_id"));
                emp.put("first_name", rs.getString("first_name"));
                emp.put("last_name", rs.getString("last_name"));
                emp.put("department", rs.getString("department"));
                emp.put("salary", rs.getString("salary"));
                employees.add(emp);
            }

            if (employees.isEmpty()) {
                message = "No employee found with the first name: " + firstName;
            }

            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error occurred while retrieving employee information.";
        }

        request.setAttribute("employees", employees);
        request.setAttribute("message", message);
        request.getRequestDispatcher("display_employees.jsp").forward(request, response);
    }
}

// CREATE DATABASE IF NOT EXISTS EmployeeDB;
// USE EmployeeDB;

// CREATE TABLE employees (
// emp_id INT AUTO_INCREMENT PRIMARY KEY,
// first_name VARCHAR(50) NOT NULL,
// last_name VARCHAR(50) NOT NULL,
// department VARCHAR(50),
// salary DECIMAL(10, 2)
// );

// INSERT INTO employees (first_name, last_name, department, salary) VALUES
// ('John', 'Doe', 'HR', 55000.00),
// ('Jane', 'Smith', 'IT', 65000.00),
// ('Mark', 'Taylor', 'Finance', 75000.00);