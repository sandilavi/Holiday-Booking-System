package com.mycompany.resources;

import com.fasterxml.jackson.annotation.JsonView;
import com.mycompany.dao.RoomDAO;
import com.mycompany.main.Room;
import com.mycompany.main.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    private static final Logger logger = LoggerFactory.getLogger(RoomResource.class);
    private RoomDAO roomDAO;

    public RoomResource(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }
    
    public RoomResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForRoom.class)
    public Response getAllRooms() {
        logger.info("Aceess the getAllRooms method.");
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            logger.info("All rooms fetched successfully.");
            return Response.ok(rooms).build();
        } catch (SQLException e) {
            logger.error("Error fetching all rooms: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching all rooms. Please try again later.")
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForRoom.class)
    @Path("/{id}")
    public Response getRoomById(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the getRoomById method.");
        try {
            Room room = roomDAO.getRoomById(id);
            if (room != null) {
                logger.info("Room details fetched successfully.");
                return Response.ok(room).build();
            } else {
                logger.info("Room not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Room not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error fetching room with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching room. Please try again later.")
                    .build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForRoom.class)
    public Response addRoom(Room room) {
        logger.info("Aceess the addRoom method.");
        try {
            Room newRoom = roomDAO.addRoom(room);
            logger.info("Room details have been added successfully.");
            return Response.status(Response.Status.CREATED).entity(newRoom).build();
        } catch (SQLException e) {
            logger.error("Error adding room: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding room. Please try again later.")
                    .build();
        }
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForRoom.class)
    @Path("/{id}")
    public Response updateRoom(@PathParam("id") int id, Room room) {
        room.setId(id); // Ensure the room ID is set correctly
        logger.info("Access the updateRoom method.");
        try {
            // Retrieve the existing room details to get the hotelId
            Room existingRoom = roomDAO.getRoomById(id);
            if (existingRoom == null) {
                logger.info("Room not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Room not found.").build();
            }

            room.setHotelId(existingRoom.getHotelId()); // Set the hotelId from the existing room

            boolean isUpdated = roomDAO.updateRoom(room);
            if (isUpdated) {
                logger.info("Room details have been updated successfully.");
                return Response.ok(room).build(); // Return the updated room
            } else {
                logger.info("Room not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Room not found.").build();
            }
        } catch (SQLException e) {
            // Log the exception and return an error response
            logger.error("Error updating room: " + e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating room.").build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Views.OnlyForRoom.class)
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") int id) throws SQLException {
        logger.info("Aceess the deleteRoom method.");
        try {
            boolean deleted = roomDAO.deleteRoom(id);
            if (deleted) {
                logger.info("Room details have been deleted successfully.");
                return Response.ok("Room details have been deleted successfully.").build();
            } else {
                logger.info("Room not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Room not found.").build();
            }
        } catch (Exception e) {
            logger.error("Error deleting room with ID {}: {}", id, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting room. Please try again later.")
                    .build();
        }
    }
}
