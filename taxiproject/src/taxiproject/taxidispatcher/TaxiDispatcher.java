package taxiproject.taxidispatcher;

import java.util.ArrayList;

import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.simulation.routeplanner.Journey;
import taxiproject.taxiclientfix.TaxiPosition;
import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

public class TaxiDispatcher extends Block {

	public ArrayList<TaxiPosition> availableTaxis;
	public ArrayList<TaxiPosition> busyTaxis; 
	public ArrayList<TaxiPosition> offDutyTaxis;
	public ArrayList<TaxiOrderPair> pendingOrders;
	
	public ArrayList<Order> orderQueue;
	
	public taxiproject.user.Order Order;
	public boolean finished = false;

	public TaxiDispatcher() {
		availableTaxis = new ArrayList<TaxiPosition>();
		busyTaxis = new ArrayList<TaxiPosition>();
		offDutyTaxis = new ArrayList<TaxiPosition>();
		pendingOrders = new ArrayList<TaxiOrderPair>();
		orderQueue = new ArrayList<Order>();
	}

	// To Taxi Dispatch Console
	public String getOrderInfo(Order order) {
		String message;
		if (order.topic.equals("order")) {
			message = "Order number " + order.id + " is registered from "
					+ order.alias + " to address: " + order.destination;
		} else if (order.topic.equals("taxiConfirmation")) {
			message = "Order number " + order.id + " is confirmed by "
					+ order.assignedTaxi + " to address: " + order.destination
					+ "\n";
		} else if (order.topic.equals("cancel")) {
			message = "Order number " + order.id + " was cancelled by user ("
					+ order.alias + ").";
		} else {
			message = "not any of the wanted topics";
		}
		return message;
	}

	
	public Order confirmToUser(Order order) {
		order.topic = order.alias;
		return order;
	}

	public void printObject(Order order) {
//		System.out.println("Order received at dispatch: " + order.toString());
	}

	// Register taxis on start-up. All taxis are added as off-duty.
	public void regTaxi(String taxiAlias) {
		offDutyTaxis.add(new TaxiPosition(taxiAlias));
		System.out.println(taxiAlias + " is registered in dispatch.");
	}

	// Book taxi
	public void bookTaxi(Order order) { // String taxiAlias
		for (int i = 0; i < pendingOrders.size(); i++) {
			if (pendingOrders.get(i).getTaxiPosition().getTaxiAlias()
					.equals(order.assignedTaxi)) {
				TaxiPosition taxi = pendingOrders.get(i).getTaxiPosition();
				pendingOrders.remove(i); // Taxi has confirmed, is now busy
				busyTaxis.add(taxi);
				System.out.println(taxi + " is now booked for order "
						+ order.id);
			} else {
				System.out.println("Taxi has not been queried for the order.");
			}
		}
		for (int i = 0 ; i < orderQueue.size(); i++) {
			if (order.getId().equals(orderQueue.get(i).getId())) {
				orderQueue.remove(i);
				System.out.println("Taxi is now booked, orderID: " + order.getId() + " and order is removed from orderQueue");
			}
		}
	}

	public Order queryTaxi(Order order) {
		TaxiPosition taxi;
		if (availableTaxis.size() != 0) {
			taxi = availableTaxis.remove(0); // FØRSTE OG BESTE, ENDRES SENERE
			order.assignedTaxi = taxi.getTaxiAlias();
			order.topic = taxi.getTaxiAlias();
			System.out.println("Order " + order.id + " has been queried to "
					+ order.assignedTaxi);
			TaxiOrderPair pendingOrder = new TaxiOrderPair(order.id, taxi);
			pendingOrders.add(pendingOrder);

		} else {
			System.out.println("\nNo available taxis.");
			order.topic = "die";	// Order is still in queue (not removed until bookTaxi())
		}

		return order;
	}

	// Allow taxi to be on duty or off duty.
	public void dutyEdit(TaxiPosition taxi) {
		System.out.println("Duty change for " + taxi.getTaxiAlias() + " registered in dispatch");
		for (int i = 0; i < offDutyTaxis.size(); i++) {	// Taxi is off duty, goes on duty
			if (offDutyTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				offDutyTaxis.remove(i);
				availableTaxis.add(taxi);
				System.out.println(taxi.getTaxiAlias() + " is now AVAILABLE.");
				
				if(!orderQueue.isEmpty()) {
					
				}
				
				return;
			}
		}
		for (int i = 0; i < availableTaxis.size(); i++) { // Taxi is on duty, goes off duty
			if (availableTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				offDutyTaxis.add(availableTaxis.get(i));
				availableTaxis.remove(i);
				System.out.println(taxi.getTaxiAlias() + "is now OFF DUTY.");
				return;
			}
		}
	}

	public boolean isConfirmation(Order order) {
		if (order.confirmed && !order.delete)
			return true;
		return false;
	}

	public String getTaxiAlias(Order order) {
		return order.assignedTaxi;
	}

	public boolean isCancellation(Order order) {
		return order.delete;
	}

	public Order modifyTopic(Order order) { // Modify order topic to taxiAlias
											// and release taxi from order
		if (!order.confirmed) {
			System.out.println("ModifyTopic NULL");
			System.out.println(order.toString());
			for (int i = 0; i < pendingOrders.size(); i++) { // If the order has
																// not yet been
																// confirmed by
																// the Taxi
				System.out.println(pendingOrders.get(i).getTaxiPosition()
						.getTaxiAlias());
				if (order.id.equals(pendingOrders.get(i).getOrderId())) {
					order.topic = pendingOrders.get(i).getTaxiPosition()
							.getTaxiAlias();
					System.out.println("topic: "
							+ pendingOrders.get(i).getTaxiPosition()
									.getTaxiAlias());
					TaxiPosition taxi = pendingOrders.get(i).getTaxiPosition();
					pendingOrders.remove(i); // Release taxi
					availableTaxis.add(taxi);
				}
			}
		} else { // If order is confirmed by taxi
			System.out.println("ModifyTopic = " + order.assignedTaxi);
			order.topic = order.assignedTaxi;
			int taxiIndex = busyTaxis.indexOf(order.assignedTaxi); // Find taxi
			TaxiPosition taxi = busyTaxis.remove(taxiIndex); // Remove taxi from
																// busy taxi
																// list
			availableTaxis.add(taxi); // Taxi is again available
		}
		return order;
	}

	// public String getClosestTaxi(Order order) throws JSONException {
	// String userPos = order.userPos;
	// TaxiPosition taxi;
	// Integer shortestDist;
	// for (int i = 0; i < availableTaxis.size(); i++) { //Get taxi with
	// shortest distance to user
	// taxi = availableTaxis.get(i);
	// String taxiPos = taxi.getTaxiPos();
	//
	// String json =
	// String.format("http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=true",
	// userPos, taxiPos);
	// JSONObject jsonObject = new JSONObject(json);
	//
	// jsonObject.get(key)
	// // if json.distance.value <
	// }
	//
	//
	// return "blabla";
	// }

	public Journey createPickup(Order order) {
		finished = false;
		String taxiPos = "";
		for (int i = 0; i < busyTaxis.size(); i++) {
			if (busyTaxis.get(i).getTaxiAlias().equals(order.assignedTaxi)) {
				taxiPos = busyTaxis.get(i).getTaxiPos();
//				System.out.println(toString());
//				System.out.println("the position of taxi is: "
//						+ busyTaxis.get(i).getTaxiPos());
				break;
			}
		}

		System.out.println("CREATE PICKUP USERPOS: " + order.userPos);
		return new Journey(taxiPos, order.userPos, order.assignedTaxi); // from
																		// taxi
																		// to
																		// user
	}

	public Journey createJourney(Order order) {
		System.out.println("\n\n\nSTARTING JOURNEY");
		System.out.println("order.alias = " + order.alias);
		finished = true;

		System.out.println("CREATE JOURNEY USERPOS: " + order.userPos);
		return new Journey(order.userPos, order.destination, order.assignedTaxi); // from
																					// user
																					// to
																					// destination
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

	public Order orderFinished(Order order) {
		order.completed = true;
		return order;
	}

	public String getAddress(Order order) {
		String address = order.destination;
		address = address.replace(" ", "%20");
		return "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ address + "&sensor=true";
	}

	public void printString(String message) {
		System.out.println("String printed is: " + message);
		getCoordinates(message);
	}

	public MapUpdate endPos(Order order) {
		MapUpdate mu;

		Position p;

		// String[] coordinates = getCoordinates(json);
		//
		// System.out.println("latitude: " + coordinates[0]);
		// System.out.println("longitude: " + coordinates[1]);
		//

		String[] coordinates = order.destination.split(",");

		double latitude = Double.parseDouble(coordinates[0]);
		double longitude = Double.parseDouble(coordinates[1]);

		p = new Position(latitude * 1e6, longitude * 1e6);

		Marker user = Marker.createMarker(order.alias).position(p)
				.hue(Marker.HUE_ROSE);
		Marker taxi = Marker.createMarker(order.assignedTaxi).position(p)
				.hue(Marker.HUE_GREEN);

		mu = new MapUpdate();
		mu.addMarker(user);
		mu.addMarker(taxi);

		return mu;

	}

	// public String[] getCoordinates(String json) {
	//
	// String[] coordinates = new String[2];
	//
	// int index = json.indexOf("location");
	//
	//
	// String latitude = json.substring(index+37, index+47);
	// String longitude = json.substring(index+72, index+82);
	// System.out.println("latitude: " + latitude);
	// System.out.println("longitude: " + longitude);
	//
	// coordinates[0] = latitude;
	// coordinates[1] = longitude;
	//
	//
	// return coordinates;
	// }

	
	//Lars
//	public String[] getCoordinates(String json) {
//
//		int start = json.indexOf("location");
//		int stop = 0;
//		for (int i = start; i < json.length(); i++) {
//			if (json.charAt(i) == '}') {
//				stop = i - 13;
//				break;
//			}
//		}
//
//		String cut = json.substring(start, stop);
//
//		String[] cutArray = cut.split(",");
//
//		int lat = cut.indexOf("lat") + 7;
//		String latitude = cutArray[0].substring(lat);
//		System.out.println(latitude);
//		String longitude = cutArray[1].substring(7);
//
//		String[] coordinates = new String[2];
//		coordinates[0] = latitude;
//		coordinates[1] = longitude;
//
//		return coordinates;
//
//	}

	
	
	
	//Eyvind
	public String[] getCoordinates(String json) {

		int start = json.indexOf("location");
		int stop = 0;
		for (int i = start; i < json.length(); i++) {
			if (json.charAt(i) == '}') {
				stop = i - 13;
				break;
			}
		}

		String cut = json.substring(start, stop);

		String[] cutArray = cut.split(",");

		int lat = cut.indexOf("lat") + 7;
		String latitude = cutArray[0].substring(lat);
		System.out.println(latitude);
		String longitude = cutArray[1].substring(24);

		String[] coordinates = new String[2];
		coordinates[0] = latitude;
		coordinates[1] = longitude;

		return coordinates;

	}
	
	
	
	public Order extractLonLat(Order order, String json) {
		String[] coordinates = getCoordinates(json);

//		System.out.println("latitude: " + coordinates[0]);
//		System.out.println("longitude: " + coordinates[1]);

		order.destination = coordinates[0] + "," + coordinates[1];

		return order;
	}

	public Order releaseTaxi(Order order) {
		int index = 0;

		for (int i = 0; i < busyTaxis.size(); i++) {
			if (busyTaxis.get(i).getTaxiAlias().equals(order.assignedTaxi)) {
				index = i;
				break;
			} else
				System.out.println("RELEASETAXI: TAXI NOT FOUND!");
		}
		TaxiPosition taxi = busyTaxis.get(index);
		System.out.println("RELEASING TAXI: " + taxi.getTaxiAlias()
				+ ". NOW AVAILABLE!");
		taxi.setTaxiPos(order.destination);
		availableTaxis.add(taxi);
		busyTaxis.remove(index);


		order.topic = taxi.getTaxiAlias();
		return order;
	}

	public Order releaseUser(Order order) {
		order.topic = order.alias;
		return order;
	}

	
	
	
	
	
	
	
	//Handling queue of orders
	
	public Order addOrder(Order order) {
		if (!orderQueue.contains(order)) {
			orderQueue.add(order);
			System.out.println("Order added to orderQueue. Size of queue is now: " + orderQueue.size());
		}
		return order;
	}
	
	
	
	
	
	
	
}
