package com.book;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/BookSearchServlet")
public class BookSearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Books", "root", "1234");

            String query = "SELECT * FROM books WHERE title=? OR author=? OR isbn=?";
            PreparedStatement ps = conn.prepareStatement(query);
            String searchPattern = searchQuery;
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            ResultSet rs = ps.executeQuery();

            out.println("<h2>Search Results:</h2>");
            out.println("<table border='1'><tr><th>Title</th><th>Author</th><th>ISBN</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("title") + "</td><td>" + rs.getString("author") + "</td><td>" + rs.getString("isbn") + "</td></tr>");
            }
            out.println("</table>");

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }
}
