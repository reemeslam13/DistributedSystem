package de.uniba.rz.Get;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.stubs.TicketsService;

@Path("tickets")
public class ReadSingle {

    @GET
    @Path("{ticket-id}")
    public Ticket getTicket(@PathParam("ticket-id") int id) {
        Ticket ticket = TicketsService.getTicket(id);

        if (ticket == null) {
            throw new WebApplicationException("No ticket found with id: " + id, 404);
        }
        return ticket;
    }
}
