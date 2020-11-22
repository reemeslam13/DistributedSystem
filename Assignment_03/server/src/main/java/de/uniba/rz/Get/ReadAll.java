package de.uniba.rz.Get;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.uniba.rz.entities.RestTicket;
import de.uniba.rz.stubs.TicketsService;

@Path("tickets")
public class ReadAll {

    @GET
    public List<RestTicket> getTickets() {
        return TicketsService.getTickets();//*return also status code
    }
}
