package com.mycompany.resources;

import com.mycompany.dao.BookingDAO;
import com.mycompany.main.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {
    private static final Logger logger = LoggerFactory.getLogger(BookingResource.class);
    private BookingDAO bookingDAO;

    public BookingResource(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }
    
    public BookingResource() {
    }

    @GET
    public Response getAllBookings() {
        logger.info("Aceess the getAllBookings method.");
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            logger.info("All bookings fetched successfully.");
            return Response.ok(bookings).build();
        } catch (SQLException e) {
            logger.error("Error fetching all bookings: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching all bookings. Please try again later.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the getBookingById method.");
        try {
            Booking booking = bookingDAO.getBookingById(id);
            if (booking != null) {
                logger.info("Booking details fetched successfully.");
                return Response.ok(booking).build();
            } else {
                logger.info("Booking not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Booking not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error fetching booking with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching booking. Please try again later.")
                    .build();
        }
    }

    @POST
    public Response addBooking(Booking booking) {
        logger.info("Aceess the addBooking method.");
        try {
            Booking newBooking = bookingDAO.addBooking(booking);
            logger.info("Booking details have been added successfully.");
            return Response.status(Response.Status.CREATED).entity(newBooking).build();
        } catch (SQLException e) {
            logger.error("Error adding booking: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding booking. Please try again later.")
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id") int id, Booking booking) {
        try {
            booking.setId(id);
            boolean updated = bookingDAO.updateBooking(booking);
            if (updated) {
                // Fetch the updated booking details
                Booking updatedBooking = bookingDAO.getBookingById(id);
                return Response.ok(updatedBooking).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating booking").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the deleteBooking method.");
        try {
            boolean deleted = bookingDAO.deleteBooking(id);
            if (deleted) {
                logger.info("Booking details have been deleted successfully.");
                return Response.ok("Booking details have been deleted successfully.").build();
            } else {
                logger.info("Booking not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Booking not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error deleting booking with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting booking. Please try again later.")
                    .build();
        }
    }
}
