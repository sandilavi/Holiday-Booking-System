package com.mycompany.main;

import com.mycompany.dao.PaymentDAO;
import com.mycompany.dao.BookingDAO;
import com.mycompany.dao.CustomerDAO;
import com.mycompany.dao.HotelDAO;
import com.mycompany.dao.RoomDAO;
import com.mycompany.resources.PaymentResource;
import com.mycompany.resources.BookingResource;
import com.mycompany.resources.CustomerResource;
import com.mycompany.resources.HotelResource;
import com.mycompany.resources.RoomResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/api/";

    public static void main(String[] args) {
        try {
            // Check database connection before starting the server
            if (checkDatabaseConnection()) {
                // Insert data when the application starts
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/holiday_booking_system", "root", "1234");

                // Create DAO instances
                CustomerDAO customerDAO = new CustomerDAO(connection);
                HotelDAO hotelDAO = new HotelDAO(connection);
                RoomDAO roomDAO = new RoomDAO(connection);
                BookingDAO bookingDAO = new BookingDAO(connection);
                PaymentDAO paymentDAO = new PaymentDAO(connection);

                // Create Resource instances
                CustomerResource customerResource = new CustomerResource(customerDAO);
                HotelResource hotelResource = new HotelResource(hotelDAO);
                RoomResource roomResource = new RoomResource(roomDAO);
                BookingResource bookingResource = new BookingResource(bookingDAO);
                PaymentResource paymentResource = new PaymentResource(paymentDAO);

                // Start the server with resource configuration
                final HttpServer server = startServer(customerResource, hotelResource, roomResource, bookingResource, paymentResource);
                System.out.println("Holiday Booking System API started.");
                System.out.println("---------------------------------");

                // Keep the server running until terminated
                Thread.currentThread().join();
            } else {
                System.err.println("Failed to connect to the database. Exiting.");
            }
        } catch (Exception e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    public static HttpServer startServer(CustomerResource customerResource, HotelResource hotelResource,  RoomResource roomResource,  BookingResource bookingResource,  PaymentResource paymentResource) {
        final ResourceConfig config = new ResourceConfig()
            .register(customerResource) // Register CustomerResource
            .register(hotelResource) // Register HotelResource
            .register(roomResource) // Register RoomResource
            .register(bookingResource) // Register BookingResource
            .register(paymentResource) // Register PaymentResource    
            .register(JacksonFeature.class); // Register JacksonFeature for JSON support

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static boolean checkDatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/holiday_booking_system", "root", "1234");
            connection.close(); // Close the connection after checking
            System.out.println("Database connection successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return false;
        }
    }
}
