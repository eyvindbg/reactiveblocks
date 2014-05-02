package taxiproject.taxicentralfix;

import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.simulation.routeplanner.Journey;
import taxiproject.user.Order;

public class TaxiCentralFix extends Block {

	public java.lang.String subscriptionTopic;
	public java.lang.String publishTopic;
	public java.lang.String messageData;

	
	public TaxiCentralFix(){
		this.subscriptionTopic = "order,taxi,taxiConfirmation,taxiCreate,dutyEdit,cancel,taxiPosition,release";
		
	}

	public void confirmConnection() {
		System.out.println("MQTT CONNECTED: TAXI CENTRAL");
	}

	
	public void printError(String error) {
		System.out.println(error);
	}


	public String getOrderTopic(Order order) {
//		System.out.println("TAXICENTRAL" + order.topic);
		return order.topic;
	}


	public boolean isOrder(String serialized) {
		if (serialized.contains("topic")) return true;
		return false;
	}

	public boolean isDutyEdit(String topic) {
		if (topic.equals("dutyEdit")) {
			return true;
		}
		return false;
	}

	public String getTopic(Journey journey) {
		return journey.getTaxiId();
	}

}
