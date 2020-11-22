package de.uniba.rz.app;

import de.uniba.rz.entities.*;
import methods.Reassemble;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UDPTicketManagementBackend implements TicketManagementBackend {
	String hostname;
	int port;
	int CHUNKSIZE = 1000;
	HashMap<Integer, Ticket> serverTicketStore = new HashMap<>();
	AtomicInteger nextId = new AtomicInteger(1);

	public UDPTicketManagementBackend(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
			throws TicketException {
		Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
		serverTicketStore.put(newTicket.getId(), newTicket);
	
		sendTicket(newTicket, this.hostname, this.port);
		return (Ticket) newTicket.clone();
	}

	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		//sendRequestToFetchAllTickets("getAllTicket");
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
		sendRequestToUpdate((Ticket) ticketToModify.clone());
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
		sendRequestToUpdate((Ticket) ticketToModify.clone());
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
		sendRequestToUpdate((Ticket) ticketToModify.clone());
		return (Ticket) ticketToModify.clone();
	}

	@Override
	public void triggerShutdown() {

	}
	
	private void sendRequestToUpdate(Ticket ticket) {
		// just sends the updated ticket
		ticket.setDescription("changeStatus" + ticket.getId());
		sendTicket(ticket, hostname, port);

	}
	private void sendRequestToFetchAllTickets(String reqMessage) {
		// TODO fetch the tickets from sharedTicket store
		try (DatagramSocket sock = new DatagramSocket(null)) {
			SocketAddress serverAddress = new InetSocketAddress(hostname, port);
			byte[] ticketfragment = reqMessage.getBytes();
			DatagramPacket reqPacket = new DatagramPacket(ticketfragment, ticketfragment.length, serverAddress);

			// send packet
			sock.send(reqPacket);
			System.out.println(new String(reqPacket.getData()));

		} // try
		catch (IOException e) {
			// dummy exception handling
			e.printStackTrace();
		}
		updateTheLocalTicketStore();

	}
	private void updateTheLocalTicketStore() {

		try (DatagramSocket serverSocket = new DatagramSocket(null)) {
			// create socket address
			serverSocket.setSoTimeout(5000);
			int numberof_fragments = 0;// number of Fragments

			// prepare packet to receive data
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				// wait for packet
				serverSocket.receive(packet);
				// ServerTicketStore objServerTicketStore = new ServerTicketStore();
				System.out.println("\t [Client]: Received Packet. " + "Creating new Thread to handle it.");
				// handle packet externally in another thread

				//byte[] receivedData = packet.getData();
				//String content = new String(receivedData).trim();

				methods.Reassemble r = new Reassemble();
				DatagramPacket finalp = r.HandleLargePacket(packet);// This might be a fragment of a packet, so

				if (finalp != null) {
					numberof_fragments++; // increase number of Fragments
					// new PacketHandler(finalp).start();
					byte[] receivedByteData = finalp.getData();
					String receivedStringData = new String((receivedByteData), StandardCharsets.UTF_8);
					System.out.println("The received ticket list :" + receivedStringData);
					
					// System.out.println("This is ");
					System.out.println(String.format("\t *** [SERVER]: This Package has been sent via %s Fragments",
							numberof_fragments));
					numberof_fragments = 0;
				} else {
					numberof_fragments++;

				}

			} catch (SocketTimeoutException e) {
				// swallow timeoutsy
				System.out.println("exception"+e.getMessage());
			}

		} catch (SocketException e) {
			// dummy exception handling - do NOT do this in your Assignment!
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// dummy exception handling - do NOT do this in your Assignment!
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void sendTicket(Ticket ticket, String hostname, int port) {
		System.out.println("\t [Sender]: Trying to send message '" + ticket.toString() + "'");

		// create address of recipient

		List<String> ticketDataList = ticket.splitEqually(CHUNKSIZE);
		ticketDataList.forEach(item -> {

			try (DatagramSocket sock = new DatagramSocket(null)) {
				SocketAddress serverAddress = new InetSocketAddress(hostname, port);

				byte[] ticketfragment = item.getBytes();
				DatagramPacket packet = new DatagramPacket(ticketfragment, ticketfragment.length, serverAddress);

				// send packet
				sock.send(packet);
				System.out.println(new String(packet.getData()));
			} // try
			catch (IOException e) {
				// dummy exception handling
				e.printStackTrace();
			}

		});// foreach

	}

}
