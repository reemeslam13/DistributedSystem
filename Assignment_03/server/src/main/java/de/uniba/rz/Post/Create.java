package de.uniba.rz.Post;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.uniba.rz.entities.RestTicket;
import de.uniba.rz.stubs.TicketsService;

@Path("tickets")
public class Create {

    @POST
    public Response createTicket(RestTicket newTicket, @Context UriInfo uriInfo) {
        RestTicket ticket = TicketsService.addTicket(newTicket);

        if (ticket == null) {
            throw new WebApplicationException("Invalid request body" , 400);
        }
        System.out.println("* Create New RestTicket: #"+String.valueOf(ticket.getId())+" : "+ticket.getTopic());
        UriBuilder path = uriInfo.getAbsolutePathBuilder();
        path.path(Integer.toString(ticket.getId()));
        return Response.created(path.build()).build();
    }
}
