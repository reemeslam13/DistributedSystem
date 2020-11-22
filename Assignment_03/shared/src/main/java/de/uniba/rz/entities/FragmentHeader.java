package de.uniba.rz.entities;

public class FragmentHeader {
	public String packet_id; //unique id of the packet this fragment belongs to
	public int total_frags; //total number of fragments for this packet
	public int received_frags; // number of fragments received so far

}
