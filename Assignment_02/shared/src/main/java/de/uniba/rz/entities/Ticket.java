package de.uniba.rz.entities;

import java.util.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * used Ticket Representation.
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "reporter", "topic", "description", "type","priority","status"})

public class Ticket {

	//private static final long serialVersionUID = -6979364632920616224L;
	@XmlAttribute(required = true)
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
	public Boolean containstext(String text) {
		Boolean result = false;
		text = text.toLowerCase();
		if (topic.toLowerCase().contains(text) || reporter.toLowerCase().contains(text) || description.toLowerCase().contains(text))
			result = true;
		return result;
	}
	public String toJSON() {
		StringBuilder json = new StringBuilder();
		
		json.append("{");
		
		json.append("\"id\":");
		json.append(id);
		json.append(",");
		
		json.append("\"reporter\":\"");
		json.append(reporter);
		json.append("\",");
		
		json.append("\"topic\":\"");
		json.append(topic);
		json.append("\",");
		
		json.append("\"priority\":\"");
		json.append(priority);
		json.append("\",");
		
		json.append("\"status\":\"");
		json.append(status);
		json.append("\",");
		
		json.append("\"type\":\"");
		json.append(type);
		json.append("\",");
		
		json.append("\"Description\":\"");
		json.append(description);
		json.append("\"");
		
		json.append("}");
		
		return json.toString();		
		
		
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
	    for (int start = 0 ,  chunknumber=1; start < text.length(); start += size, chunknumber++) {
	    	String t = text.substring(start, Math.min(text.length(), start + size));
	    	// add ticketid;number of chunks; chucksnumber; to the beginning of the package
	    	
	    	t = 	Integer.toString(this.id)+";"+
	    			Integer.toString(numberofchunks)+";"+
	    			Integer.toString(chunknumber)+";"+
	    			t;
	    	
	    	
	        ret.add(t);
	    }
	    return ret;
	}
	public static Ticket toTicket(String content) {
		int id;
		String reporter;
		String topic;
		String description;
		Type type;
		Priority priority;
		Status status;
		
		//Ticket newTicket = new Ticket();
		
		reporter = content.substring((content.indexOf("reported by:")+13), (content.indexOf(")")));
		//newTicket.setReporter(reporter);	
		//System.out.println("The Reporter is :"+reporter);
		
		String tempId =(content.substring(content.indexOf("#")+1,(content.indexOf(":"))));
		id =Integer.parseInt(tempId);
		//newTicket.setId(id);
		System.out.println("the value of id is: "+id);
		
		topic =content.substring((content.indexOf(":")+2),(content.indexOf(" (")));
		//newTicket.setTopic(topic);
		//System.out.println("The topic is :"+topic);
		
		if (content.indexOf("Description:\n")==-1) {
			description= "";
		}
		else {
		description =(content.substring(content.indexOf("Description:\n")+13));
		}
		//newTicket.setDescription(description);
		//System.out.println("The desc is :"+description);
		
		String tempStatus = content.substring(content.indexOf("Status: ")+8,(content.indexOf("	 Type")));
		status= Status.valueOf(tempStatus);
		//newTicket.setStatus(status);
		System.out.println("the value of status:"+status);
		
		priority = Priority.valueOf(content.substring(content.indexOf("Priority:")+10,(content.indexOf("Description")-1)));
		//newTicket.setPriority(priority);
		System.out.println("The value of prio:"+priority);
		
		String tempType=content.substring(content.indexOf("Type:")+5,(content.indexOf("	 Priority")));
		type= Type.valueOf(tempType);
		//newTicket.setType(type);
		
		Ticket newTicket = new Ticket(id, reporter, topic, description, type, priority,status);
		System.out.println("new ticket"+newTicket);

		
		return newTicket;
	}
	

}
