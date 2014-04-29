package taxiproject.taxidispatcher;

import java.util.ArrayList;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;

import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.simulation.routeplanner.Journey;
import taxiproject.taxiclient.TaxiPosition;
import taxiproject.user.Order;

public class TaxiDispatcher extends Block {

	
	public ArrayList<TaxiPosition> availableTaxis;
	public ArrayList<TaxiPosition> busyTaxis; //skal vi heller ta TaxiClient som type? Hvordan linke?
	public ArrayList<TaxiPosition> offDutyTaxis;
	public ArrayList<TaxiOrderPair> pendingOrders;
	public taxiproject.user.Order Order;
	public boolean finished = false;

	
	public TaxiDispatcher() {
		availableTaxis = new ArrayList<TaxiPosition>();
		busyTaxis = new ArrayList<TaxiPosition>();
		offDutyTaxis = new ArrayList<TaxiPosition>();
		pendingOrders = new ArrayList<TaxiOrderPair>();
	}
	
	// To Taxi Dispatch Console
	public String getOrderInfo(Order order) {
		String message;
		if (order.topic.equals("order")) {
			message = "Order number " + order.id + " is registered from " + order.alias + " to address: " + order.destination;
			}
		else if (order.topic.equals("taxiConfirmation")) {
			message = "Order number " + order.id + " is confirmed by " + order.assignedTaxi + " to address: " + order.destination + "\n";
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
		order.topic = order.alias;
		return order;
	}

	public void printObject(Order order) {
		System.out.println("Order received at dispatch: " + order.toString());
	}
	
	// Register taxis on start-up. All taxis are added as off-duty.
	public void regTaxi(String taxiAlias) {
		offDutyTaxis.add(new TaxiPosition(taxiAlias));
		System.out.println(taxiAlias + " is registered in dispatch.");
	}
	
	// Book taxi
	public void bookTaxi(Order order) { //String taxiAlias
		for (int i = 0; i < pendingOrders.size(); i++) {
			if (pendingOrders.get(i).getTaxiPosition().getTaxiAlias().equals(order.assignedTaxi)) {
				TaxiPosition taxi = pendingOrders.get(i).getTaxiPosition();
				pendingOrders.remove(i); // Taxi has confirmed, is now busy
				busyTaxis.add(taxi);
				System.out.println(taxi + " is now booked for order " + order.id);
			} else {
				System.out.println("Taxi has not been queried for the order.");
			}
		}
	}
	
	public Order queryTaxi(Order order) {
		TaxiPosition taxi;
		if (availableTaxis.size() != 0) {
			taxi = availableTaxis.remove(0); // FØRSTE OG BESTE, ENDRES SENERE
			order.assignedTaxi = taxi.getTaxiAlias();
			order.topic = taxi.getTaxiAlias();
			System.out.println("Order " + order.id + " has been queried to " + order.assignedTaxi);
			TaxiOrderPair pendingOrder = new TaxiOrderPair(order.id, taxi);
			pendingOrders.add(pendingOrder);
			
		} else {
			System.out.println("No available taxis.");
		}
		
		return order;
	}
	
	// Allow taxi to be on duty or off duty. 
	public void dutyEdit(TaxiPosition taxi) {
		System.out.println("Duty change for " + taxi.getTaxiAlias() + " registered in dispatch. Pos: " + taxi.getTaxiPos());
		for (int i = 0; i < offDutyTaxis.size(); i++) { // Taxi is off duty
			if (offDutyTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				offDutyTaxis.remove(i);
				availableTaxis.add(taxi);
				System.out.println(taxi.getTaxiAlias() + " is now AVAILABLE.");
				return;
			}
		}
		for (int i = 0; i < availableTaxis.size(); i++) { // Taxi is on duty
			if (availableTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				offDutyTaxis.add(availableTaxis.get(i));
				availableTaxis.remove(i);
				System.out.println(taxi.getTaxiAlias() + "is now OFF DUTY.");
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
				System.out.println(pendingOrders.get(i).getTaxiPosition().getTaxiAlias());
				if (order.id.equals(pendingOrders.get(i).getOrderId())) {
					order.topic = pendingOrders.get(i).getTaxiPosition().getTaxiAlias();
					System.out.println("topic: " + pendingOrders.get(i).getTaxiPosition().getTaxiAlias());
					TaxiPosition taxi = pendingOrders.get(i).getTaxiPosition();
					pendingOrders.remove(i); // Release taxi
					availableTaxis.add(taxi);
				}
			}
		} else { // If order is confirmed by taxi
			System.out.println("ModifyTopic = " + order.assignedTaxi);
			order.topic = order.assignedTaxi; 
			int taxiIndex = busyTaxis.indexOf(order.assignedTaxi); // Find taxi
			TaxiPosition taxi = busyTaxis.remove(taxiIndex); // Remove taxi from busy taxi list
			availableTaxis.add(taxi); // Taxi is again available
		}
		return order;
	}

//	public String getClosestTaxi(Order order) throws JSONException {
//		String userPos = order.userPos;
//		TaxiPosition taxi;
//		Integer shortestDist;
//		for (int i = 0; i < availableTaxis.size(); i++) {  //Get taxi with shortest distance to user
//			taxi = availableTaxis.get(i);
//			String taxiPos = taxi.getTaxiPos();
//			
//			String json = String.format("http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=true", userPos, taxiPos);
//			JSONObject jsonObject = new JSONObject(json);
//			
//			jsonObject.get(key)
//			// if json.distance.value < 
//		}
//		
//		
//		return "blabla";
//	}

	public Journey createPickup(Order order) {
		finished = false;
		String taxiPos = "";
		for (int i = 0; i < busyTaxis.size(); i++) {
			if (busyTaxis.get(i).getTaxiAlias().equals(order.assignedTaxi)) {
				taxiPos = busyTaxis.get(i).getTaxiPos();
				System.out.println(toString());
				System.out.println("the position of taxi is: " + busyTaxis.get(i).getTaxiPos());
				break;
			}
		}
		
//		return new Journey(order.userPos, order.destination, order.assignedTaxi); //from user to destination
		return new Journey(taxiPos,order.userPos,order.assignedTaxi); //from taxi to user
//		return null;
	}

	public Journey createJourney(Order order) {
		System.out.println("STARTING JOURNEY");
		System.out.println("order.alias = " + order.alias);
		finished = true;
		return new Journey(order.userPos, order.destination, order.assignedTaxi); //from user to destination
	}
	
	
	
	public MapUpdate deleteMarker(Order order) {
		MapUpdate u = new MapUpdate();
		Marker m;
		
		m = Marker.createMarker(order.alias);
		m.remove();
		u.addMarker(m);
		return u;
	}
	
	
	
	@Override
	public String toString() {
		return "TaxiDispatcher [availableTaxis=" + availableTaxis
				+ ", busyTaxis=" + busyTaxis + ", offDutyTaxis=" + offDutyTaxis
				+ ", pendingOrders=" + pendingOrders + "]";
	}

	public boolean isFinished() {
		return finished;
	}


	
	

	
}
