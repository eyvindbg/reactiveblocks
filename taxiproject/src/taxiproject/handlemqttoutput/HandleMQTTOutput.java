package taxiproject.handlemqttoutput;

import no.ntnu.item.arctis.runtime.Block;

public class HandleMQTTOutput extends Block {
	
	
	
	public java.lang.String messageData;
	public java.lang.String messageTopic;

	public boolean isOrder(String serialized) { //daarlig og hardkokt metode!!!
		if (serialized.substring(0, 4).equals("Taxi")) return false;
//		if (serialized.equals("TaxiMSID0") || serialized.equals("TaxiMSID1") || serialized.equals("TaxiMSID2")) return false;
		return true;
	}

	public boolean isDutyEdit(String topic) {
		if (topic.equals("dutyEdit")) {
			System.out.println("Taxi duty edit: " + messageData);
			return true;
		}
		return false;
	}
	
	public void printError(String error) {
		System.out.println(error);
	}

}
