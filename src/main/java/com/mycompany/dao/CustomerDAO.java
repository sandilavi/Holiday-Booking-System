package com.mycompany.dao;

import com.mycompany.main.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final Connection connection;

    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    //Create
    public Customer addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer (Name, Email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }
            return customer;
        }
    }

    //Read
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("Id"));
                customer.setName(resultSet.getString("Name"));
                customer.setEmail(resultSet.getString("Email"));
                customers.add(customer);
            }
        }
        return customers;
    }

    //Read
    public Customer getCustomerById(int id) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractCustomerFromResultSet(resultSet);
                }
            }
        }
        return null; // Customer not found
    }
    
    private Customer extractCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("Id");
        String name = resultSet.getString("Name");
        String email = resultSet.getString("Email");

        return new Customer(id, name, email);
    }
    
    //Update
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET Name = ?, Email = ? WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setInt(3, customer.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Returns true if at least one row was updated
        }
    }

    //Delete
    public boolean deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returns true if at least one row was deleted
        }
    }
}
