import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet("/view-events")
public class EventsServlets extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/praveen";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Praveen@123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String eventQuery = "SELECT event_name, event_date, interests FROM events";
            StringBuilder eventsHtml = new StringBuilder();

            try (PreparedStatement stmt = conn.prepareStatement(eventQuery);
                 ResultSet rs = stmt.executeQuery()) {
                eventsHtml.append("<table border='1' cellpadding='10' style='margin: 20px auto; border-collapse: collapse;'>");
                eventsHtml.append("<tr><th>Event Name</th><th>Event Date</th><th>Interests</th></tr>");
                while (rs.next()) {
                    eventsHtml.append("<tr>")
                              .append("<td>").append(rs.getString("event_name")).append("</td>")
                              .append("<td>").append(rs.getDate("event_date")).append("</td>")
                              .append("<td>").append(rs.getString("interests")).append("</td>")
                              .append("</tr>");
                }
                eventsHtml.append("</table>");
            }

            // Write HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Upcoming Events</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f9; }");
            out.println("h1 { color: #333; }");
            out.println("table { width: 80%; margin: 20px auto; border-collapse: collapse; }");
            out.println("th, td { padding: 10px; border: 1px solid #ddd; }");
            out.println("th { background-color: #4CAF50; color: white; }");
            out.println("a { display: inline-block; margin: 10px; text-decoration: none; color: #4CAF50; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Upcoming Events</h1>");
            out.println(eventsHtml.toString());
            out.println("<br><a href='new.html'>Back to Add Event</a>");
            out.println("</body>");
            out.println("</html>");

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
            out.println("<h1>Failed to Retrieve Events</h1>");
            out.println("<p>There was an error retrieving the events. Please try again later.</p>");
            out.println("<a href='new.html'>Back to Add Event</a>");
            out.println("</body>");
            out.println("</html>");
            e.printStackTrace();
        }
    }
}
