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
	public ArrayList<String> pendingOrders;
	public taxiproject.user.Order Order;

	
	public TaxiDispatcher() {
		availableTaxis = new ArrayList<String>();
		busyTaxis = new ArrayList<String>();
		offDutyTaxis = new ArrayList<String>();
		pendingTaxis = new ArrayList<String>();
		pendingOrders = new ArrayList<String>();
	}
	
	// For TaxiDispatch to keep track of which taxis are assigned to which orders
	public class TaxiOrderPair {
		public final String orderId;
		public final String taxiAlias;
		
		public TaxiOrderPair(String orderId, String taxiAlias) {
			this.orderId = orderId;
			this.taxiAlias = taxiAlias;
		}
	}
	
	
	// To Taxi Dispatch Console
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


	public Order confirmToUser(Order order) {
		order.topic = String.format("%s", order.alias);
		return order;
	}

//	public Order confirmToTaxi(Order order) {
//		order.topic = "order";
//		return order;
//	}

	public void printObject(Order order) {
		System.out.println("Order received at dispatch: " + order.toString());
	}
	
	// Register taxis on start-up. All taxis are added as off-duty.
	public void regTaxi(String taxiAlias) {
		offDutyTaxis.add(taxiAlias);
		System.out.println(taxiAlias + "is registered in dispatch.");
	}
	
	// Book taxi
	public void bookTaxi(String taxiAlias) {
		if (pendingTaxis.size() != 0) {
			int taxiIndex = pendingTaxis.indexOf(taxiAlias);
			String taxi = pendingTaxis.remove(taxiIndex);
			busyTaxis.add(taxi);
		}
	}
	
	public Order queryTaxi(Order order) {
		String taxiAlias = "";
		if (availableTaxis.size() != 0) {
			taxiAlias = availableTaxis.remove(0);
			pendingTaxis.add(taxiAlias);
		} else {
			System.out.println("No available taxis.");
		}
		
		order.topic = taxiAlias;
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
		if (order.confirmed) return true;
		return false;
		
	}


	public String getTaxiAlias(Order order) {
		return order.assignedTaxi;
	}


	

	
}
