package servlets;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import db.DBConnection;

@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        int flightId = Integer.parseInt(request.getParameter("flight_id"));
        String seatNumber = request.getParameter("seat_number");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO reservations (user_id, flight_id, seat_number) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, flightId);
            ps.setString(3, seatNumber);
            ps.executeUpdate();

            // Reduce available seats
            String updateSeats = "UPDATE flights SET available_seats = available_seats - 1 WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(updateSeats);
            ps2.setInt(1, flightId);
            ps2.executeUpdate();

            response.sendRedirect("booking.html?success=booked");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("booking.html?error=failed");
        }
    }
}
