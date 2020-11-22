package de.uniba.rz.Get;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.stubs.TicketsService;

@Path("tickets")
public class ReadAll {

    @GET
    public List<Ticket> getTickets() {
        return TicketsService.getTickets();
    }
}
