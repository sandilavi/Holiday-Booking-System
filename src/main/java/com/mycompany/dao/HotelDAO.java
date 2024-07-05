package com.mycompany.dao;

import com.mycompany.main.Hotel;
import com.mycompany.main.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {
    private final Connection connection;

    public HotelDAO(Connection connection) {
        this.connection = connection;
    }

    //Create
    public Hotel addHotel(Hotel hotel) throws SQLException {
        String sql = "INSERT INTO Hotel (Name, Address) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getAddress());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    hotel.setId(generatedKeys.getInt(1));
                }
            }
            
            // Fetch and set rooms for the hotel
            hotel.setRooms(new ArrayList<>()); // Assuming no rooms initially
            return hotel;
        }
    }

    //Read
    public List<Hotel> getAllHotels() throws SQLException {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM Hotel";
        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Hotel hotel = extractHotelFromResultSet(resultSet);

                // Fetch rooms associated with the hotel
                RoomDAO roomDAO = new RoomDAO(connection);
                List<Room> rooms = roomDAO.getRoomsByHotelId(hotel.getId());
                hotel.setRooms(rooms);

                hotels.add(hotel);
            }
        }
        return hotels;
    }

    //Read
    public Hotel getHotelById(int id) throws SQLException {
        String sql = "SELECT * FROM Hotel WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Hotel hotel = extractHotelFromResultSet(resultSet);

                    // Fetch and set the rooms for the hotel
                    RoomDAO roomDAO = new RoomDAO(connection);
                    List<Room> rooms = roomDAO.getRoomsByHotelId(id);
                    hotel.setRooms(rooms);

                    return hotel;
                }
            }
        }
        return null; // Hotel not found
    }

    public List<Room> getRoomsByHotelId(int hotelId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE HotelId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                // Create an instance of RoomDAO
                RoomDAO roomDAO = new RoomDAO(connection);
                while (resultSet.next()) {
                    Room room = roomDAO.extractRoomFromResultSet(resultSet);
                    // Set the hotel object
                    room.setHotel(getHotelById(hotelId));
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }
    
    private Hotel extractHotelFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        String name = resultSet.getString("Name");
        String address = resultSet.getString("Address");

        return new Hotel(id, name, address);
    }
    
    //Update
    public boolean updateHotel(Hotel hotel) throws SQLException {
        String sql = "UPDATE Hotel SET Name = ?, Address = ? WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getAddress());
            statement.setInt(3, hotel.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                // Fetch and set the list of rooms associated with the hotel
                List<Room> rooms = getRoomsByHotelId(hotel.getId());
                hotel.setRooms(rooms);
                return true;
            } else {
                return false;
            }
        }
    }

    //Delete
    public boolean deleteHotel(int id) throws SQLException {
        String sql = "DELETE FROM Hotel WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returns true if at least one row was deleted
        }
    }
}
