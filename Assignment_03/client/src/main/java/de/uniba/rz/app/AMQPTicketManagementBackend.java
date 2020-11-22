package de.uniba.rz.app;

import de.uniba.rz.entities.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import methods.Sharedmethods;
import methods.Configuration;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class AMQPTicketManagementBackend implements TicketManagementBackend {
	private String hostname;
	private String queuename;
	private final int MAXTRY = 5;
	private final String LOGFILE = "clientlogfile.log";
	private int trynumber = 0;// if fail to connect after 5 tries, connection will get closed

	HashMap<Integer, Ticket> serverTicketStore = new HashMap<>();
	AtomicInteger nextId = new AtomicInteger(1);

	public AMQPTicketManagementBackend(String hostname, String queuename) {

		this.hostname = hostname;
		this.queuename = queuename;
	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
			throws TicketException {
		Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
		serverTicketStore.put(newTicket.getId(), newTicket);

		sendMessage(newTicket.toString());
		return (Ticket) newTicket.clone();
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
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

	@Override
	public Ticket acceptTicket(int id) throws TicketException {
		Ticket ticketToModify = getTicketByIdInteral(id);
		if (ticketToModify.getStatus() != Status.NEW) {
			throw new TicketException(
					"Can not accept Ticket as it is currently in status " + ticketToModify.getStatus());
		}

		ticketToModify.setStatus(Status.ACCEPTED);
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
		return (Ticket) ticketToModify.clone();
	}

	@Override
	public void triggerShutdown() {

	}

	private final ConnectionFactory connFactory = new ConnectionFactory();

	public void sendMessage(String message) {

		// Note: Could be omitted because localhost is set be default
		connFactory.setHost(this.hostname);

		try (Connection connection = connFactory.newConnection();) {

			Channel channel = connection.createChannel();

			/*
			 * Note that the declared queue is automatically bound to the pre-declared
			 * (default) exchange of RabbitMQ. The address of the exchange is an empty
			 * string ("") and the type is always "direct". The name of the queue will be
			 * the binding key that is evaluated against the routing key determining an
			 * incoming message's destination. That implies that it is impossible to send a
			 * message directly to the queue from the client's side. The message always has
			 * to pass an exchange.
			 */
			Configuration c =new Configuration();
			boolean autodelete = c.getAMQPchannelProp(AMQPChannelProperty.AUTODELETE);
			boolean durable = c.getAMQPchannelProp(AMQPChannelProperty.DURABLE);
			boolean exclusive = c.getAMQPchannelProp(AMQPChannelProperty.EXCLUSIVE);
			try {
				channel.queueDeclarePassive(this.queuename);
			} catch (IOException e) {// channel does not exist, so create it
				channel = connection.createChannel();// queueDeclarePassive has closed the channel, so we need to
														// recreate it
				channel.queueDeclare(this.queuename, durable, exclusive, autodelete, null);
			}

			// Declare a server-named and temporary queue
			String replyQueue = channel.queueDeclare().getQueue(); // Create a random UUID as correlation id
			String uuid = UUID.randomUUID().toString();
			// Create properties
			BasicProperties basicProperties = new BasicProperties().builder().correlationId(uuid).replyTo(replyQueue)
					.build();
			// Send message to the default exchange ("") with the queueName as routing key
			channel.basicPublish("", this.queuename, basicProperties, message.getBytes());

		} catch (IOException e) {
			String msg = "AMQP Client IO Exception\n";
			msg = msg+e.getCause().getMessage();
			new Sharedmethods().log(LOGFILE,  msg);		
			
			return;

		} catch (TimeoutException e) {
			trynumber++;

			if (trynumber < MAXTRY)// retry this.sendMessage(message); else {
				System.out.println("Not Responding... Connection will close...");
			return;
		}

	}

}
