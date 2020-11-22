package de.uniba.rz.Put;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import de.uniba.rz.entities.RestTicket;
import de.uniba.rz.stubs.TicketsService;


//In this class method PUT is implemented by calling TicketsService.updateTicket method
@Path("tickets")
public class Update {

    @PUT
    @Path("{ticket-id}")
    public Response updateTicket(@PathParam("ticket-id") int id, RestTicket updatedTicket) {
        RestTicket ticket = TicketsService.getTicket(id);
        
        if (ticket == null) {
            throw new WebApplicationException("No ticket found with id: " + id, 404);
        }

        if (updatedTicket == null) {
            throw new WebApplicationException("Invalid request body", 400);
        }

        RestTicket resultTicket = TicketsService.updateTicket(id, updatedTicket);
        System.out.print("* Update Ticket# "+String.valueOf(ticket.getId()));
        System.out.println(", New Status: "+ticket.getStatus().toString());

        return Response.ok().entity(resultTicket).build();
    }
}
