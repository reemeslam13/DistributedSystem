package de.uniba.rz.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.google.common.base.Objects;
import de.uniba.rz.entities.RestTicket;
import de.uniba.rz.entities.Type;

public class TicketsService {
	private static final List<RestTicket> TICKETS = new ArrayList<>();//List of tickets stored in server

	public static List<RestTicket> getTickets() {
		return TICKETS;
	}

	/**
	 * Azar:
	 * 
	 * @param text:    search string
	 * @param typestr: if search by RestTicket type
	 * @param offset
	 * @param limit
	 * @return: List of tickets after applying filter There are different
	 *          implementations of method getTickets depending on the input
	 *          arguments if there are offsets and limit values, the result will be
	 *          paginated
	 */
	public static List<RestTicket> getTickets(String text, String typestr, int offset, int limit) {
		List<RestTicket> paginatedTickets = new ArrayList<RestTicket>();
		if (Objects.equal(typestr, "notype"))// default value, just apply name filter
			paginatedTickets = getTickets(text);
		else {
			try {
				typestr = typestr.toUpperCase();
				Type type = Type.valueOf(typestr);// if it is a valid type
				paginatedTickets = getTickets(text, type);
			} // try

			catch (IllegalArgumentException e) {// if it is not a valid type, return empty list
				return Collections.emptyList();
			}
		} // else
		if (limit > 0 && offset > 0) {
			if (offset + limit - 1 > paginatedTickets.size()) {
				return Collections.emptyList();
			} else {
				return paginatedTickets.subList(offset - 1, offset + limit - 1);
			}

		} else {
			return paginatedTickets;
		}
	}

	public static List<RestTicket> getTickets(String text) {

		List<RestTicket> ticketlist = TICKETS.stream().filter(t -> t.containstext(text))
				.collect(Collectors.toList());
		Collections.sort(ticketlist, new Comparator<RestTicket>() {
			//To sort the list by Priority
			@Override
			public int compare(RestTicket t1, RestTicket t2) {
				return t1.getPriority().compareTo(t2.getPriority());
			}
		});
		return ticketlist;
	}

	public static List<RestTicket> getTickets(String text, Type type) {
		List<RestTicket> ticketlist = TICKETS.stream().filter(t -> t.containstext(text) && t.getType() == type)
				.collect(Collectors.toList());

		Collections.sort(ticketlist, new Comparator<RestTicket>() {
			@Override
			public int compare(RestTicket t1, RestTicket t2) {
				return t1.getPriority().compareTo(t2.getPriority());
			}
		});
		return ticketlist;
	}

	public static RestTicket getTicket(int id) {
		return TICKETS.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
	}

	public static boolean deleteTicket(int id) {
		RestTicket ticket = getTicket(id);
		return TICKETS.remove(ticket);
	}

	public static RestTicket addTicket(RestTicket newTicket) {
		if (newTicket == null) {
			return null;
		}
		int highestId = TICKETS.stream().map(RestTicket::getId).max(Comparator.naturalOrder()).orElse(0);
		newTicket.setId(highestId + 1);
		TICKETS.add(newTicket);

		return newTicket;
	}

	public static RestTicket updateTicket(int id, RestTicket updatedTicket) {
		RestTicket ticket = getTicket(id);

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
