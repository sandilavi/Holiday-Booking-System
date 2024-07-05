package com.mycompany.main;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Payment {
    private int id; // Primary key
    private int amount;
    private String paymentDate;
    private String paymentMethod;
    private int customerId; // Foreign key
    private Customer customer;

    // Constructors
    public Payment() {}

    public Payment(int id, int amount, String paymentDate, String paymentMethod, int customerId) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.customerId = customerId;
    }
    
    /*public Payment(int id, int amount, String paymentDate, String paymentMethod, int customerId, Customer customer) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.customerId = customerId;
        this.customer = customer;
    }*/

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
}
