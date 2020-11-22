package de.uniba.rz.backend;

import java.io.IOException;
import java.net.*;
import java.util.List;

import de.uniba.rz.entities.Ticket;
//import de.uniba.rz.entities.*;
import methods.*;

public class UdpRemoteAccess implements RemoteAccess {
	String hostname;
	int port;
	int CHUNKSIZE = 1000;
	//int initialCommunicationPort = 1200;
	
	public UdpRemoteAccess(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public void run() {
		try (DatagramSocket serverSocket = new DatagramSocket(null)) {
			// create socket address
			SocketAddress address = new InetSocketAddress(this.port);
			// and bind the socket to this address
			serverSocket.bind(address);
			// set timeout time to 5000ms
			serverSocket.setSoTimeout(5000);
			int numberof_fragments = 0;// number of Fragments
			while (true) {
				// prepare packet to receive data
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);

				try {
					// wait for packet
					serverSocket.receive(packet);
					
					
					ServerTicketStore objServerTicketStore = new ServerTicketStore();
					
					System.out.println("\t [SERVER]: Received Packet. " + "Creating new Thread to handle it.");
					// handle packet externally in another thread

					byte[] receivedData = packet.getData();
					String content = new String(receivedData).trim();
					if (content.equals("getAllTicket")) {

						List<Ticket> resServerTicketList = objServerTicketStore.getAllTickets();
						respondToTheRequest(resServerTicketList);

					}
					else {
						
						methods.Reassemble r = new Reassemble();
						DatagramPacket finalp = r.HandleLargePacket(packet);// This might be a fragment of a packet, so
						// will be sent to HandleLargePacket method to reassemble with other Fragments

						// if it is the last Fragment, the return value from HandleLargePacket method
						// will be the completed Ticket.
						// It will be sent to PacketHandler
						if (finalp != null) {
							numberof_fragments++; // increase number of Fragments
							new PacketHandler(finalp).start();
							System.out.println("\t ***[ SERVER]: Received Packet. ");
							System.out.println(String.format("\t *** [SERVER]: This Package has been sent via %s Fragments",
									numberof_fragments));
							numberof_fragments = 0;
						} else {
							numberof_fragments++;

						}

					}
					}
					 catch (SocketTimeoutException e) {
					// swallow timeout
				}
			} // while
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
	
	private void respondToTheRequest(List<Ticket> resServerTicketList) {

		// create address of recipient
		resServerTicketList.forEach(ticket -> {
			List<String> ticketDataList = ticket.splitEqually(CHUNKSIZE);
			ticketDataList.forEach(item -> {

				try (DatagramSocket sock = new DatagramSocket(null)) {

					SocketAddress serverAddress = new InetSocketAddress(this.hostname, port);

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

			});
		});// foreach

	}

}
