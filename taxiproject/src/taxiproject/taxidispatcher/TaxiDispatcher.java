package taxiproject.taxidispatcher;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.simulation.routeplanner.Journey;
import sun.misc.IOUtils;
import taxiproject.taxiclient.TaxiPosition;
import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

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
			taxi = availableTaxis.remove(0); // F�RSTE OG BESTE, ENDRES SENERE
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

	public Order orderFinished(Order order) {
		order.completed = true;
		order.topic = String.format("%s,%s", order.alias, order.assignedTaxi);
		return order;
	}

	public MapUpdate endPos(Order order, String json) {
		MapUpdate mu;
		
		Position p;
		
		String[] coordinates = getCoordinates(json);
		
		System.out.println("latitude: " + coordinates[0]);
		System.out.println("longitude: " + coordinates[1]);
		
		double latitude = Double.parseDouble(coordinates[0]);
		double longitude = Double.parseDouble(coordinates[1]);
		
		p = new Position(latitude * 1e6, longitude * 1e6);
		
		Marker user = Marker.createMarker(order.alias).position(p).hue(Marker.HUE_ROSE);
		Marker taxi = Marker.createMarker(order.assignedTaxi).position(p).hue(Marker.HUE_GREEN);
		
		mu = new MapUpdate();
//		mu.addMarker(user);
		mu.addMarker(taxi);
		
		return mu;
		
	}
	
	
	public String[] getCoordinates(String json) {
		
		String[] coordinates = new String[2];
		
		int index = json.indexOf("location");
		
		
		String latitude = json.substring(index+37, index+47);
		String longitude = json.substring(index+72, index+82);
		System.out.println("latitude: " + latitude);
		System.out.println("longitude: " + longitude);
		
		coordinates[0] = latitude;
		coordinates[1] = longitude;
		
		
		return coordinates;
	}
	
	
	
	

	public String getAddress(Order order) {
		String address = order.destination;
		address = address.replace(" ", "%20");
		return "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=true";
	}

	public void printString(String message) {
		System.out.println("String printed is: " +message);
		getCoordinates(message);
	}


	
	
	
	
	
	
	
	
	
	
	
//	
//	/*Geocode request URL. Here see we are passing "json" it means we will get the output in JSON format.
//	* You can also pass "xml" instead of "json" for XML output.
//	* For XML output URL will be "http://maps.googleapis.com/maps/api/geocode/xml"; 
//	*/
//
//	private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json"; 
//
//	/*
//	* Here the fullAddress String is in format like "address,city,state,zipcode". Here address means "street number + route" .
//	*
//	*/
//	
//	public String getJSONByGoogle(String fullAddress) {
//
//		/*
//		* Create an java.net.URL object by passing the request URL in constructor. 
//		* Here you can see I am converting the fullAddress String in UTF-8 format. 
//		* You will get Exception if you don't convert your address in UTF-8 format. Perhaps google loves UTF-8 format. :)
//		* In parameter we also need to pass "sensor" parameter.
//		* sensor (required parameter) � Indicates whether or not the geocoding request comes from a device with a location sensor. This value must be either true or false.
//		*/
//		URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");
//
//		// Open the Connection 
//		URLConnection conn = url.openConnection();
//
//		//This is Simple a byte array output stream that we will use to keep the output data from google. 
//		ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
//
//		// copying the output data from Google which will be either in JSON or XML depending on your request URL that in which format you have requested.
//		IOUtils.copy(conn.getInputStream(), output);
//
//		//close the byte array output stream now.
//		output.close();
//
//		return output.toString(); // This returned String is JSON string from which you can retrieve all key value pair and can save it in POJO.
//		}
//		}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
