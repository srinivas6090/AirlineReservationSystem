package servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import db.DBConnection;

@WebServlet("/FlightSearchServlet")
public class FlightSearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String departure = request.getParameter("departure");
        String destination = request.getParameter("destination");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM flights WHERE departure = ? AND destination = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, departure);
            ps.setString(2, destination);
            ResultSet rs = ps.executeQuery();

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Available Flights</h2>");
            while (rs.next()) {
                out.println("<p>Flight: " + rs.getString("flight_number") + 
                            ", Airline: " + rs.getString("airline") + 
                            ", Price: $" + rs.getDouble("price") + "</p>");
            }
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
