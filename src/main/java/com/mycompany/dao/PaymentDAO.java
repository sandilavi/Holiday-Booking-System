package com.mycompany.dao;

import com.mycompany.main.Payment;
import com.mycompany.main.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private final Connection connection;

    public PaymentDAO(Connection connection) {
        this.connection = connection;
    }

    //Create
    public Payment addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment (Amount, PaymentDate, PaymentMethod, CustomerID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, payment.getAmount());
            statement.setString(2, payment.getPaymentDate());
            statement.setString(3, payment.getPaymentMethod());
            statement.setInt(4, payment.getCustomerId());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getInt(1));
                }
            }
            
            // Fetch the customer details
            CustomerDAO customerDAO = new CustomerDAO(connection);
            Customer customer = customerDAO.getCustomerById(payment.getCustomerId());
            payment.setCustomer(customer);
        
            return payment;
        }
    }

    //Read
    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = extractPaymentFromResultSet(resultSet);
                    payments.add(payment);
                }
            }
        return payments;
    }

    //Read
    public Payment getPaymentById(int id) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractPaymentFromResultSet(resultSet);
                }
            }
        }
        return null; // Payment not found
    }
    
    public Payment extractPaymentFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        int amount = resultSet.getInt("Amount");
        String paymentDate = resultSet.getString("PaymentDate");
        String paymentMethod = resultSet.getString("PaymentMethod");
        int customerId = resultSet.getInt("CustomerId");

        // Create a payment object with customerId
        Payment payment = new Payment(id, amount, paymentDate, paymentMethod, customerId);

        // Fetch and set the Customer object
        CustomerDAO customerDAO = new CustomerDAO(connection);
        Customer customer = customerDAO.getCustomerById(customerId);
        payment.setCustomer(customer);

        return payment;
    }
    
    public List<Payment> getPaymentsByCustomerId(int customerId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE CustomerId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Payment payment = extractPaymentFromResultSet(resultSet);
                    payments.add(payment);
                }
            }
        }
        return payments;
    }
        
    //Update
    public boolean updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE Payment SET Amount = ?, PaymentDate = ?, PaymentMethod = ? WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, payment.getAmount());
            statement.setString(2, payment.getPaymentDate());
            statement.setString(3, payment.getPaymentMethod());
            statement.setInt(4, payment.getId());

            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                // Fetch the customer details
                CustomerDAO customerDAO = new CustomerDAO(connection);
                Customer customer = customerDAO.getCustomerById(payment.getCustomerId());
                payment.setCustomer(customer);
            }
            
            return rowsUpdated > 0; // Returns true if at least one row was updated
        }
    }

    //Delete
    public boolean deletePayment(int id) throws SQLException {
        String sql = "DELETE FROM Payment WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returns true if at least one row was deleted
        }
    }
}
