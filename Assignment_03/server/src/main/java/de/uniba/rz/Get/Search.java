package de.uniba.rz.Get;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import de.uniba.rz.entities.RestTicket;
import de.uniba.rz.stubs.TicketsService;

@Path("search")
public class Search {

	
	@GET
	public List<RestTicket> getTicketsbyType(@DefaultValue("") @QueryParam("name") String name,
			@DefaultValue("notype") @QueryParam("type") String typestr, 
			@DefaultValue("1") @QueryParam("offset") int offset, @DefaultValue("2") @QueryParam("limit") int limit) {
		return TicketsService.getTickets(name, typestr, offset, limit);
	}
	
}
