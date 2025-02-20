package com.Update;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/UpdateStudentServlet")
public class UpdateStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rollNumber = request.getParameter("rollNumber");
        String firstName = request.getParameter("firstName");
        String academicYear = request.getParameter("academicYear");
        String mobileNumber = request.getParameter("mobileNumber");
        response.setContentType("text/html");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StudentDB", "root", "1234");

            String updateQuery = "UPDATE Student SET first_name = ?, academic_year = ?, mobile_number = ? WHERE roll_number = ?";
            ps = conn.prepareStatement(updateQuery);
            ps.setString(1, firstName);
            ps.setString(2, academicYear);
            ps.setString(3, mobileNumber);
            ps.setInt(4, Integer.parseInt(rollNumber));

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                response.getWriter().println("<h3 style='color:green;'>Student information updated successfully.</h3>");
            } else {
                response.getWriter().println("<h3 style='color:red;'>Failed to update student information.</h3>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        } finally {
            try { if (ps != null) ps.close(); if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }
}
