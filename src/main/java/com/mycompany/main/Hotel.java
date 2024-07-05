package com.mycompany.main;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String address;
    private List<Room> rooms;

    // Constructors
    public Hotel() {}

    public Hotel(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @JsonView(Views.RoomAndHotel.class)
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    
    @JsonView(Views.OnlyForHotel.class)
    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
