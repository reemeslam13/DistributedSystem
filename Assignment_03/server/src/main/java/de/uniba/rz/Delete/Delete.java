package de.uniba.rz.Delete;


import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import de.uniba.rz.stubs.TicketsService;

@Path("tickets")
public class Delete {

    @DELETE
    @Path("{ticket-id}")
    public void deleteTicket(@PathParam("ticket-id") int id) {
        boolean deleted = TicketsService.deleteTicket(id);

        if (!deleted) {
            throw new WebApplicationException("No Ticket found with id: " + id, 404);
        }
    }
}
