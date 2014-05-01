package taxiproject.mapcontroller;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;
import no.ntnu.item.ttm4115.simulation.routeplanner.Journey;

public class MapController extends Block {

	boolean finished = false;
	public taxiproject.user.Order order;
	public no.ntnu.item.ttm4115.simulation.routeplanner.Journey journey;
	

	public Journey createJourney(Order order) {
		System.out.println("\n\n\nSTARTING JOURNEY");
		System.out.println("order.alias = " + order.alias);
		finished = true;

		System.out.println("CREATE JOURNEY USERPOS: " + order.userPos);
		return new Journey(order.userPos, order.destination, order.assignedTaxi); // from user to destination
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
	
	
	public Order extractLonLat(Order order, String json) {
		String[] coordinates = getCoordinates(json);

//		System.out.println("latitude: " + coordinates[0]);
//		System.out.println("longitude: " + coordinates[1]);

		order.destination = coordinates[0] + "," + coordinates[1];

		return order;
	}
	
	
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
	
	
	public MapUpdate endPos(Order order) {
		MapUpdate mu;

		Position p;
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
	
	
	
	public MapUpdate deleteMarker(Order order) {
		MapUpdate u = new MapUpdate();
		Marker m;

		m = Marker.createMarker(order.alias);
		m.remove();
		u.addMarker(m);
		return u;
	}



	public Journey setNotFin(Journey journey) {
		finished = false;
		return journey;
	}
	
	
	
	
	
}
