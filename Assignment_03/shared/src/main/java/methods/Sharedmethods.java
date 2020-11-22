package methods;

import java.io.FileWriter;
import java.io.IOException;
import de.uniba.rz.entities.Priority;
import de.uniba.rz.entities.Status;
import de.uniba.rz.entities.Ticket;
import de.uniba.rz.entities.Type;

public class Sharedmethods {
	public Ticket toTicket(String s) {

		int id;
		String reporter;
		String topic;
		String description;
		Type type;
		Priority priority;
		Status status;
		try {

			s = s.replace(")", "");
			s = s.replace("(", "");
			String[] ticketparts = s.split(":");
			id = Integer.parseInt(ticketparts[0].split("#")[1].trim());
			topic = ticketparts[1].split("reported by")[0].trim();
			reporter = ticketparts[2].split("\nStatus")[0].trim();
			status = Status.valueOf(ticketparts[3].split("Type")[0].trim());
			type = Type.valueOf(ticketparts[4].split("Priority")[0].trim());
			priority = Priority.valueOf(ticketparts[5].split("\nDesc")[0].trim());
			if (ticketparts.length<7) description = "";
			else description = ticketparts[6].trim();
			Ticket ticket = new Ticket(id, reporter, topic, description, type, priority,status);

			return ticket;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void log(String logfilename, String msg)  {
		
		FileWriter logfile;
		try {
			String timestamp = new java.util.Date().toString();
			logfile = new FileWriter(logfilename);
			logfile.write(timestamp+": "+msg+"\n");
			logfile.close();
		} catch (IOException e) {
			System.out.println("Can not create log file");
			System.out.println();
			
			return;
		}

	} 

}
