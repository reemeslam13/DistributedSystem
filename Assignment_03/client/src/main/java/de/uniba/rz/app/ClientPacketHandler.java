package de.uniba.rz.app;

import java.net.DatagramPacket;

import de.uniba.rz.entities.Ticket;
import methods.Reassemble;
import methods.Sharedmethods;

public class ClientPacketHandler extends Thread {

	private final DatagramPacket packet;
	public static int numberof_fragments = 0;

	public ClientPacketHandler(DatagramPacket packet) {
		// numberof_fragments=0;
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
			// receivedTicket.setId(nextId);
			// nextId++;
			numberof_fragments = 0;
			UDPTicketManagementBackend.clientTicketStore.put(receivedTicket.getId(), receivedTicket);

			System.out.println("[PacketHandler Client id:" + this.getId() + "]: Received: Ticket# " + receivedTicket.getId());
			
		} // if

	}
}