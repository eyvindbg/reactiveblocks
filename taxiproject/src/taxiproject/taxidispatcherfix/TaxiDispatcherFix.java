package taxiproject.taxidispatcherfix;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.simulation.routeplanner.Journey;
import taxiproject.taxiclientfix.TaxiPosition;
import taxiproject.user.Order;

public class TaxiDispatcherFix extends Block {

	public List<TaxiPosition> availableTaxis;
	public List<TaxiPosition> busyTaxis;
	public List<TaxiPosition> offDutyTaxis;
	public List<TaxiOrderPair> pendingOrders;
	public List<TaxiOrderPair> declinedOrders;
	public List<Order> orderQueue;

	public taxiproject.user.Order Order;
	public boolean finished = false;
	public int shortest = -1;
	
	public TaxiPosition closestTaxi;
	public TaxiPosition currentTaxi;
	private int index;
	public taxiproject.taxiclientfix.TaxiPosition taxi;

	public TaxiDispatcherFix() {
		availableTaxis = new ArrayList<TaxiPosition>();
		busyTaxis = new ArrayList<TaxiPosition>();
		offDutyTaxis = new ArrayList<TaxiPosition>();
		pendingOrders = new ArrayList<TaxiOrderPair>();
		orderQueue = new ArrayList<Order>();
		declinedOrders = new ArrayList<TaxiOrderPair>();
	}

	// To Taxi Dispatch Console
	public String getOrderInfo(Order order) {
		String message;
		if (order.getTopic().equals("order")) {
			message = "Order number " + order.getId() + " is registered from "
					+ order.alias + " to address: " + order.destination;
		} else if (order.getTopic().equals("taxiConfirmation")) {
			message = "Order number " + order.getId() + " is confirmed by "
					+ order.assignedTaxi + " to address: " + order.destination
					+ "\n";
		} else if (order.getTopic().equals("cancel")) {
			message = "Order number " + order.getId() + " was cancelled by user ("
					+ order.alias + ").";
		} else if (order.getTopic().equals("decline")) {
			message = "Order number " + order.getId() + " was declined by taxi (" + order.assignedTaxi + ")";
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

	// Register taxis on start-up. All taxis are added as off-duty.
	public void regTaxi(String taxiAlias) {
		offDutyTaxis.add(new TaxiPosition(taxiAlias));
		System.out.println("DISPATCH REGISTERED: " + taxiAlias);
	}

	// Book taxi
	public void bookTaxi(Order order) { // String taxiAlias
		for (int i = 0; i < pendingOrders.size(); i++) {
			if (pendingOrders.get(i).getTaxiPosition().getTaxiAlias()
					.equals(order.assignedTaxi)) {
				TaxiPosition taxi = pendingOrders.get(i).getTaxiPosition();
				pendingOrders.remove(i); // Taxi has confirmed, is now busy
				busyTaxis.add(taxi);
			} else {
				System.out.println("Taxi has not been queried for the order.");
			}
		}
	}

	public Order queryTaxi(Order order) {
		TaxiPosition taxi;
		
		shortest = -1;
		index = 0;
		if (availableTaxis.size() != 0) {
			taxi = closestTaxi;
			order.assignedTaxi = taxi.getTaxiAlias();
			order.topic = taxi.getTaxiAlias();
			System.out.println("\n\n\n" + order.assignedTaxi + " HAS BEEN QUERIED FOR ORDER #" + order.getId());
			TaxiOrderPair pendingOrder = new TaxiOrderPair(order.id, taxi);
			pendingOrders.add(pendingOrder);
			availableTaxis.remove(taxi);

			for (int i = 0; i < orderQueue.size(); i++) {
				if (order.getId().equals(orderQueue.get(i).getId())) {
					orderQueue.remove(i);
					System.out.println(order.assignedTaxi + " is queried for order #"
							+ order.getId()
							+ ". The order is removed from queue. Length of queue: " + orderQueue.size());
				}
			}

		} else {
			System.out.println("\nNo available taxis.");
			order.topic = "die"; // Order is still in queue (not removed until
									// bookTaxi())
		}

		return order;
	}

	// Allow taxi to be on duty or off duty.
	public void dutyEdit(TaxiPosition taxi) {
		for (int i = 0; i < offDutyTaxis.size(); i++) { // Taxi is off duty, goes on duty
			if (offDutyTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				offDutyTaxis.remove(i);			
				availableTaxis.add(taxi);
				System.out.println(taxi.getTaxiAlias() + " is now AVAILABLE.");
				return;
			}
		}
		for (int i = 0; i < availableTaxis.size(); i++) { // Taxi is on duty, goes off duty
			if (availableTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				offDutyTaxis.add(availableTaxis.get(i));
				availableTaxis.remove(i);
				System.out.println(taxi.getTaxiAlias() + " is now OFF DUTY.");
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
			System.out.println(order.toString());
			for (int i = 0; i < pendingOrders.size(); i++) { // If the order has
																// not yet been
																// confirmed by
																// the Taxi
				if (order.id.equals(pendingOrders.get(i).getOrderId())) {
					order.topic = pendingOrders.get(i).getTaxiPosition()
							.getTaxiAlias();
					TaxiPosition taxi = pendingOrders.get(i).getTaxiPosition();
					pendingOrders.remove(i); // Release taxi
					availableTaxis.add(taxi);
				}
			}
		} else { // If order is confirmed by taxi
			order.topic = order.assignedTaxi;
			int taxiIndex = busyTaxis.indexOf(order.assignedTaxi); // Find taxi
			TaxiPosition taxi = busyTaxis.remove(taxiIndex); // Remove taxi from
																// busy taxi
																// list
			availableTaxis.add(taxi); // Taxi is again available
		}
		return order;
	}

	public Journey createPickup(Order order) {
		finished = false;
		String taxiPos = "";
		for (int i = 0; i < busyTaxis.size(); i++) {
			if (busyTaxis.get(i).getTaxiAlias().equals(order.assignedTaxi)) {
				taxiPos = busyTaxis.get(i).getTaxiPos();
				break;
			}
		}

		System.out.println("\n\n\nSTARTING PICKUP FOR (" + order.alias + ", " + order.assignedTaxi + ")");
		return new Journey(taxiPos, order.userPos, order.assignedTaxi); // from
																		// taxi
																		// to
																		// user
	}

	@Override
	public String toString() {
		return "TaxiDispatcher [availableTaxis=" + availableTaxis
				+ ", busyTaxis=" + busyTaxis + ", offDutyTaxis=" + offDutyTaxis
				+ ", pendingOrders=" + pendingOrders + "]";
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
				+ ". NOW AVAILABLE!\n\n\n");
		taxi.setTaxiPos(order.destination);
		availableTaxis.add(taxi);
		busyTaxis.remove(taxi);

		order.topic = taxi.getTaxiAlias();
		return order;
	}

	public Order releaseUser(Order order) {
		order.topic = order.alias;
		return order;
	}

	
	// Handling queue of orders

	public Order addOrder(Order order) {
		
		if (orderQueue.size() == 0 && !order.confirmed) {
			orderQueue.add(order);
		}
		
		else {
		
			for (int i = 0; i < orderQueue.size(); i++) {
				if (!order.getId().equals(orderQueue.get(i).getId()) && !order.confirmed) {
					orderQueue.add(order);
					break;
				}
			}
		}
		System.out.println("Order #" + order.getId() + " added to queue. Size of queue is now: " + orderQueue.size());
		return order;
	}

	public boolean isRelease(Order order) {
		return order.topic.equals("release");
	}

	
	public int getDistance(String json) {
		int start = json.indexOf("value");
		int stop = 0;
		for (int i = start; i < json.length(); i++) {
			if (json.charAt(i) == '}') {
				stop = i;
				break;
			}
		}

		String cut = json.substring(start, stop);
		String distance = "";

		for (int i = 0; i < cut.length(); i++) {
			if (Character.isDigit(cut.charAt(i)))
				distance += cut.charAt(i);
		}

//		System.out.println("index is: " + index + ", distance is: " +distance + ", from taxi: "+availableTaxis.get(index).taxiAlias +
//				 "\n. The shortest distance is: " + shortest + ", which is taxi: " + closestTaxi);
		return Integer.parseInt(distance);
	}


	public String generateRequest(Order order) {
		String userPos = order.userPos;
		String taxiPos = availableTaxis.get(index).getTaxiPos();
		currentTaxi = availableTaxis.get(index);
		
		String result = String.format("http://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&sensor=true",userPos, taxiPos);
		result = result.replace(" ", "%20");
		return result;
		
	}

	public void checkDistance(int present) {
		if (shortest == -1 || present < shortest) {
			shortest = present;
			closestTaxi = currentTaxi;
		}
		index++;
	}

	public boolean notFin() {
		return index < availableTaxis.size();
	}
	
	public boolean availableTaxi(Order order) {
		boolean passOrder = false;
		
		if (order.getQueue() == -1 && !availableTaxis.isEmpty()) passOrder = true;
		else if (order.getQueue() != -1) passOrder = true;
		else if (order.confirmed) passOrder = true;
		
		return passOrder;
	}

	public Order notifyUser(Order order) {
		order.setTopic(order.getAlias());
		order.setQueue(orderQueue.size());;
		return order;
	}

	public boolean pickFromQueue(TaxiPosition taxi) {
		boolean goingOffDuty = false;
		boolean queueEmpty = orderQueue.isEmpty();
		
		boolean pickFromQueue = false;
		
		for (int i = 0; i < availableTaxis.size(); i++) { // Taxi is on duty, goes off duty
			if (availableTaxis.get(i).getTaxiAlias().equals(taxi.getTaxiAlias())) {
				goingOffDuty = true;
			}
		}
		
		if (!goingOffDuty && !queueEmpty) {
			pickFromQueue = true;
		}
		
		return pickFromQueue;
	}

	public Order getQueuedOrder(TaxiPosition taxi) {
		Order order = orderQueue.get(0);
		pendingOrders.add(new TaxiOrderPair(order.getId(), taxi));
		
		System.out.println("\n\n\n" + taxi.getTaxiAlias() + " HAS BEEN QUERIED FOR ORDER #" + order.getId());
		
		order.assignedTaxi = taxi.getTaxiAlias();
		order.topic = taxi.getTaxiAlias();
		
		return order;
	}

	public boolean isDecline(Order order) {
		boolean orderIsDeclined = order.isDecline();
		order.setDecline(false);
		if (orderIsDeclined) System.out.println("\n\n\n" + order.assignedTaxi + " DECLINED ORDER #" + order.getId());
		return orderIsDeclined;
	}


	public boolean isAvailableTaxi() {
		return !availableTaxis.isEmpty();
	}

	public void addDeclinedOrder(Order order) {
		for (int i = 0; i < orderQueue.size(); i++) {
			if (order.getId().equals(orderQueue.get(i).getId())) {
				orderQueue.remove(i);
				break;
			}
		}
		orderQueue.add(0, order);
	}

	public void releaseDeclinedTaxi(Order order) {
		TaxiPosition taxi;
		
		if (order.isDecline()) {
			for (int i = 0; i < pendingOrders.size(); i++) {
				if (order.getId().equals(pendingOrders.get(i).getOrderId())) {
					taxi = pendingOrders.get(i).getTaxiPosition();
					pendingOrders.remove(order);
					availableTaxis.add(taxi);
					
				System.out.println(order.assignedTaxi + "DECLINED ORDER #" + order.getId() + ", AND IS NOW RELEASED.");
				}
		else System.out.println("No taxi to remove");
			}
		}
	}
	
}
