package com.mycompany.resources;

import com.mycompany.dao.CustomerDAO;
import com.mycompany.main.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Logger logger = LoggerFactory.getLogger(CustomerResource.class);
    private CustomerDAO customerDAO;

    public CustomerResource(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
    
    public CustomerResource() {
    }

    @GET
    public Response getAllCustomers() {
        logger.info("Aceess the getAllCustomers method.");
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            logger.info("All customers fetched successfully.");
            return Response.ok(customers).build();
        } catch (SQLException e) {
            logger.error("Error fetching all customers: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching all customers. Please try again later.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the getCustomerById method.");
        try {
            Customer customer = customerDAO.getCustomerById(id);
            if (customer != null) {
                logger.info("Customer details fetched successfully.");
                return Response.ok(customer).build();
            } else {
                logger.info("Customer not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error fetching customer with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching customer. Please try again later.")
                    .build();
        }
    }

    @POST
    public Response addCustomer(Customer customer) {
        logger.info("Aceess the addCustomer method.");
        try {
            Customer newCustomer = customerDAO.addCustomer(customer);
            logger.info("Customer details have been added successfully.");
            return Response.status(Response.Status.CREATED).entity(newCustomer).build();
        } catch (SQLException e) {
            logger.error("Error adding customer: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding customer. Please try again later.")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        customer.setId(id); // Ensure the customer ID is set correctly
        logger.info("Aceess the updateCustomer method.");
        try {
            boolean isUpdated = customerDAO.updateCustomer(customer);
            if (isUpdated) {
                logger.info("Customer details have been updated successfully.");
                return Response.ok(customer).build(); // Return the updated customer
            } else {
                logger.info("Customer not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found.").build();
            }
        } catch (SQLException e) {
            // Log the exception and return an error response
            logger.error("Error updating customer: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating customer.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the deleteCustomer method.");
        try {
            boolean deleted = customerDAO.deleteCustomer(id);
            if (deleted) {
                logger.info("Customer details have been deleted successfully.");
                return Response.ok("Customer details have been deleted successfully.").build();
            } else {
                logger.info("Customer not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error deleting customer with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting customer. Please try again later.")
                    .build();
        }
    }
}
