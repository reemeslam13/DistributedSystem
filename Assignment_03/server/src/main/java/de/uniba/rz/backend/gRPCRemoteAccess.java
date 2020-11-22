package de.uniba.rz.backend;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

import de.uniba.rz.io.rpc.gRPCticket;
import de.uniba.rz.io.rpc.gRPCticketlist;
import de.uniba.rz.io.rpc.gRPCticketlist.Builder;
import de.uniba.rz.io.rpc.gRPCupdateReq;
import de.uniba.rz.io.rpc.gRPCack;
import de.uniba.rz.io.rpc.gRPCindex;
import de.uniba.rz.io.rpc.gRPCstatus;
import de.uniba.rz.io.rpc.TicketServiceGrpc.TicketServiceImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import methods.Sharedmethods;

public class gRPCRemoteAccess implements RemoteAccess {
	static AtomicInteger nextId;
	private static gRPCticketlist TICKETS = gRPCticketlist.newBuilder().build();

	private final int port;
	private final Server server;
	private final String LOGFILE = "serverlogfile.log";

	public gRPCRemoteAccess(int port) {
		nextId = new AtomicInteger(0);
		this.port = port;
		this.server = ServerBuilder.forPort(port).addService(new TicketImpl()).build();

	}

	public void start() throws IOException {
		server.start();
		System.out.println("Server started and is listened on port " + this.port);
		System.out.println("-------------------------------------------------------------");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown
				// hook.
				
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				gRPCRemoteAccess.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}// start

	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	@Override
	public void prepareStartup(TicketStore ticketStore) throws SocketException {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/**
	 * Build ticket id on server side to avoid repeated ids This method will be used
	 * locally
	 * 
	 * @param ticket: a gRCPC ticket
	 * @return
	 */
	public static gRPCticket UpdateTicketID(gRPCticket ticket) {
		gRPCticket.Builder builder = ticket.toBuilder();
		nextId.getAndIncrement();
		ticket = builder.setId(nextId.intValue()).build();
		return ticket;
	}

	static class TicketImpl extends TicketServiceImplBase {

		@Override
		// Azar: Here I should write the codes
		/**
		 * gets a ticket from client, updates ticket id store in ticket store
		 */
		public void addTicket(gRPCticket ticket, StreamObserver<gRPCack> gRPCackObserver) {
			String newTicketstr = ticket.toString();
			ticket = UpdateTicketID(ticket);
			System.out.println("[SERVER]: New Ticket : " + newTicketstr);
			System.out.println("-------------------------------------------------------------");
			gRPCack ack = gRPCack.newBuilder().setTicketAck(true).build();
			Builder builder = TICKETS.toBuilder();
			TICKETS = builder.addGRPCticketlisElement(ticket).build();

			gRPCackObserver.onNext(ack);
			gRPCackObserver.onCompleted();
		}// addTicket

		/**
		 * if index=-1 returns all tickets return server ticket store to client
		 */
		@Override
		public void getTicket(gRPCindex index, StreamObserver<gRPCticketlist> gRPCticketlistObserver) {
			if (index.getIndex() < 0) { // return all tickets
				Builder builder = TICKETS.toBuilder();
				gRPCticketlistObserver.onNext(builder.build());
				gRPCticketlistObserver.onCompleted();
			} // if
		}

		/**
		 * used locally to find a ticket index in server store by ticket id
		 * 
		 * @param ticketid
		 * @return index of ticket in ticket store, -1 if not exist
		 */
		int FindTicketListIndex(int ticketid) {
			for (int i = 0; i < TICKETS.getGRPCticketlisElementCount(); i++) {
				if (TICKETS.getGRPCticketlisElement(i).getId() == ticketid)
					return i;
			}
			return -1;
		}

		/**
		 * update a ticket's status
		 */

		@Override
		public void updateTicketStatus(gRPCupdateReq updaterequest,
				io.grpc.stub.StreamObserver<gRPCack> updateresponseObserver) {
			int ticketid = updaterequest.getIndex();
			gRPCstatus newstatus = updaterequest.getNewstatus();
			int ticketlistindex = FindTicketListIndex(ticketid);
			Builder builder = TICKETS.toBuilder();
			builder.getGRPCticketlisElementBuilder(ticketlistindex).setTstatus(newstatus);
			TICKETS = builder.build();
			gRPCack ack = gRPCack.newBuilder().setTicketAck(true).build();
			updateresponseObserver.onNext(ack);
			updateresponseObserver.onCompleted();
		}
	}

	@Override
	public void run() {
		final gRPCRemoteAccess server = new gRPCRemoteAccess(port);
		try {
			server.start();
		} catch (IOException e) {
			String msg = "gRPC Server IO Exception\n";
			msg = msg+e.getCause().getMessage();
			new Sharedmethods().log(LOGFILE,  msg);		
		}
		try {
			server.blockUntilShutdown();
		} catch (InterruptedException e) {
			String msg = "gRPC Server Timeout Exception\n";
			msg = msg+e.getMessage();
			new Sharedmethods().log(LOGFILE,  msg);		
		}
		

	}

}
