package taxiproject.taxidispatcher;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.taxiclient.TaxiClient;
import taxiproject.user.Order;
import taxiproject.userclient.UserClient;

public class TaxiDispatcher extends Block {

	
	public ArrayList<String> availableTaxis;
	public List<String> busyTaxis; //skal vi heller ta TaxiClient som type? Hvordan linke?

	
	public TaxiDispatcher() {
		availableTaxis = new ArrayList<String>();
		busyTaxis = new ArrayList<String>();
	}
	
	
	
	public String getOrderInfo(Order order) {
		String confirmation;
		if (order.topic.equals("order")) {
			confirmation = "Order number " + UserClient.counter.toString() + " is registered from " + order.alias + " to address: " + order.address;
			}
		else if (order.topic.equals("taxiConfirmation")) {
			confirmation = "Order number " + UserClient.counter.toString() + " is confirmed from " + order.assignedTaxi + "to address " + order.address + "\n";
		}
		else {
			confirmation = "not any of the wanted topics";
		}
		return confirmation;
	}

	public Order findTaxi(Order order) {
		return null;
	}

	public Order confirmToUser(Order order) {
		order.topic = String.format("%s",order.alias);
		return order;
	}

//	public Order confirmToTaxi(Order order) {
//		order.topic = "order";
//		return order;
//	}

	public void printObject(Order order) {
		System.out.println("Order i dispatch: " + order.toString());
	}
	
	
	public void addTaxi(String name) {
		busyTaxis.add(name);
//		System.out.println("lagt til i listen: " +name);
	}

	public String print(String name) {
//		System.out.println("inne i dispatch " +name);
		return name;
	}


	

	
}
