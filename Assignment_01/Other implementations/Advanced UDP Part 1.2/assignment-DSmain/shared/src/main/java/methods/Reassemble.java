package methods;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniba.rz.entities.Fragment;

public class Reassemble {

	// For every fragment received, the header(including total number of fragments
	// and fragment's index) for
	// will be stored
	private static List<List<Integer>> RecievedFragsHeader = new ArrayList<>();

	// for every fragment received, the fragment's data will be stored
	private static ArrayList<Fragment> ReceivedFragsData = new ArrayList<>();

	// Reassembled packets data
	private String ReassembledData;

	public void ReleaseMemory() {
		// Clears List of Fragments; used when server shuts down.
		Reassemble.RecievedFragsHeader.clear();
		Reassemble.ReceivedFragsData.clear();
		this.ReassembledData = "";
	}

	// Look for a Fragment Header
	private List<Integer> FindFragHeader(int fragment_id) {
		List<Integer> found = new ArrayList<Integer>();

		if (RecievedFragsHeader.isEmpty())
			return found;
		for (int i = 0; i < RecievedFragsHeader.size(); i++) {
			found = RecievedFragsHeader.get(i);
			if (found.get(0) == fragment_id)
				return found;
		}

		return found;
	}

	private void UpdateFragHeaderList(int fragid) {

		for (int i = 0; i < RecievedFragsHeader.size(); i++) {

			if (RecievedFragsHeader.get(i).get(0) == fragid) {
				int new_recivedpackets_number = RecievedFragsHeader.get(i).get(2) + 1;// increase the number of received
																						// Frags
				RecievedFragsHeader.get(i).set(2, new_recivedpackets_number);
			}
		} // for
	}

	// check if all fragments of a packet has received
	private boolean AllFragmentsRecieved(int fragid) {
		for (int i = 0; i < RecievedFragsHeader.size(); i++) {
			int totalnumber = RecievedFragsHeader.get(i).get(1);
			int recieved = RecievedFragsHeader.get(i).get(2);

			if (RecievedFragsHeader.get(i).get(0) == fragid) {
				return totalnumber == recieved;

			}
		} // for
		return false;
	}

	// Filters Fragments belong to the same packet in ReceivedFragsData
	private ArrayList<Fragment> FilterFragments(int packetid) {
		ArrayList<Fragment> filtered = new ArrayList<Fragment>();
		for (int i = 0; i < ReceivedFragsData.size(); i++) {
			if (ReceivedFragsData.get(i).packet_id == packetid) {
				filtered.add(ReceivedFragsData.get(i));

			}
		} // for
		return filtered;
	}

	// Remove Fragments from both lists
	private void DeleteFragmentsFromList(int packid) {
		List<Integer> found = new ArrayList<Integer>();
		for (int i = 0; i < RecievedFragsHeader.size(); i++) {
			found = RecievedFragsHeader.get(i);
			if (found.get(0) == packid)
				RecievedFragsHeader.remove(i);
		} // for

		for (int i = 0; i < ReceivedFragsData.size(); i++) {
			if (ReceivedFragsData.get(i).packet_id == packid) {
				ReceivedFragsData.remove(i);

			}
		} // for
	}

	// all fragments have received, now Reassemble the package, then remove them!
	private void ReassemblePackage(int packid) {
		StringBuilder wholedata = new StringBuilder();
		ArrayList<Fragment> filtered = FilterFragments(packid);

		for (int i = 0; i < filtered.size(); i++) {
			int filterindex = filtered.get(i).frag_index;
			if (filterindex - 1 == i)
				wholedata.append(filtered.get(i).frag_data);
		} // for

		ReassembledData = wholedata.toString();
		DeleteFragmentsFromList(packid);
	}

	// Insert new Fragment's Header to the Header's List
	private void NewFragmentHeader(int fragid, int total, int received) {
		ArrayList<Integer> newFragHeader = new ArrayList<>();
		newFragHeader.add(fragid);// id
		newFragHeader.add(total);// total number
		newFragHeader.add(received);// index
		RecievedFragsHeader.add(newFragHeader);
	}

	// Insert new Fragment's Data to the Data List
	private void NewFragmentData(Fragment fragment) {

		ReceivedFragsData.add(fragment);
	}

	private boolean HandleFragment(Fragment fragment) {
		int packetid = fragment.packet_id;
		List<Integer> recievedfragment = FindFragHeader(packetid);
		if (recievedfragment.isEmpty()) {// This is a fragment for a new packet
			NewFragmentHeader(packetid, fragment.total_frags, fragment.frag_index);

		} // if
		else {// This fragment is not for a new packet so update list
			UpdateFragHeaderList(packetid);
		} // else
		NewFragmentData(fragment);// Insert Data in both Lists
		if (AllFragmentsRecieved(packetid)) {
			ReassemblePackage(packetid);
			return true;
		}

		return false;
	}

	private Fragment TokenizePacket(String content) {
		Fragment f = new Fragment();
		String[] result = content.split(";");

		f.packet_id = Integer.parseInt(result[0]);
		f.total_frags = Integer.parseInt(result[1]);
		f.frag_index = Integer.parseInt(result[2]);
		f.frag_data = result[3];

		return f;
	}

	public DatagramPacket HandleLargePacket(DatagramPacket packet) {
		byte[] receivedData = packet.getData();
		String content = new String(receivedData).trim();
		Fragment fragment = TokenizePacket(content);
		if (HandleFragment(fragment)) {
			byte[] ticketbyte = ReassembledData.getBytes();
			DatagramPacket ressembeledpacket = new DatagramPacket(ticketbyte, ticketbyte.length);
			return ressembeledpacket;
		}

		return null;

	}

}
