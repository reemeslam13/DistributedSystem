package de.uniba.rz.app;

import de.uniba.rz.entities.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UDPTicketManagementBackend implements TicketManagementBackend {
    String hostname;
    int port;
    int CHUNKSIZE = 1000;
    HashMap<Integer, Ticket> serverTicketStore = new HashMap<>();
    AtomicInteger nextId = new AtomicInteger(1);

    public UDPTicketManagementBackend(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)
            throws TicketException {
        Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
        serverTicketStore.put(newTicket.getId(), newTicket);



        sendTicket(newTicket, this.hostname, this.port);
        return (Ticket) newTicket.clone();
    }

    @Override
    public List<Ticket> getAllTickets() throws TicketException {
        return serverTicketStore.entrySet().stream().map(entry -> (Ticket) entry.getValue().clone())
                .collect(Collectors.toList());
    }

    @Override
    public Ticket getTicketById(int id) throws TicketException {
        if (!serverTicketStore.containsKey(id)) {
            throw new TicketException("Ticket ID is unknown");
        }

        return serverTicketStore.get(id);
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
        if (!serverTicketStore.containsKey(id)) {
            throw new TicketException("Ticket ID is unknown");
        }

        return serverTicketStore.get(id);
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

    public void sendTicket(Ticket ticket, String hostname, int port) {
        System.out.println("\t [Sender]: Trying to send message '" + ticket.toString() + "'");

        // create address of recipient

        List<String> ticketDataList = ticket.splitEqually(CHUNKSIZE);
        ticketDataList.forEach(item -> {

            try (DatagramSocket sock = new DatagramSocket(null)) {
                SocketAddress serverAddress = new InetSocketAddress(hostname, port);

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

        });// foreach

    }

}
