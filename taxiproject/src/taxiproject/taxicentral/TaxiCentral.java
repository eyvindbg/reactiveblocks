package taxiproject.taxicentral;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

public class TaxiCentral extends Block {

	public java.lang.String subscriptionTopic = "order,taxi,taxiConfirmation,taxiCreate";
	public java.lang.String publishTopic;
	public java.lang.String serializedString;
	
	public TaxiCentral(){
		this.subscriptionTopic = "order,taxi,taxiConfirmation,taxiCreate";
		
	}

	public void confirmConnection() {
		System.out.println("connected MQTT in taxi central");
	}

	public Order setMessage(Order order) {
		System.out.println("Order: " + order);
		return order;
	}

	public void printError(String error) {
		System.out.println(error);
	}

	
	public Order createConfirmation(Order order) {
		return order;
	}

	public String getOrderTopic(Order order) {
		return order.topic;
	}

	public void printObject(Order order) {
		System.out.println(order.toString());
	}

	public String printTaxi(String taxiname) {
		System.out.println("created: "+taxiname);
		return taxiname;
	}

	public void print(String something) {
		System.out.println("here is " + something);
	}

	public boolean isOrder(String serialized) { //daarlig og hardkokt metode!!!
		if (serialized.equals("TaxiMSID0") || serialized.equals("TaxiMSID1") || serialized.equals("TaxiMSID2"))
			return false;
		return true;
	}

}
