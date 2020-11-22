package methods;

import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.Type;
import de.uniba.rz.io.rpc.gRPCstatus.tstatus;
import de.uniba.rz.io.rpc.gRPCticket;
import de.uniba.rz.io.rpc.gRPCticket.ticketpriority;
import de.uniba.rz.io.rpc.gRPCticket.tickettype;

public class gRPCmethods {
	//public getType
	public ticketpriority gRPCPriority(Priority priority) {
		switch (priority) {
		case CRITICAL:
			return ticketpriority.CRITICAL;
		case MAJOR:
			return ticketpriority.MAJOR;
		case MINOR:
			return ticketpriority.MINOR;
		default:
			return ticketpriority.UNRECOGNIZED;
		}// switch
	}

	public tickettype gRPCType(Type type) {
		switch (type) {
		case BUG:
			return tickettype.BUG;
		case ENHANCEMENT:
			return tickettype.ENHANCEMENT;
		case QUESTION:
			return tickettype.QUESTION;
		case TASK:
			return tickettype.TASK;
		default:
			return tickettype.UNRECOGNIZED;
		}// switch
	}

	public tstatus gRPCStatus(Status Ticketstatus) {
		switch (Ticketstatus) {
		case ACCEPTED:
			return tstatus.ACCEPTED;
		case CLOSED:
			return tstatus.CLOSED;
		case NEW:
			return tstatus.NEW;
		case REJECTED:
			return tstatus.REJECTED;
		default:
			return tstatus.UNRECOGNIZED;
		}// switch
	}
	
	private Priority TicketPriority(ticketpriority gpriority) {
		switch (gpriority) {
		case CRITICAL:
			return Priority.CRITICAL;
		case MAJOR:
			return Priority.MAJOR;
		case MINOR:
			return Priority.MINOR;
		default:
			return null;
		}// switch
	}

	private  Type TicketType(tickettype gtype) {
		switch (gtype) {
		case BUG:
			return Type.BUG;
		case ENHANCEMENT:
			return Type.ENHANCEMENT;
		case QUESTION:
			return Type.QUESTION;
		case TASK:
			return Type.TASK;
		default:
			return null;
		}// switch
	}

	private Status TicketStatus(tstatus grpcstatus) {
		switch (grpcstatus) {
		case ACCEPTED:
			return Status.ACCEPTED;
		case CLOSED:
			return Status.CLOSED;
		case NEW:
			return Status.NEW;
		case REJECTED:
			return Status.REJECTED;
		default:
			return null;
		}// switch
	}
	
	
 
	public Ticket gRPCTicketToTicket(gRPCticket gTicket) {
		//Ticket(int id, String reporter, String topic, String description, Type type, Priority priority,
		//		Status status)
		
		Ticket ticket = new Ticket(gTicket.getId(),
				gTicket.getReporter(),
				gTicket.getTopic(),
				gTicket.getDescription(),
				TicketType(gTicket.getType()),
				TicketPriority(gTicket.getPriority()),
				TicketStatus(gTicket.getTstatus().getTicketstatus())
				);
		return ticket;
		
	}

}
