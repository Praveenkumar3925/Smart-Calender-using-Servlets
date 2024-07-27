import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/add-event")
public class AddServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/praveen";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Praveen@123";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String eventName = request.getParameter("event_name");
        String eventDate = request.getParameter("event_date");
        String interests = request.getParameter("interests");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded successfully");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}


        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO events (event_name, event_date, interests) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, eventName);
                stmt.setDate(2, Date.valueOf(eventDate));
                stmt.setString(3, interests);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Write successful response
                    out.println("<!DOCTYPE html>");
                    out.println("<html lang='en'>");
                    out.println("<head>");
                    out.println("<meta charset='UTF-8'>");
                    out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                    out.println("<title>Event Added</title>");
                    out.println("<style>");
                    out.println("body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f9; }");
                    out.println("h1 { color: #333; }");
                    out.println("a { display: inline-block; margin: 10px; text-decoration: none; color: #4CAF50; }");
                    out.println("a:hover { text-decoration: underline; }");
                    out.println("</style>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Event Details Inserted Successfully</h1>");
                    out.println("<a href='view-events'>View Upcoming Events</a>");
                    out.println("<a href='new.html'>Back to Add Event</a>");
                    out.println("</body>");
                    out.println("</html>");
                } else {
                    throw new SQLException("Failed to insert event.");
                }
            }
        } catch (SQLException e) {
            // Write error response
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Error</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f9; }");
            out.println("h1 { color: #d9534f; }");
            out.println("p { color: #d9534f; }");
            out.println("a { display: inline-block; margin: 10px; text-decoration: none; color: #4CAF50; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Failed to Add Event</h1>");
            out.println("<p>There was an error processing your request. Please try again.</p>");
            out.println("<a href='new.html'>Back to Add Event</a>");
            out.println("</body>");
            out.println("</html>");
            e.printStackTrace();
        }
    }
}
