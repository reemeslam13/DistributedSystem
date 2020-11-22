package de.uniba.rz.stubs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.base.Objects;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.Type;

public class TicketsService {
	private static final List<Ticket> TICKETS = new ArrayList<>();

	static {
		List<Ticket> tickets = Arrays
				.asList(new Ticket(1, "Azar", "Ticket1", "Desc", Type.QUESTION, Priority.MINOR, Status.NEW));
		TICKETS.addAll(tickets);
	}

	public static List<Ticket> getTickets() {
		return TICKETS;
	}

	public static List<Ticket> getTickets(String text, String typestr, int offset, int limit) {
		List<Ticket> paginatedTickets = new ArrayList<Ticket>();
		if (Objects.equal(typestr, "notype"))// default value, just apply name filter
			paginatedTickets = getTickets(text);
		else {
			try {
				typestr = typestr.toUpperCase();
				Type type = Type.valueOf(typestr);// if it is a valid type
				paginatedTickets = getTickets(text,type);
				//System.out.print("*** here in try ***");
			} // try

			catch (IllegalArgumentException e) {// if it is not a valid type, return empty list return
				//System.out.print("*** here in catch ***");
				return Collections.emptyList();
			}
		} // else
		if (limit > 0 && offset > 0) {
			if (offset + limit-1 > paginatedTickets.size()) {
				//System.out.print("*** here in if ***");
			
				return Collections.emptyList();}
			else {
				//System.out.print("*** here in else ***");
				return paginatedTickets.subList(offset-1, offset + limit-1);}

		} else
			//System.out.print("*** here in last else ***");
			{return paginatedTickets;}
	}

	public static List<Ticket> getTickets(String text) {

		List<Ticket> ticketlist = TICKETS.stream().filter(t -> t.containstext(text)).collect(Collectors.toList());
		Collections.sort(ticketlist, new Comparator<Ticket>() {
			@Override
			public int compare(Ticket t1, Ticket t2) {
				return t1.getPriority().compareTo(t2.getPriority());
			}
		});
		return ticketlist;
	}

	public static List<Ticket> getTickets(String text, Type type) {
		List<Ticket> ticketlist = TICKETS.stream().filter(t -> t.containstext(text) && t.getType() == type)
				.collect(Collectors.toList());

		Collections.sort(ticketlist, new Comparator<Ticket>() {
			@Override
			public int compare(Ticket t1, Ticket t2) {
				return t1.getPriority().compareTo(t2.getPriority());
			}
		});
		return ticketlist;
	}

	public static Ticket getTicket(int id) {
		return TICKETS.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}

	public static boolean deleteTicket(int id) {
		Ticket ticket = getTicket(id);
		return TICKETS.remove(ticket);
	}

	public static Ticket addTicket(Ticket newTicket) {
		if (newTicket == null) {
			return null;
		}
		int highestId = TICKETS.stream().map(Ticket::getId).max(Comparator.naturalOrder()).orElse(0);
		newTicket.setId(highestId + 1);
		TICKETS.add(newTicket);

		return newTicket;
	}

	public static Ticket updateTicket(int id, Ticket updatedTicket) {
		Ticket ticket = getTicket(id);

		if (ticket == null || updatedTicket == null) {
			return null;
		}
		Optional.ofNullable(updatedTicket.getReporter()).ifPresent(d -> ticket.setReporter(d));
		Optional.ofNullable(updatedTicket.getTopic()).ifPresent(d -> ticket.setTopic(d));
		Optional.ofNullable(updatedTicket.getDescription()).ifPresent(d -> ticket.setDescription(d));
		Optional.ofNullable(updatedTicket.getType()).ifPresent(d -> ticket.setType(d));
		Optional.ofNullable(updatedTicket.getPriority()).ifPresent(d -> ticket.setPriority(d));
		Optional.ofNullable(updatedTicket.getStatus()).ifPresent(d -> ticket.setStatus(d));

		return ticket;
	}
}
