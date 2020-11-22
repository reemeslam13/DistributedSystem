package de.uniba.rz.backend;

import java.net.DatagramPacket;

import de.uniba.rz.entities.Ticket;

public class PacketHandler extends Thread {

    private final DatagramPacket packet;

    public PacketHandler(DatagramPacket packet) {
        this.packet = packet;
        run();
    }

    public void run() {
//        System.out.println("\t [PacketHandler [id:"+this.getId()+"]: Handling received packet.");
        // process received packet
        byte[] receivedData = packet.getData();
        String content = new String(receivedData).trim();
        System.out.println("\t [PacketHandler [id:"+this.getId()+"]: Received: " + content);
        
        //String tempDesc = content.substring(0,12);
        Ticket newTempTicket = Ticket.toTicket(content);
        ServerTicketStore objServerTicketStore=new ServerTicketStore();
        if((newTempTicket.getDescription()).equals("changeStatus"+newTempTicket.getId())) {
        	//Update the status of the ticket in the store
        	objServerTicketStore.updateTicketStatus(newTempTicket.getId(),newTempTicket.getStatus());
        }
        else {
        	objServerTicketStore.storeNewTicket(newTempTicket.getReporter(), newTempTicket.getTopic(), newTempTicket.getDescription(), newTempTicket.getType(), newTempTicket.getPriority());
        }
        
    }

}