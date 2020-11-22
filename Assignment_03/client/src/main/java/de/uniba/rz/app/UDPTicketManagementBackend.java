package de.uniba.rz.app;


//import de.uniba.rz.backend.PacketHandler;
//import de.uniba.rz.backend.PacketHandler;
import de.uniba.rz.entities.*;
import methods.Reassemble;
import methods.Sharedmethods;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UDPTicketManagementBackend implements TicketManagementBackend {
	String hostname;
	int port;
	int CHUNKSIZE = 1000;
	private final String LOGFILE = "clientlogfile.log";
	String REQUEST_MSG = "getalltickets";
	DatagramSocket clientSocket;
	SocketAddress serverAddress;
	static HashMap<Integer, Ticket> clientTicketStore = new HashMap<>();
	AtomicInteger nextId = new AtomicInteger(1);

	public UDPTicketManagementBackend(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		CreateSocket(hostname, port);
	}

	@Override
	public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
			throws TicketException {
		Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
		//clientTicketStore.put(newTicket.getId(), newTicket);

		sendTicket(newTicket, this.hostname, this.port);
		return (Ticket) newTicket.clone();
	}

	private void ReadTickets() {
		
		
		clientTicketStore.clear();
		methods.Reassemble r = new Reassemble();
		r.ReleaseMemory();
		
		try (DatagramSocket serversocket = new DatagramSocket(null)) {
			// create socket address
			SocketAddress address = new InetSocketAddress(port+1);

			// and bind the socket to this address
			serversocket.bind(address);

			// set timeout time to 5000ms
			serversocket.setSoTimeout(1000);

			while (true) {
			
				byte[] data = new byte[1050];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				 try {
					serversocket.receive(packet);
					//System.out.println("[CLIENT]: Received Packet/Fragment. " + "Creating new Thread to handle it.");

					new ClientPacketHandler(packet).start();
					} catch (IOException e) {
						String msg = "UDP Client IO Exception\n";
						msg = msg+e.getMessage();
						new Sharedmethods().log(LOGFILE,  msg);		
						return;
					}
				} // while
			} // run
	 catch (SocketException e1) {
		 String msg = "UDP Client Socket Exception\n";
			msg = msg+e1.getMessage();
			new Sharedmethods().log(LOGFILE,  msg);	
			}

	}
	@Override
	public List<Ticket> getAllTickets() throws TicketException {
		
		SendRequestToServer();
		ReadTickets();
		//System.out.println("----------------------------------------------------");
		return clientTicketStore.entrySet().stream().map(entry -> (Ticket) entry.getValue().clone())
				.collect(Collectors.toList());

	}

	@Override
	public Ticket getTicketById(int id) throws TicketException {
		if (!clientTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}

		return clientTicketStore.get(id);
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
		if (!clientTicketStore.containsKey(id)) {
			throw new TicketException("Ticket ID is unknown");
		}

		return clientTicketStore.get(id);
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

	public boolean CreateSocket(String hostname, int port) {
		try {
			clientSocket = new DatagramSocket(null);
			serverAddress = new InetSocketAddress(hostname, port);
			return true;
		} catch (Exception e) {
			String msg = "UDP Client Socket Exception\n";
			msg = msg+e.getMessage();
			new Sharedmethods().log(LOGFILE,  msg);	
			return false;
		}

	}

	public void SendRequestToServer() {
		byte[] request_message = REQUEST_MSG.getBytes();

		DatagramPacket packet = new DatagramPacket(request_message, request_message.length, serverAddress);

		// send packet
		try {
			clientSocket.send(packet);
		} catch (IOException e) {
			String msg = "UDP Client IO Exception\n";
			msg = msg+e.getMessage();
			new Sharedmethods().log(LOGFILE,  msg);	
			clientSocket.close();
		}
		System.out.println("[CLIENT]: Send request to fetch tickets from server");
		System.out.println("----------------------------------------------------");

	}

	public void sendTicket(Ticket ticket, String hostname, int port) {
		
		List<String> ticketDataList = ticket.splitEqually(CHUNKSIZE);
		for(int i=0;i<ticketDataList.size();i++) {

			byte[] ticketfragment = ticketDataList.get(i).getBytes();
			DatagramPacket packet = new DatagramPacket(ticketfragment, ticketfragment.length, serverAddress);

			// send packet
			try {
				clientSocket.send(packet);
			} catch (IOException e) {
				String msg = "UDP Client IO Exception\n";
				msg = msg+e.getMessage();
				new Sharedmethods().log(LOGFILE,  msg);	
				
			}
			
			
		} // foreach
		System.out.println(String.format("[CLIENT]: Send Ticket # %s to server", ticket.getId()));
		System.out.println("----------------------------------------------------");

	}

}
