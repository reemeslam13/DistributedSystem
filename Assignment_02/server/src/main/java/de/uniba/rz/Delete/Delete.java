package de.uniba.rz.Delete;


import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import de.uniba.rz.stubs.TicketsService;



@Path("cats")
public class Delete {

    @DELETE
    @Path("{cat-id}")
    public void deleteCat(@PathParam("cat-id") int id) {
        boolean deleted = TicketsService.deleteTicket(id);

        if (!deleted) {
            throw new WebApplicationException("No Ticket found with id: " + id, 404);
        }
    }
}
