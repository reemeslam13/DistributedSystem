package de.uniba.rz.entities;

import java.io.Serializable;

import java.util.*;

/**
 * used Ticket Representation.
 * 
 */
public class Ticket implements Serializable {

	private static final long serialVersionUID = -6979364632920616224L;

	private int id;
	private String reporter;
	private String topic;
	private String description;
	private Type type;
	private Priority priority;
	private Status status;

	public Ticket() {}
	
	public Ticket(int id, String reporter, String topic, String description, Type type, Priority priority) {
		super();
		this.id = id;
		this.reporter = reporter;
		this.topic = topic;
		this.description = description;
		this.type = type;
		this.priority = priority;
		this.setStatus(Status.NEW);
	}

	public Ticket(int id, String reporter, String topic, String description, Type type, Priority priority,
			Status status) {
		super();
		this.id = id;
		this.reporter = reporter;
		this.topic = topic;
		this.description = description;
		this.type = type;
		this.priority = priority;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public String getReporter() {
		return reporter;
	}

	public Status getStatus() {
		return status;
	}

	public String getTopic() {
		return topic;
	}

	public Type getType() {
		return type;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Ticket #" + id + ": " + topic + " (reported by: " + reporter + ")\n" + "Status: " + status + "\t Type:"
				+ type + "\t Priority: " + priority + "\n" + "Description:\n" + description;
	}

	@Override
	public Object clone() {
		return new Ticket(this.id, this.reporter, this.topic, this.description, this.type, this.priority, this.status);
	}
	
	public  List<String> splitEqually(int size) {
	    // Give the list the right capacity to start with.
		String text = this.toString();
		
	   
	    int numberofchunks =(text.length() + size - 1) / size;
	    List<String> ret = new ArrayList<String>(numberofchunks);
	    String uniqueID = UUID.randomUUID().toString();
	    for (int start = 0 ,  chunknumber=1; start < text.length(); start += size, chunknumber++) {
	    	String t = text.substring(start, Math.min(text.length(), start + size));
	    	// add ticketid;number of chunks; chucksnumber; to the beginning of the package
	    	
	    	t = 	uniqueID+";"+
	    			Integer.toString(numberofchunks)+";"+
	    			Integer.toString(chunknumber)+";"+
	    			t;
	    	
	    	
	        ret.add(t);
	    }
	    return ret;
	}
	
	

}
