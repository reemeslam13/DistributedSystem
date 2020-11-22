package de.uniba.rz.app;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.TicketException;
import de.uniba.rz.entities.Type;

public class RESTTicketManagementBackend implements TicketManagementBackend {
	HashMap<Integer, Ticket> serverTicketStore = new HashMap<>();
	AtomicInteger nextId = new AtomicInteger(1);

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority) {
		Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
		serverTicketStore.put(newTicket.getId(), newTicket);

		// System.out.print(newTicket.toJSON());
		sendTicket(newTicket);

		return (Ticket) newTicket.clone();
	}

	public void sendTicket(Ticket ticket) {
		Client client = ClientBuilder.newClient();

		Entity<Ticket> entity = Entity.json(ticket); // marshal to json or xml
		System.out.println(entity.toString());

		Response response = (Response) client.target("http://localhost:9999").path("/tickets")
				.request(MediaType.APPLICATION_JSON) // Alternative: MediaType.APPLICATION_XML
				.header("Content-Type", "application/json") // Further headers possible
				.post(entity); // Alternatives: put()
		// TODO: check possible status codes and process the response
		System.out.println("Status: " + response.getStatus() + "\nLocation: " + response.getLocation());
	}

	public void ReadAllTickets() {

		Client client = ClientBuilder.newClient();
		Response response = (Response) client.target("http://localhost:9999").path("/tickets") 
				.request(MediaType.APPLICATION_JSON) 
				.get(); 
		List<Ticket> returnTickets = null;
		
		if (response.getStatus() == 200) {
			returnTickets = response.readEntity(new GenericType<List<Ticket>>() {
			});

			returnTickets.forEach((ticket) -> {
				serverTicketStore.put(ticket.getId(), ticket);

			});

		} else {

		} // else
	}
	public List<Ticket> filterTickets(String name) {

		Client client = ClientBuilder.newClient();
		
		Response response = (Response) client.target("http://localhost:9999").path("/search") // http://localhost:9999/cats/5
				.queryParam("name", name)
				.request(MediaType.APPLICATION_JSON) 
				
				.get(); 
		List<Ticket> returnTickets = null;
		
		if (response.getStatus() == 200) {
			returnTickets = response.readEntity(new GenericType<List<Ticket>>() {
			});

		} else {

		} // else
		return returnTickets;
	}
	public List<Ticket> filterTickets(String name, Type type) {

		Client client = ClientBuilder.newClient();
		
		Response response = (Response) client.target("http://localhost:9999").path("/search") // http://localhost:9999/cats/5
				.queryParam("name", name)
				.queryParam("type", type)
				.request(MediaType.APPLICATION_JSON) 
				
				.get(); 
		List<Ticket> returnTickets = null;
		
		if (response.getStatus() == 200) {
			returnTickets = response.readEntity(new GenericType<List<Ticket>>() {
			});

		} else {

		} // else
		return returnTickets;
	}
	public List<Ticket> filterTickets(String name, Type type, int offset, int limit) {

		Client client = ClientBuilder.newClient();
		
		Response response = (Response) client.target("http://localhost:9999").path("/search") // http://localhost:9999/cats/5
				.queryParam("name", name)
				.queryParam("type", type)
				.queryParam("offset", offset)
				.queryParam("limit", limit)
				.request(MediaType.APPLICATION_JSON) 
				
				.get(); 
		List<Ticket> returnTickets = null;
		
		if (response.getStatus() == 200) {
			returnTickets = response.readEntity(new GenericType<List<Ticket>>() {
			});

		} else {

		} // else
		return returnTickets;
	}
	@Override
	public List<Ticket> getAllTickets() throws TicketException {

		ReadAllTickets();
		return serverTicketStore.entrySet().stream().map(entry -> (Ticket) entry.getValue().clone())
				.collect(Collectors.toList());
	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		if (!serverTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}

		return serverTicketStore.get(id);
	}

	private void updateTicket(Ticket modifiedTicket) {
		Client client = ClientBuilder.newClient();
		String ticketid = Integer.toString(modifiedTicket.getId());
		Entity<Ticket> entity = Entity.json(modifiedTicket); 
		Response response = (Response) client.target("http://localhost:9999").path("/tickets/" + ticketid)
				.request(MediaType.APPLICATION_JSON) 
				.header("Content-Type", "application/json") 
				.put(entity); 		
		System.out.println("Status: " + response.getStatus() + "\nLocation: " + response.getLocation());
	}

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.NEW) {
			throw new TicketException(
					"Can not accept Ticket as it is currently in status " + ticketToModify.getStatus());
		}

		ticketToModify.setStatus(Status.ACCEPTED);
		updateTicket(ticketToModify);
		return (Ticket) ticketToModify.clone();
	}

	@Override
	public Ticket rejectTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.NEW) {
			throw new TicketException(
					"Can not reject Ticket as it is currently in status " + ticketToModify.getStatus());
		}

		ticketToModify.setStatus(Status.REJECTED);
		updateTicket(ticketToModify);
		// sendRequestToUpdate((Ticket) ticketToModify.clone());
		return (Ticket) ticketToModify.clone();
	}

	private Ticket getTicketByIdInteral(int id) throws TicketException {
		if (!serverTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}

		return serverTicketStore.get(id);
	}

	@Override
	public Ticket closeTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.ACCEPTED) {
			throw new TicketException(
					"Can not close Ticket as it is currently in status " + ticketToModify.getStatus());
		}
		ticketToModify.setStatus(Status.CLOSED);
		updateTicket(ticketToModify);
		return (Ticket) ticketToModify.clone();
	}

	@Override
	public void triggerShutdown() {

	}
	public List<Ticket> getTicketsByName(String name){
		List<Ticket> result = filterTickets(name);
		return result;
	}
	public List<Ticket> getTicketsByNameAndType(String name, Type type){
		List<Ticket> result = filterTickets(name,type);
		return result;
	}
	public List<Ticket> searchTickets(String name, Type type, int offset, int limit){
		List<Ticket> result = filterTickets(name,type,offset,limit);
		return result;
	}
	

}
