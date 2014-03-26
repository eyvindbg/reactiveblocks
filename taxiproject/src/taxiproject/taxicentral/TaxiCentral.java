package taxiproject.taxicentral;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

public class TaxiCentral extends Block {

	public java.lang.String subscriptionTopic;
	public java.lang.String publishTopic;
	
	public TaxiCentral(){
		this.subscriptionTopic = "order,taxi,taxiConfirmation";
		
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

}
