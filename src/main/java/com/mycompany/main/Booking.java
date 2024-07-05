package com.mycompany.main;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Booking {
    private int id; // Primary key
    private String startDate;
    private String endDate;
    private int customerId; // Foreign key
    private Customer customer;
    private int roomId; // Foreign key
    private Room room;

    // Constructors
    public Booking() {}

    public Booking(int id, String startDate, String endDate, int customerId, int roomId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
        this.roomId = roomId;
    }
    
    /*public Booking(int id, String startDate, String endDate, int customerId, Customer customer, int roomId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
        this.customer = customer;
        this.roomId = roomId;
    }*/

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
}
