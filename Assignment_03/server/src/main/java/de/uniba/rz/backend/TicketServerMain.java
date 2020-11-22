package de.uniba.rz.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import de.uniba.rz.entities.MsgPassingMethod;
import methods.Applicationmethods;

public class TicketServerMain {

	public static void main(String[] args) throws IOException, NamingException {
		System.out.println(" * To run in UDP method, 3 arguments needed: " + "\t 'udp' <hostname> <portnumber>\n"
				+ " * for AMQP method, 4 arguments needed: " + "\t 'AMQP' <hostname> <queueName> <push|pull>\n"
				+ " * for gRPC method, 2 arguments needed: " + "\t 'gRPC' <hostname> <portnumber>\n"
				+ " * for REST method, 1 argument  needed: \t 'Rest'\n" + " * Otherwise Local method will be used\n\n"
				+ " * case insensitive *\n"
				+ "------------------------------------------- SERVER -------------------------------------------");
		Applicationmethods.setmethod(MsgPassingMethod.LOCAL);// Default message passing method

		ServerTicketStore simpleTestStore = new ServerTicketStore();
		List<RemoteAccess> remoteAccessImplementations = getAvailableRemoteAccessImplementations(args);
		if (remoteAccessImplementations==null) System.exit(0);

		// Starting remote access implementations:
		for (RemoteAccess implementation : remoteAccessImplementations) {
			implementation.prepareStartup(simpleTestStore);
			new Thread(implementation).start();
		} // for

		try (BufferedReader shutdownReader = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Press enter to shutdown system.");
			shutdownReader.readLine();
			System.out.println("Shutting down...");

			// Shutting down all remote access implementations
			for (RemoteAccess implementation : remoteAccessImplementations) {
				implementation.shutdown();
			} // for
			System.out.println("completed. Bye!");
			System.exit(0);
		} // try

	}// main

	private static List<RemoteAccess> getAvailableRemoteAccessImplementations(String[] args) throws SocketException {
		List<RemoteAccess> implementations = new ArrayList<>();
		if (args == null) {
			System.out.println("*Invalid parameters");

		} else {
			try {
				switch (args[0].toLowerCase()) {

				case "grpc":
					System.out.println("* Using gRPC backend implemenation...");
					Applicationmethods.setmethod(MsgPassingMethod.gRPC);// gRPC
					int gRPCport = Integer.parseInt(args[2]);
					implementations.add(new gRPCRemoteAccess(gRPCport));
					break;
				case "rest":
					System.out.println("* Using REST backend implemenation...");
					Applicationmethods.setmethod(MsgPassingMethod.REST);// RESTful API
					implementations.add(new RESTRemoteAccess());
					break;
				case "udp":
					System.out.println("* Using UDP backend implemenation...");
					Applicationmethods.setmethod(MsgPassingMethod.UDP);// UDP message passing method
					String UDPhost = args[1];
					int UDPport = Integer.parseInt(args[2]);
					implementations.add(new UdpRemoteAccess(UDPhost, UDPport));
					break;
				case "amqp":
					Applicationmethods.setmethod(MsgPassingMethod.AMQP);// AMQP message passing method
					System.out.println("* Using AMQP backend implemenation...");
					String AMQPhost = args[1];
					String queuename = args[2];
					String AMQPmethod = args[3];
					implementations.add(new AMQPRemoteAccess(AMQPhost, queuename, AMQPmethod));
					break;

				// Default case for unknown implementations
				default:
					implementations.clear();
					System.out.println("* Unknown server type. Try running server again...");

				}// switch
			} catch (Exception e) {
				System.out.println("Invalid arguments.Try running server again...");
				System.exit(0);
			}

		} // else

		return implementations;

	}
}
