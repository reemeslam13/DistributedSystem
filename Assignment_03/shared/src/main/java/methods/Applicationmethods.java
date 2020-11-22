package methods;

import de.uniba.rz.entities.MsgPassingMethod;

public class Applicationmethods {
	private static MsgPassingMethod msmethod;
	
	public static void setmethod(MsgPassingMethod method) {
		msmethod= method;
		
	}
	public static MsgPassingMethod getmethod() {
		return msmethod;
	}
	

}
