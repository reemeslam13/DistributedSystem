package de.uniba.rz.Put;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.stubs.TicketsService;



@Path("tickets")
public class Update {

    @PUT
    @Path("{ticket-id}")
    public Response updateTicket(@PathParam("ticket-id") int id, Ticket updatedTicket) {
        Ticket ticket = TicketsService.getTicket(id);
        System.out.println("******* Ticket: "+ticket.toString());
        if (ticket == null) {
            throw new WebApplicationException("No ticket found with id: " + id, 404);
        }

        if (updatedTicket == null) {
            throw new WebApplicationException("Invalid request body", 400);
        }

        Ticket resultTicket = TicketsService.updateTicket(id, updatedTicket);
        System.out.println("******* Ticket Result: "+ticket.toString());

        return Response.ok().entity(resultTicket).build();
    }
}
