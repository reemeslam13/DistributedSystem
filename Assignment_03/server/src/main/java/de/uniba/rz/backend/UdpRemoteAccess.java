package de.uniba.rz.backend;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import de.uniba.rz.entities.Ticket;

public class UdpRemoteAccess implements RemoteAccess {
	public static List<Ticket> serverTicketstoe = new ArrayList<Ticket>();
	String hostname;
	int port;
	int CHUNKSIZE = 1000;
	String REQUEST_MSG = "getalltickets";
	
	
	DatagramSocket serverSocket;// to recieve data from client
	// DatagramSocket clientSocket;//to send all tickets to client

	// private static ArrayList<String> serverFragments = new ArrayList<>();

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

	private void SendAllFragments() {
		
		for (int j = 0; j < serverTicketstoe.size(); j++) {

			Ticket ticket = serverTicketstoe.get(j);
			List<String> ticketDataList = ticket.splitEqually(CHUNKSIZE);
			for (int i = 0; i < ticketDataList.size(); i++) {
				try (DatagramSocket clientsock = new DatagramSocket(null)){
					SocketAddress clientAddress = new InetSocketAddress(hostname, port+1);
					byte[] fragment_packet = ticketDataList.get(i).getBytes();
					DatagramPacket packet = new DatagramPacket(fragment_packet,
							fragment_packet.length, clientAddress);
					clientsock.send(packet);
					clientsock.close();
					//clientSocket = new DatagramSocket(null);
				} catch (IOException e) {
					//clientsock.close();
					e.printStackTrace();
				}
				
		} // for i
		}//for j

	}

	/*
	 * private void CreateSocket(String host, int port) { try {
	 * 
	 * SocketAddress address = new InetSocketAddress(host, port); // and bind the
	 * socket to this address serverSocket = new DatagramSocket(null);
	 * serverSocket.bind(address); // set timeout time to 5000ms
	 * serverSocket.setSoTimeout(5000); return; } // try catch (IOException e) {
	 * e.printStackTrace(); System.exit(1);
	 * 
	 * } }
	 */
	@Override
	public void run() {
		try (DatagramSocket serversocket = new DatagramSocket(null)) {
			// create socket address
			SocketAddress address = new InetSocketAddress(port);

			// and bind the socket to this address
			serversocket.bind(address);

			// set timeout time to 5000ms
			serversocket.setSoTimeout(5000);

			while (true) {
				// prepare packet to receive data
				byte[] data = new byte[1050];
				DatagramPacket packet = new DatagramPacket(data, data.length);

				try {
					serversocket.receive(packet);

					// wait for packet
					
					// handle packet externally in another thread
					byte[] receivedData = packet.getData();
					String content = new String(receivedData).trim();

					if (Objects.deepEquals(content, REQUEST_MSG)) {// Client has sent request to get all
																	// tickets
						SendAllFragments();
						System.out.println("[SERVER]: Received Ticket Request");
						System.out.println("----------------------------------------------------");

					} else {// client has sent Ticket Fragment
						
						new PacketHandler(packet).start();

					} // else

				} catch (IOException e) {
					// swallow timeout
					// System.out.println("Time out...No Data to read...waiting...");
					// return;
				}
				// finally {serverSocket.close();}

			} // while
		} // run
 catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//serversocket.close();
		}
	}
}
