package com.mycompany.resources;

import com.mycompany.dao.PaymentDAO;
import com.mycompany.main.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {
    private static final Logger logger = LoggerFactory.getLogger(PaymentResource.class);
    private PaymentDAO paymentDAO;

    public PaymentResource(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }
    
    public PaymentResource() {
    }

    @GET
    public Response getAllPayments() {
        logger.info("Aceess the getAllPayments method.");
        try {
            List<Payment> payments = paymentDAO.getAllPayments();
            logger.info("All payments fetched successfully.");
            return Response.ok(payments).build();
        } catch (SQLException e) {
            logger.error("Error fetching all payments: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching all payments. Please try again later.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the getPaymentById method.");
        try {
            Payment payment = paymentDAO.getPaymentById(id);
            if (payment != null) {
                logger.info("Payment details fetched successfully.");
                return Response.ok(payment).build();
            } else {
                logger.info("Payment not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Payment not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error fetching payment with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching payment. Please try again later.")
                    .build();
        }
    }

    @POST
    public Response addPayment(Payment payment) {
        logger.info("Aceess the addPayment method.");
        try {
            Payment newPayment = paymentDAO.addPayment(payment);
            logger.info("Payment details have been added successfully.");
            return Response.status(Response.Status.CREATED).entity(newPayment).build();
        } catch (SQLException e) {
            logger.error("Error adding payment: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding payment. Please try again later.")
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response updatePayment(@PathParam("id") int id, Payment payment) {
        payment.setId(id); // Ensure the payment ID is set correctly
        logger.info("Accessing the updatePayment method.");

        try {
            // Retrieve the existing payment details
            Payment existingPayment = paymentDAO.getPaymentById(id);
            if (existingPayment == null) {
                logger.info("Payment not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Payment not found.").build();
            }

            // Log the existing customer ID
            logger.info("Existing Customer ID: " + existingPayment.getCustomerId());

            // Set the customerId from the existing payment
            payment.setCustomerId(existingPayment.getCustomerId());
            logger.info("Set customer ID from existing payment: " + existingPayment.getCustomerId());

            boolean isUpdated = paymentDAO.updatePayment(payment);
            if (isUpdated) {
                // Fetch the updated payment details after the update
                Payment updatedPayment = paymentDAO.getPaymentById(id);

                if (updatedPayment != null) {
                    // Set the customer details from the updated payment
                    payment.setCustomer(updatedPayment.getCustomer());
                    logger.info("Customer details set from updated payment.");

                    logger.info("Payment details have been updated successfully.");
                    return Response.ok(updatedPayment).build(); // Return the updated payment
                } else {
                    logger.error("Failed to fetch updated payment details.");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch updated payment details.").build();
                }
            } else {
                logger.info("Payment not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Payment not found.").build();
            }
        } catch (SQLException e) {
            // Log the exception and return an error response
            logger.error("Error updating payment: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating payment.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePayment(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the deletePayment method.");
        try {
            boolean deleted = paymentDAO.deletePayment(id);
            if (deleted) {
                logger.info("Payment details have been deleted successfully.");
                return Response.ok("Payment details have been deleted successfully.").build();
            } else {
                logger.info("Payment not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Payment not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error deleting payment with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting payment. Please try again later.")
                    .build();
        }
    }
}
