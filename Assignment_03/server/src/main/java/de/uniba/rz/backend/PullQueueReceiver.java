package de.uniba.rz.backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import javax.annotation.concurrent.ThreadSafe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import de.uniba.rz.entities.AMQPChannelProperty;
import methods.Configuration;
import methods.Sharedmethods;

@ThreadSafe
public final class PullQueueReceiver extends QueueReceiver {

	private final String hostname;
	private final String queuename;
	private final String LOGFILE = "serverlogfile.log";

	private final ConnectionFactory connFactory = new ConnectionFactory();

	public PullQueueReceiver(String hostname, String queueName) {
		this.hostname = hostname;
		this.queuename = queueName;
	}

	@Override
	public void run() {

		// Could be omitted because localhost is set be default
		connFactory.setHost(this.hostname);

		System.out.println(" [RECEIVER]: Start waiting for messages");
		System.out.println("-------------------------------------------");

		try (Connection connection = connFactory.newConnection();) {
			Channel channel = connection.createChannel();
			Configuration c = new Configuration();
			boolean autodelete = c.getAMQPchannelProp(AMQPChannelProperty.AUTODELETE);
			boolean durable = c.getAMQPchannelProp(AMQPChannelProperty.DURABLE);
			boolean exclusive = c.getAMQPchannelProp(AMQPChannelProperty.EXCLUSIVE);
			try {
				channel.queueDeclarePassive(this.queuename);
			} catch (IOException e) {// channel does not exist, so create it
				channel = connection.createChannel();// queueDeclarePassive has closed the channel, so we need to
														// recreate it
				channel.queueDeclare(this.queuename, durable, exclusive, autodelete, null);
			}

			// Try to get a message from our queue as long nobody tells us to stop consuming
			// new message
			while (!Thread.currentThread().isInterrupted()) {
				
				GetResponse response = channel.basicGet(this.queuename, true);

				if (response != null) {
					byte[] body = response.getBody();
					System.out.println(" [RECEIVER]: >Received: " + new String(body, StandardCharsets.UTF_8));
					System.out.println("-------------------------------------------");
				}
			}
			System.out.println("\t [RECEIVER]: Stopped.");

		} catch (IOException e) {
			String msg = "AMQP Server IO Exception\n";
			msg = msg+e.getCause().getMessage();
			new Sharedmethods().log(LOGFILE,  msg);
			return;
			
		} catch (TimeoutException e) {
			String msg = "AMQP Server Timeout Exception\n";
			msg = msg+e.getCause().getMessage();
			new Sharedmethods().log(LOGFILE,  msg);			
			this.interrupt();
			return;
			
		}
	}

	@Override
	public void startServer() {
		this.start();
	}

	@Override
	public void stopServer() {
		System.out.println(" [RECEIVER]: Stopping to listen for messages.");
		System.out.println("-------------------------------------------");
		this.interrupt(); // Ask the PullQueueReceiver for a graceful shutdown
	}

}