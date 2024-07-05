package com.mycompany.resources;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycompany.dao.HotelDAO;
import com.mycompany.main.Hotel;
import com.mycompany.main.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/hotels")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HotelResource {
    private static final Logger logger = LoggerFactory.getLogger(HotelResource.class);
    private HotelDAO hotelDAO;

    public HotelResource(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }
    
    public HotelResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForHotel.class)
    public Response getAllHotels() {
        logger.info("Aceess the getAllHotels method.");
        try {
            List<Hotel> hotels = hotelDAO.getAllHotels();
            logger.info("All hotels fetched successfully.");
            return Response.ok(hotels).build();
        } catch (SQLException e) {
            logger.error("Error fetching all hotels: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching all hotels. Please try again later.")
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForHotel.class)
    @Path("/{id}")
    public Response getHotelById(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the getHotelById method.");
        try {
            Hotel hotel = hotelDAO.getHotelById(id);
            if (hotel != null) {
                logger.info("Hotel details fetched successfully.");
                return Response.ok(hotel).build();
            } else {
                logger.info("Hotel not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Hotel not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error fetching hotel with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching hotel. Please try again later.")
                    .build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForHotel.class)
    public Response addHotel(Hotel hotel) {
        logger.info("Aceess the addHotel method.");
        try {
            Hotel newHotel = hotelDAO.addHotel(hotel);
            logger.info("Hotel details have been added successfully.");
            return Response.status(Response.Status.CREATED).entity(newHotel).build();
        } catch (SQLException e) {
            logger.error("Error adding hotel: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding hotel. Please try again later.")
                    .build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForHotel.class)
    @Path("/{id}")
    public Response updateHotel(@PathParam("id") int id, Hotel hotel) {
        hotel.setId(id); // Ensure the hotel ID is set correctly
        logger.info("Aceess the updateHotel method.");
        try {
            boolean isUpdated = hotelDAO.updateHotel(hotel);
            if (isUpdated) {
                logger.info("Hotel details have been updated successfully.");
                return Response.ok(hotel).build(); // Return the updated hotel
            } else {
                logger.info("Hotel not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Hotel not found.").build();
            }
        } catch (SQLException e) {
            // Log the exception and return an error response
            logger.error("Error updating hotel: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating hotel.").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForHotel.class)
    @Path("/{id}")
    public Response deleteHotel(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the deleteHotel method.");
        try {
            boolean deleted = hotelDAO.deleteHotel(id);
            if (deleted) {
                logger.info("Hotel details have been deleted successfully.");
                return Response.ok("Hotel details have been deleted successfully.").build();
            } else {
                logger.info("Hotel not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Hotel not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error deleting hotel with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting hotel. Please try again later.")
                    .build();
        }
    }
}
