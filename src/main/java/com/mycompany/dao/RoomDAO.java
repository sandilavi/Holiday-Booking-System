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

public class RoomDAO {
    private final Connection connection;

    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    //Create
    public Room addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO Room (Type, HotelId) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, room.getType());
            statement.setInt(2, room.getHotelId());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setId(generatedKeys.getInt(1));
                }
            }
            
        // Fetch and set the Hotel object
        HotelDAO hotelDAO = new HotelDAO(connection);
        Hotel hotel = hotelDAO.getHotelById(room.getHotelId());
        room.setHotel(hotel);
        
        return room;
        }
    }

    //Read
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT Room.*, Hotel.name AS hotelName, Hotel.address AS hotelAddress " +
                 "FROM Room " +
                 "LEFT JOIN Hotel ON Room.hotelId = Hotel.id";
        try (PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Room room = extractRoomFromResultSet(resultSet);
                rooms.add(room);
            }
        }
    return rooms;
    }

    //Read
    public Room getRoomById(int id) throws SQLException {
        String sql = "SELECT * FROM Room WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractRoomFromResultSet(resultSet);
                }
            }
        }
        return null; // Room not found
    }
    
    private Hotel getHotelById(int hotelId) throws SQLException {
    String sql = "SELECT * FROM Hotel WHERE Id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, hotelId);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return new Hotel(
                    resultSet.getInt("Id"),
                    resultSet.getString("Name"),
                    resultSet.getString("Address")
                );
            }
        }
    }
    return null; // Hotel not found
}

    public Room extractRoomFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        String type = resultSet.getString("Type");
        int hotelId = resultSet.getInt("HotelId");

        // Fetch the hotel details
        Hotel hotel = getHotelById(hotelId);

        return new Room(id, type, hotelId, hotel);
    }

    public List<Room> getRoomsByHotelId(int hotelId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE HotelId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, hotelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Room room = extractRoomFromResultSet(resultSet);
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }

    //Update
    public boolean updateRoom(Room room) throws SQLException {
        String sql = "UPDATE Room SET Type = ? WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getType());
            statement.setInt(2, room.getId());

            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                // Fetch and set the Hotel object
                HotelDAO hotelDAO = new HotelDAO(connection);
                Hotel hotel = hotelDAO.getHotelById(room.getHotelId());
                room.setHotel(hotel);
            }
            
            return rowsUpdated > 0; // Returns true if at least one row was updated
        }
    }

    //Delete
    public boolean deleteRoom(int id) throws SQLException {
        String sql = "DELETE FROM Room WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returns true if at least one row was deleted
        }
    }
}
