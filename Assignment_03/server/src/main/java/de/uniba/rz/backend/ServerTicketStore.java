package de.uniba.rz.backend;

import java.util.ArrayList;
import java.util.List;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.Type;

public class ServerTicketStore implements TicketStore {

	private static int nextTicketId = 0;
	public static List<Ticket> ticketList = new ArrayList<>();
	//Ticket dummyTicket = new Ticket(1, "a", "a", "a", Type.BUG, Priority.CRITICAL);

	@Override
	// call this function to store the tickets
	public Ticket storeNewTicket(String reporter, String topic, String description, Type type, Priority priority) {
		System.out.println("Creating new Ticket from Reporter SAY: " + reporter + " with the topic \"" + topic + "\"");
		Ticket newTicket = new Ticket(++nextTicketId, reporter, topic, description, type, priority);
		ticketList.add(newTicket);
		System.out.println("hihi" + ticketList.toString());
		return newTicket;
	}

	@Override
	// this function can be used to update the tickets
	public void updateTicketStatus(int ticketId, Status newStatus) {
		for (Ticket ticket : ticketList) {
			if (ticket.getId() == ticketId) {
				ticket.setStatus(newStatus);
			}
		}
	}

	@Override
	public List<Ticket> getAllTickets() {
		//ticketList.add(dummyTicket);
		return ticketList;
	}
}
