package com.mycompany.main;

import com.fasterxml.jackson.annotation.JsonView;

public class Room {
    private int id; // Primary key
    private String type;
    private int hotelId; // Foreign key
    private Hotel hotel;

    // Constructors
    public Room() {}

    public Room(int id, String type, int hotelId) {
        this.id = id;
        this.type = type;
        this.hotelId = hotelId;
    }
    
    public Room(int id, String type, int hotelId, Hotel hotel) {
        this.id = id;
        this.type = type;
        this.hotelId = hotelId;
        this.hotel = hotel;
    }

    // Getters and Setters
    @JsonView(Views.RoomAndHotel.class)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @JsonView(Views.RoomAndHotel.class)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    @JsonView(Views.IgnoreHotelId.class)
    public int getHotelId() {
        return hotelId;
    }
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    
    @JsonView(Views.OnlyForRoom.class)
    public Hotel getHotel() {
        return hotel;
    }
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
