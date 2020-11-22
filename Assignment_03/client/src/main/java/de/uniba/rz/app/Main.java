package de.uniba.rz.app;

import de.uniba.rz.ui.swing.MainFrame;
import de.uniba.rz.entities.MsgPassingMethod;
import methods.Applicationmethods;

import de.uniba.rz.ui.swing.SwingMainController;
import de.uniba.rz.ui.swing.SwingMainModel;

/**
 * Main class to start the TicketManagement5000 client application Currently
 * only a local backend implementation is registered.<br>
 * 
 * To add additional implementations modify the method
 * <code>evaluateArgs(String[] args)</code>
 *
 * @see #evaluateArgs(String[])
 */
public class Main {
	public static MsgPassingMethod selectedMsgPassingMethod = MsgPassingMethod.LOCAL;

	/**
	 * Starts the TicketManagement5000 application based on the given arguments
	 * 
	 * <p>
	 * <b>TODO No changes needed here - but documentation of allowed args should be
	 * updated</b>
	 * </p>
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {

		TicketManagementBackend backendToUse = evaluateArgs(args);
		

		SwingMainController control = new SwingMainController(backendToUse);
		SwingMainModel model = new SwingMainModel(backendToUse);
		MainFrame mf = new MainFrame(control, model,selectedMsgPassingMethod );

		control.setMainFrame(mf);
		control.setSwingMainModel(model);

		control.start();
	}

	private static TicketManagementBackend evaluateArgs(String[] args) {

		Applicationmethods.setmethod(MsgPassingMethod.LOCAL);// Default message passing method
		System.out.println(" * To run in UDP method, 3 arguments needed: " + "\t 'udp' <hostname> <portnumber>\n"
				+ " * for AMQP method, 3 arguments needed: " + "\t 'AMQP' <hostname> <queueName>\n"
				+ " * for gRPC method, 3 arguments needed: " + "\t 'gRPC' <hostname> <port number>\n"
				+ " * for REST method, 1 argument  needed: \t 'Rest'\n" + " * Otherwise Local method will be used\n\n"
				+ " * case insensitive *\n"
				+ "------------------------------------------- CLIENT -------------------------------------------");

		if (args == null) {
			System.out.println("* Using local backend implemenation.");

			return new LocalTicketManagementBackend();

		} else {
			try {
				switch (args[0].toLowerCase()) {

				case "local":
					selectedMsgPassingMethod= MsgPassingMethod.LOCAL;// Default message passing method
					return new LocalTicketManagementBackend();
				case "grpc":
					System.out.println("*** Using gRPC backend implemenation.");
					selectedMsgPassingMethod = MsgPassingMethod.gRPC;// gRPC
					String gRPChost = args[1];
					int gRPCport = Integer.parseInt(args[2]);
					return new gRPCTicketManagementBackend(gRPChost, gRPCport);
				case "rest":
					selectedMsgPassingMethod = (MsgPassingMethod.REST);// RESTful API
					return new RESTTicketManagementBackend();
				case "udp":
					System.out.println("*** Using UDP backend implemenation.");
					selectedMsgPassingMethod = MsgPassingMethod.UDP;// UDP message passing method
					String UDPhost = args[1];
					int UDPport = Integer.parseInt(args[2]);
					return new UDPTicketManagementBackend(UDPhost, UDPport);
				case "amqp":
					selectedMsgPassingMethod = MsgPassingMethod.AMQP;// AMQP message passing method
					System.out.println("*** Using AMQP backend implemenation.");
					String AMQPhost = args[1];
					String queuename = args[2];
					return new AMQPTicketManagementBackend(AMQPhost, queuename);

				// Default case for unknown implementations
				default:
					selectedMsgPassingMethod= MsgPassingMethod.LOCAL;
					System.out.println("* Unknown backend type. Using local backend implementation.");
					return new LocalTicketManagementBackend();
				}

			} catch (Exception e) {
				System.out.println("Invalid arguments.Run client again.");
				return new LocalTicketManagementBackend();
			}
		}
	}
}
