package taxiproject.taxicentral;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

public class TaxiCentral extends Block {

	public java.lang.String subscriptionTopic;
	public java.lang.String publishTopic;
	
	public TaxiCentral(){
		this.subscriptionTopic = "order,taxi,dispatch";
		
	}

	public void confirmConnection() {
		System.out.println("connected MQTT in taxi central");
	}

	public Order setOrderTopic(Order order, String topic) {
		order.topic = topic;
		return order;
	}

	public void printError(String error) {
		System.out.println(error);
	}

	public void messageToTaxi() {
	}

}
