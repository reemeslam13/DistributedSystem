package de.uniba.rz.backend;

import java.net.DatagramPacket;

import de.uniba.rz.entities.Ticket;
import methods.Reassemble;
import methods.Sharedmethods;

public class PacketHandler extends Thread {

	private final DatagramPacket packet;
	public static int numberof_fragments = 0;
	public static int nextId = 1;

	public PacketHandler(DatagramPacket packet) {
		this.packet = packet;

	}

	public void run() {
		numberof_fragments++;
		methods.Reassemble r = new Reassemble();
		String finalp = r.HandleLargePacket(packet);

		if (finalp != null) {// This is the last Fragment, so the ticket is completed
			// System.out.println(
			// String.format("[SERVER]: Received Ticket via %s Fragments",
			// numberof_fragments));
			Ticket receivedTicket = new Sharedmethods().toTicket(finalp);
			receivedTicket.setId(nextId);
			nextId++;
			numberof_fragments = 0;

			UdpRemoteAccess.serverTicketstoe.add(receivedTicket);
			System.out.println("[SERVER Packet Handler]: Received: Ticket#" + receivedTicket.getId());
			System.out.println("----------------------------------------------------");
		}

	}
}