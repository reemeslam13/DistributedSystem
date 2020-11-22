package de.uniba.rz.backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import javax.annotation.concurrent.ThreadSafe;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;

@ThreadSafe
public final class PushQueueReceiver extends QueueReceiver {

	private final String hostname;
	private final String queueName;

	private final ConnectionFactory connFactory = new ConnectionFactory();

	public PushQueueReceiver(String hostname, String queueName) {
		this.hostname = hostname;
		this.queueName = queueName;
	}

	@Override
	public void run() {

		// Data structure exchanging data between the receiver's thread and the
		// dispatcher thread
		final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1, true);

		// Note: Could be omitted because localhost is set be default
		connFactory.setHost(this.hostname);

		System.out.println("\t [RECEIVER]: Start waiting for messages");

		try (Connection connection = connFactory.newConnection(); Channel channel = connection.createChannel()) {

			channel.queueDeclare(this.queueName, false, false, false, null);

			// Ensure that we will only get one message at a time from RabbitMQ
			channel.basicQos(1);
			//  Declare fanoutExchange
			channel.exchangeDeclare("fanoutExchange", BuiltinExchangeType.FANOUT);
			String getBC = channel.queueDeclare().getQueue();
			channel.queueBind(getBC, "fanoutExchange", "");

			// Read replyTo-queue and the correlation id

			/*
			 * This method registers a consumer on the queue by the usage of the consumerTag
			 * and a callback object. We pass the callback object DefaultConsumer, which
			 * implements the interface Consumer. A dispatcher thread within the Java Client
			 * Library will call the methods within this interface, for example,
			 * handleDelivery(...). Hence, we have to provide an implementation to achieve a
			 * certain behavior.
			 *
			 * NOTE: PushQueueReceiver will not call the implemented methods of that
			 * interface! These methods are called by the thread pool of the dispatcher (one
			 * per connection)!
			 */
			// TODO: think of an appropriate strategy to handle the acknowledgement


			channel.basicConsume(this.queueName, false, "myConsumerTag", new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
										   byte[] body) throws IOException {
					try {
						// After receiving, add the message to a thread-safe data structure, like a
						// BlockingQueue
						String recievedMess = new String(body, StandardCharsets.UTF_8);
						if(recievedMess.startsWith("Announcement")) {
							// BroadCast for Clients updates
							System.out.print("Start BroadCasting to Clients");
							channel.basicPublish("fanoutExchange", "", null, body);

						} else {
							blockingQueue.put(new String(body, StandardCharsets.UTF_8));
							String replyTo = properties.getReplyTo();
							String correlationId = properties.getCorrelationId();
							// Read replyTo-queue and the correlation id

							// Prepare response for the client
							String responseMessage = new String("Processed by Server: ")
									.concat(new String(body, StandardCharsets.UTF_8));
							// Append the correlation id to support to match request and response
							BasicProperties basicProperties = properties.builder().correlationId(correlationId).build();
							channel.basicPublish("", replyTo, basicProperties, responseMessage.getBytes());
							channel.basicAck(envelope.getDeliveryTag(), true);
							System.out.println("Ack Sent!");
							channel.basicPublish("fanoutExchange", "", null, "Client has created a ticket".getBytes());
						}

					} catch (InterruptedException e) {
						// Preserve the interrupt for the caller (channel's thread pool)
						Thread.currentThread().interrupt();
					}
				}

			});

			while (!Thread.currentThread().isInterrupted()) {
				try {
					/*
					 * The PushQueueReceiver thread will wait until a message becomes available.
					 * Because we will receive messages asynchronously, this may need an indefinite
					 * amount of time. PushQueueReceiver will be suspended (by calling take()) from
					 * the CPU while it is waiting for a message (No Pull-API!)
					 */
					System.out.println("\t [RECEIVER]: >Received: " + blockingQueue.take());

					// TODO: further processing of the message
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

		} catch (IOException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying,
			// logging, ...)
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO: Think of an appropriate exception handling strategy (e.g., retrying,
			// logging, ...)
			e.printStackTrace();
		}
	}

	@Override
	public void startServer() {
		this.start();
	}

	@Override
	public void stopServer() {
		System.out.println("\t [RECEIVER]: Stopping to listen for messages.");
		this.interrupt(); // Ask the PushQueueReceiver for a graceful shutdown
	}
}
