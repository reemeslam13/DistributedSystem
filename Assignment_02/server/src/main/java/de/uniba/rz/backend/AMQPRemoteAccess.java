package de.uniba.rz.backend;

import java.util.Scanner;

public class AMQPRemoteAccess implements RemoteAccess {
	String hostname;
	String queuename;
	String receivetype;

	public AMQPRemoteAccess(String hostname, String queuename, String receivetype) {

		this.hostname = hostname;
		this.queuename = queuename;
		this.receivetype = receivetype;
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public void run() {

		QueueReceiver server;

		// Create QueueReceiver (see class implementation)
		switch (this.receivetype) {
		case "pull":
			server = new PullQueueReceiver(this.hostname, this.queuename);
			break;
		case "push":
			server = new PushQueueReceiver(this.hostname, this.queuename);
			break;
		default:
			throw new IllegalArgumentException("Only push or pull API is supported!");
		}

		// Start the server (consumer)
		server.startServer();

		// Wait for Input to shutdown the Server properly
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hit Enter to stop the server.");
		scanner.nextLine();
		scanner.close();

		// Ask server for shutdown
		server.stopServer();

		// Wait for server's termination
		try {
			server.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
