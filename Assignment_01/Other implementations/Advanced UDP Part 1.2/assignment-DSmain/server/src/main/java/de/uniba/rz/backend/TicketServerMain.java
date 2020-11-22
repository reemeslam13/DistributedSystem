package de.uniba.rz.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

public class TicketServerMain {

    public static void main(String[] args) throws IOException, NamingException {

        if (args.length < 3) {
            System.out.println("Invalid usage. \n " + "* In case of UDP method, Three arguments needed: "
                    + "<'udp'> <hostname> <portnumber> \n" + " * In case of AMQP method, Four arguments needed: "
                    + "<'AMQP'> <hostname> <queueName> <push|pull>");
        } else {

            TicketStore serverTicketStore = new ServerTicketStore();

            List<RemoteAccess> remoteAccessImplementations = getAvailableRemoteAccessImplementations(args);

            // Starting remote access implementations:
            for (RemoteAccess implementation : remoteAccessImplementations) {
                implementation.prepareStartup(serverTicketStore);
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
            } // try
        } // else
    }

    private static List<RemoteAccess> getAvailableRemoteAccessImplementations(String[] args) throws SocketException {
        List<RemoteAccess> implementations = new ArrayList<>();
        if(args.length == 3){//UDP
            implementations.add(new UdpRemoteAccess(args[1], Integer.parseInt(args[2])));
        }
//        if (args.length == 4)//amqp
//            implementations.add(new AMQPRemoteAccess(args[1], args[2], args[3]));

        return implementations;
    }
}
