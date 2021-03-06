package de.uniba.rz.app;

import com.rabbitmq.client.*;
import de.uniba.rz.entities.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.rabbitmq.client.AMQP.BasicProperties;

public class AMQPTicketManagementBackend extends Thread implements TicketManagementBackend {
    String hostname;
    String queuname;
    final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1, true);

    int CHUNKSIZE = 1000;
    HashMap<Integer, Ticket> serverTicketStore = new HashMap<>();
    AtomicInteger nextId = new AtomicInteger(1);
    private final ConnectionFactory connFactory = new ConnectionFactory();

    public AMQPTicketManagementBackend(String hostname, String queuename) {
        this.hostname = hostname;
        this.queuname = queuename;
        sendMessage("Announcement New Client");
        this.start();
    }

    @Override
    public Ticket createNewTicket(String reporter, String topic, String description, Type type, Priority priority)  {
        Ticket newTicket = new Ticket(nextId.getAndIncrement(), reporter, topic, description, type, priority);
        serverTicketStore.put(newTicket.getId(), newTicket);
        sendMessage(newTicket.toString());
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
        sendMessage("Announcement Client accepting ticket");
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
        sendMessage("Announcement Client rejecting data");
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
        sendMessage("Announcement Client closing ticket");
        return (Ticket) ticketToModify.clone();
    }

    @Override
    public void triggerShutdown() {
        this.interrupted();
    }


    public void sendMessage(String message) {

        // Note: Could be omitted because localhost is set be default
        connFactory.setHost(this.hostname);

        try (Connection connection = connFactory.newConnection();) {

            Channel channel = connection.createChannel();

            channel.queueDeclare(this.queuname, false, false, false, null);

            // Declare a server-named and temporary queue
            String replyQueue = channel.queueDeclare().getQueue(); // Create a random UUID as correlation id
            String uuid = UUID.randomUUID().toString();
            // Create properties
            BasicProperties basicProperties = new BasicProperties().builder().correlationId(uuid).replyTo(replyQueue)
                    .build();
            channel.basicPublish("", this.queuname, basicProperties, message.getBytes());


            // Send message to the default exchange ("") with the queueName as routing key

        } catch (IOException e) {
            // TODO: Think of an appropriate exception handling strategy (e.g., retrying,
            // logging,...)
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO: Think of an appropriate exception handling strategy (e.g., retrying,
            // logging,...)
            e.printStackTrace();
        }

    }

    public void run() {
        System.out.println("check");
        try (Connection connection = connFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(this.queuname, false, false, false, null);
            String getBC = channel.queueDeclare().getQueue();
            channel.queueBind(getBC, "fanoutExchange", "");
            System.out.println("Get BroadCast");
            channel.basicConsume(getBC, false, "myConsumerTag", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    System.out.println("new String(body, StandardCharsets.UTF_8)");
                    try {
                        // After receiving, add the message to a thread-safe data structure, like a
                        // BlockingQueue
                        blockingQueue.put(new String(body, StandardCharsets.UTF_8));
                        System.out.println(new String(body, StandardCharsets.UTF_8));
                        // Read replyTo-queue and the correlation id
                        // Prepare response for the client
                    } catch (InterruptedException e) {
                        // Preserve the interrupt for the caller (channel's thread pool)
                        Thread.currentThread().interrupt();
                    }
                }
            });

        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
