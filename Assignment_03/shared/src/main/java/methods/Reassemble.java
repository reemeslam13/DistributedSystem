package methods;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

import de.uniba.rz.entities.Fragment;
import de.uniba.rz.entities.FragmentHeader;

public class Reassemble {

	
	// For every fragment received, the header(including total number of fragments
	// and fragment's index) for
	// will be stored
	public static List<FragmentHeader> RecievedFragsHeader = new ArrayList<>();

	// for every fragment received, the fragment's data will be stored
	public static ArrayList<Fragment> ReceivedFragsData = new ArrayList<>();

	// Reassembled packets data
	private String ReassembledData;

	public void ReleaseMemory() {
		// Clears List of Fragments; used when server shuts down.
		RecievedFragsHeader.clear();
		ReceivedFragsData.clear();
		this.ReassembledData = "";
	}

	// Look for a Fragment Header
	private FragmentHeader FindFragHeader(String fragment_id) {
		FragmentHeader found = new FragmentHeader();

		if (RecievedFragsHeader.isEmpty())
			return found;
		for (int i = 0; i < RecievedFragsHeader.size(); i++) {
			found = RecievedFragsHeader.get(i);
			if (found.packet_id == fragment_id)
				return found;
		}

		return found;
	}

	private void UpdateFragHeaderList(String packetid) {

		for (int i = 0; i < RecievedFragsHeader.size(); i++) {
			if (Objects.equal(RecievedFragsHeader.get(i).packet_id ,packetid)) {
				int new_recivedpackets_number = RecievedFragsHeader.get(i).received_frags + 1;// increase the number of received
																						// Frags
				RecievedFragsHeader.get(i).received_frags= new_recivedpackets_number;
			}
		} // for
	}

	// check if all fragments of a packet has received
	private boolean AllFragmentsRecieved(String packetid) {
		for (int i = 0; i < RecievedFragsHeader.size(); i++) {
			int totalnumber = RecievedFragsHeader.get(i).total_frags;
			int recieved = RecievedFragsHeader.get(i).received_frags;

			if (Objects.equal(RecievedFragsHeader.get(i).packet_id, packetid) ) {
				return totalnumber == recieved;
			}
		} // for
		return false;
	}

	// Filters Fragments belong to the same packet in ReceivedFragsData
	private ArrayList<Fragment> FilterFragments(String packetid) {
		ArrayList<Fragment> filtered = new ArrayList<Fragment>();
		for (int i = 0; i < ReceivedFragsData.size(); i++) {
			if (Objects.equal(ReceivedFragsData.get(i).packet_id , packetid) ) {
				filtered.add(ReceivedFragsData.get(i));

			}
		} // for
		return filtered;
	}

	// Remove Fragments from both lists
	
	  private void DeleteFragmentsFromList(String packid) { FragmentHeader found =
	  new FragmentHeader(); for (int i = 0; i < RecievedFragsHeader.size(); i++) {
	  found = RecievedFragsHeader.get(i); if (found.packet_id == packid)
	  RecievedFragsHeader.remove(i); } // for
	 
		for (int i = 0; i < ReceivedFragsData.size(); i++) {
			if (ReceivedFragsData.get(i).packet_id == packid) {
				ReceivedFragsData.remove(i);

			}
		} // for
	}

	// all fragments have received, now Reassemble the package, then remove them!
	private void ReassemblePackage(String packid) {
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
	private void NewFragmentHeader(String fragid, int total, int received) {
		FragmentHeader newFragHeader = new FragmentHeader();
		newFragHeader.packet_id=fragid;// id
		newFragHeader.total_frags=total;// total number
		newFragHeader.received_frags=received;// index
		RecievedFragsHeader.add(newFragHeader);
	}

	// Insert new Fragment's Data to the Data List
	private void NewFragmentData(Fragment fragment) {

		ReceivedFragsData.add(fragment);
	}

	private boolean HandleFragment(Fragment fragment) {
		String packetid = fragment.packet_id;
		FragmentHeader recievedfragment = FindFragHeader(packetid);
		//******** check for null
		if (recievedfragment.packet_id==null) {// This is a fragment for a new packet
			NewFragmentHeader(packetid, fragment.total_frags, fragment.frag_index);

		} // if
		else {// This fragment is not for a new packet so update list
			UpdateFragHeaderList(packetid);
		} // else
		NewFragmentData(fragment);// Insert Fragment Data in both Lists
		if (AllFragmentsRecieved(packetid)) {
			ReassemblePackage(packetid);
			return true;
		}

		return false;
	}

	private Fragment TokenizePacket(String content) {
		Fragment f = new Fragment();
		String[] result = content.split(";");

		f.packet_id = result[0];// unique packet id
		f.total_frags = Integer.parseInt(result[1]);
		f.frag_index = Integer.parseInt(result[2]);
		f.frag_data = result[3];

		return f;
	}

	public String HandleLargePacket(DatagramPacket packet) {
		byte[] receivedData = packet.getData();
		String content = new String(receivedData).trim();
		Fragment fragment = TokenizePacket(content);
		if (HandleFragment(fragment)) {
			//byte[] ticketbyte = ReassembledData.getBytes();
			//DatagramPacket ressembeledpacket = new DatagramPacket(ticketbyte, ticketbyte.length);
			return ReassembledData;
		}

		return null;

	}

}
