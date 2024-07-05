package com.mycompany.dao;

import com.mycompany.main.Booking;
import com.mycompany.main.Customer;
import com.mycompany.main.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private final Connection connection;

    public BookingDAO(Connection connection) {
        this.connection = connection;
    }

    //Create
    public Booking addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO Booking (StartDate, EndDate, CustomerID, RoomID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, booking.getStartDate());
            statement.setString(2, booking.getEndDate());
            statement.setInt(3, booking.getCustomerId());
            statement.setInt(4, booking.getRoomId());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                }
            }
            
        // Fetch the customer details
        CustomerDAO customerDAO = new CustomerDAO(connection);
        Customer customer = customerDAO.getCustomerById(booking.getCustomerId());
        booking.setCustomer(customer);
        
        // Fetch and set the room details
        RoomDAO roomDAO = new RoomDAO(connection);
        Room room = roomDAO.getRoomById(booking.getRoomId());
        booking.setRoom(room);
        
        return booking;
        }
    }

    //Read
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking";
        try (PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                bookings.add(booking);
            }
        }
        return bookings;
    }

    //Read
    public Booking getBookingById(int id) throws SQLException {
        String sql = "SELECT * FROM Booking WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractBookingFromResultSet(resultSet);
                }
            }
        }
        return null; // Booking not found
    }
    
    public Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        String startDate = resultSet.getString("StartDate");
        String endDate = resultSet.getString("EndDate");
        int customerId = resultSet.getInt("customerId");
        int roomId = resultSet.getInt("roomId");

        // Create a booking object with customerId
        Booking booking = new Booking(id, startDate, endDate, customerId, roomId);

        // Fetch and set the Customer object
        CustomerDAO customerDAO = new CustomerDAO(connection);
        Customer customer = customerDAO.getCustomerById(customerId);
        booking.setCustomer(customer);
    
        // Fetch and set the room details
        RoomDAO roomDAO = new RoomDAO(connection);
        Room room = roomDAO.getRoomById(booking.getRoomId());
        booking.setRoom(room);

        return booking;
    }
        
    //Update
    public boolean updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE Booking SET StartDate = ?, EndDate = ? WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, booking.getStartDate());
            statement.setString(2, booking.getEndDate());
            statement.setInt(3, booking.getId());

            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                // Fetch the customer details
                CustomerDAO customerDAO = new CustomerDAO(connection);
                Customer customer = customerDAO.getCustomerById(booking.getCustomerId());
                booking.setCustomer(customer);
                
                // Fetch and set the room details
                RoomDAO roomDAO = new RoomDAO(connection);
                Room room = roomDAO.getRoomById(booking.getRoomId());
                booking.setRoom(room);
            }
            
            return rowsUpdated > 0; // Returns true if at least one row was updated
        }
    }

    //Delete
    public boolean deleteBooking(int id) throws SQLException {
        String sql = "DELETE FROM Booking WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returns true if at least one row was deleted
        }
    }
}
