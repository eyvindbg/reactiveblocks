package taxiproject.taxidispatcher;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.taxiclient.TaxiClient;
import taxiproject.user.Order;
import taxiproject.userclient.UserClient;

public class TaxiDispatcher extends Block {

	
	public ArrayList<String> availableTaxis;
	public ArrayList<String> busyTaxis; //skal vi heller ta TaxiClient som type? Hvordan linke?
	public ArrayList<String> offDutyTaxis;
	public ArrayList<String> pendingTaxis;
	public ArrayList<TaxiOrderPair> pendingOrders;
	public taxiproject.user.Order Order;

	
	public TaxiDispatcher() {
		availableTaxis = new ArrayList<String>();
		busyTaxis = new ArrayList<String>();
		offDutyTaxis = new ArrayList<String>();
		pendingTaxis = new ArrayList<String>();
		pendingOrders = new ArrayList<TaxiOrderPair>();
	}
	
	// To Taxi Dispatch Console
	public String getOrderInfo(Order order) {
		String message;
		if (order.topic.equals("order")) {
			message = "Order number " + order.id + " is registered from " + order.alias + " to address: " + order.address;
			}
		else if (order.topic.equals("taxiConfirmation")) {
			message = "Order number " + order.id + " is confirmed by " + order.assignedTaxi + " to address: " + order.address + "\n";
		}
		else if (order.topic.equals("cancel")) {
			message = "Order number " + order.id + " was cancelled by user (" + order.alias + ").";
		}
		else {
			message = "not any of the wanted topics";
		}
		return message;
	}


	public Order confirmToUser(Order order) {
		order.topic = String.format("%s", order.alias);
		return order;
	}

	public void printObject(Order order) {
		System.out.println("Order received at dispatch: " + order.toString());
	}
	
	// Register taxis on start-up. All taxis are added as off-duty.
	public void regTaxi(String taxiAlias) {
		offDutyTaxis.add(taxiAlias);
		System.out.println(taxiAlias + " is registered in dispatch.");
	}
	
	// Book taxi
	public void bookTaxi(Order order) { //String taxiAlias
		for (int i = 0; i < pendingOrders.size(); i++) {
			if (pendingOrders.get(i).getTaxiAlias().equals(order.assignedTaxi)) {
				String taxi = pendingOrders.get(i).getTaxiAlias();
				pendingOrders.remove(i); // Taxi has confirmed, is now busy
				busyTaxis.add(taxi);
				System.out.println(taxi + " is now booked for order " + order.id);
			} else {
				System.out.println("Taxi has not been queried for the order.");
			}
		}
	}
	
	public Order queryTaxi(Order order) {
		String taxiAlias = "";
		if (availableTaxis.size() != 0) {
			taxiAlias = availableTaxis.remove(0); // FØRSTE OG BESTE, ENDRES SENERE
			pendingTaxis.add(taxiAlias);
			order.assignedTaxi = taxiAlias;
			order.topic = taxiAlias;
			System.out.println("Order " + order.id + " has been queried to " + order.assignedTaxi);
			TaxiOrderPair pendingOrder = new TaxiOrderPair(order.id, taxiAlias);
			pendingOrders.add(pendingOrder);
			
		} else {
			System.out.println("No available taxis.");
		}
		
		return order;
	}
	
	// Allow taxi to be on duty or off duty. 
	public void dutyEdit(String taxiAlias) {
		System.out.println("Duty change for " + taxiAlias + " registered in dispatch.");
		for (int i = 0; i < offDutyTaxis.size(); i++) {
			if (offDutyTaxis.get(i).equals(taxiAlias)) {
				String taxi = offDutyTaxis.remove(i);
				availableTaxis.add(taxi);
				System.out.println(taxiAlias + " is now AVAILABLE.");
				return;
			}
		}
		for (int i = 0; i < availableTaxis.size(); i++) {
			if (availableTaxis.get(i).equals(taxiAlias)) {
				String taxi = availableTaxis.remove(i);
				offDutyTaxis.add(taxi);
				System.out.println(taxiAlias + "is now OFF DUTY.");
				return;
			}
		}
	}


	public boolean isConfirmation(Order order) {
		if (order.confirmed && !order.delete) return true;
		return false;
	}


	public String getTaxiAlias(Order order) {
		return order.assignedTaxi;
	}


	public boolean isCancellation(Order order) {
		return order.delete;
	}


	public Order modifyTopic(Order order) { // Modify order topic to taxiAlias and release taxi from order
		if (!order.confirmed) {
			System.out.println("ModifyTopic NULL");
			System.out.println(order.toString());
			for (int i = 0; i < pendingOrders.size(); i++) { // If the order has not yet been confirmed by the Taxi
				System.out.println(pendingOrders.get(i).getTaxiAlias());
				if (order.id.equals(pendingOrders.get(i).getOrderId())) {
					order.topic = pendingOrders.get(i).getTaxiAlias();
					System.out.println("topic: " + pendingOrders.get(i).getTaxiAlias());
					String taxi = pendingOrders.get(i).getTaxiAlias();
					pendingOrders.remove(i); // Release taxi
					availableTaxis.add(taxi);
				}
			}
		} else { // If order is confirmed by taxi
			System.out.println("ModifyTopic = " + order.assignedTaxi);
			order.topic = order.assignedTaxi; 
			int taxiIndex = busyTaxis.indexOf(order.assignedTaxi); // Find taxi
			String taxi = busyTaxis.remove(taxiIndex); // Remove taxi from busy taxi list
			availableTaxis.add(taxi); // Taxi is again available
		}
		return order;
	}


	

	
}
