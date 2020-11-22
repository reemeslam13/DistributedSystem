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
        MainFrame mf = new MainFrame(control, model);

        control.setMainFrame(mf);
        control.setSwingMainModel(model);

        control.start();
    }

    private static TicketManagementBackend evaluateArgs(String[] args) {

        Applicationmethods.setmethod(MsgPassingMethod.LOCAL);// Default message passing method
        System.out.println("* In case of UDP method, Three arguments needed: " + "<'udp'> <hostname> <portnumber> \n"
                + " * In case of AMQP method, Three arguments needed: " + "<'AMQP'> <hostname> <queueName> "
                + " * Otherwise Local method will be used");

        if (args == null || args.length < 3) {
            System.out.println("* Using local backend implemenation.");

            return new LocalTicketManagementBackend();

        } else {
            switch (args[0].toLowerCase()) {
                case "local":
                    Applicationmethods.setmethod(MsgPassingMethod.LOCAL);// Default message passing method
                    return new LocalTicketManagementBackend();
                case "udp":
                    System.out.println("*** Using UDP backend implemenation.");
                    Applicationmethods.setmethod(MsgPassingMethod.UDP);// UDP message passing method
                    String UDPhost = args[1];
                    int UDPport = Integer.parseInt(args[2]);
                    return new UDPTicketManagementBackend(UDPhost, UDPport);
                case "amqp":
                    Applicationmethods.setmethod(MsgPassingMethod.AMQP);// AMQP message passing method
                    System.out.println("*** Using AMQP backend implemenation.");
                    String AMQPhost = args[1];
                    String queuename = args[2];
                    //return new AMQPTicketManagementBackend(AMQPhost, queuename);

                // Default case for unknown implementations
                default:
                    System.out.println("* Unknown backend type. Using local backend implementation.");
                    return new LocalTicketManagementBackend();
            }

        }
    }
}
