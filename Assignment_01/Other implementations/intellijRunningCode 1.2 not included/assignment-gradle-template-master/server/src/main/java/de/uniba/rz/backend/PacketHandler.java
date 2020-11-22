package de.uniba.rz.backend;

import java.net.DatagramPacket;

public class PacketHandler extends Thread {

    private final DatagramPacket packet;

    public PacketHandler(DatagramPacket packet) {
        this.packet = packet;
    }

    public void run() {
//        System.out.println("\t [PacketHandler [id:"+this.getId()+"]: Handling received packet.");
        // process received packet
        byte[] receivedData = packet.getData();
        String content = new String(receivedData).trim();
        System.out.println("\t [PacketHandler [id:"+this.getId()+"]: Received: " + content);
    }

}