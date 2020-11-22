package de.uniba.rz.Get;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import de.uniba.rz.entities.Ticket;
import de.uniba.rz.stubs.TicketsService;

@Path("search")
public class Search {

	
	@GET
	public List<Ticket> getTicketsbyType(@DefaultValue("") @QueryParam("name") String name,
			@DefaultValue("notype") @QueryParam("type") String typestr, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		
		/*
		 * if (Objects.equal(typestr, "notype"))// default value, just apply name filter
		 * return TicketsService.getTickets(name); else { typestr=typestr.toUpperCase();
		 * try { Type type = Type.valueOf(typestr);// if it is a valid type return
		 * TicketsService.getTickets(name, type); } catch (IllegalArgumentException
		 * e){// if it is not a valid type, return empty list return
		 * Collections.emptyList(); } }//else
		 */
		return TicketsService.getTickets(name, typestr, offset, limit);
	}
	
}
