package de.uniba.rz.backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import javax.annotation.concurrent.ThreadSafe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

@ThreadSafe
public final class PullQueueReceiver extends QueueReceiver {

	private final String hostname;
	private final String queueName;

	private final ConnectionFactory connFactory = new ConnectionFactory();

	public PullQueueReceiver(String hostname, String queueName) {
		this.hostname = hostname;
		this.queueName = queueName;
	}

	@Override
	public void run() {

		// Note: Could be omitted because localhost is set be default
		connFactory.setHost(this.hostname);

		System.out.println("\t [RECEIVER]: Start waiting for messages");

		try (Connection connection = connFactory.newConnection();) {
			Channel channel = connection.createChannel();
			channel.queueDeclare(this.queueName, false, false, false, null);

			// Try to get a message from our queue as long nobody tells us to stop consuming
			// new message
			while (!Thread.currentThread().isInterrupted()) {
				// TODO: Think of an appropriate strategy to handle the acknowledgement, so far,
				// we are using automatic acknowledgement
				GetResponse response = channel.basicGet(this.queueName, true);

				if (response != null) {
					byte[] body = response.getBody();
					System.out.println("\t [RECEIVER]: >Received: " + new String(body, StandardCharsets.UTF_8));
				}
			}
			System.out.println("\t [RECEIVER]: Stopped.");

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
		this.interrupt(); // Ask the PullQueueReceiver for a graceful shutdown
	}

}