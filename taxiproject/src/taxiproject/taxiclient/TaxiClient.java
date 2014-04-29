package taxiproject.taxiclient;

import java.util.ArrayList;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

public class TaxiClient extends Block {
	

	public String taxiAlias;
	public String subscription;
	public Order order;
	public String topic = "";
	public String taxiPos;
	
	public MapTuple[] mapPositions = {new MapTuple(63.430300, 10.377515) , new MapTuple(63.4304808, 10.394216), 
			new MapTuple(63.433180 , 10.394216), new MapTuple(63.428916, 10.3923114), new MapTuple(63.4348329, 10.4129671), 
			new MapTuple(63.4284735, 10.4008918), new MapTuple(63.4333946, 10.3990679)};
	
	public int mapPosCounter = 0;
	
	public final String taxi1 = "TaxiMSID0";
	public final String taxi2 = "TaxiMSID1";
	public final String taxi3 = "TaxiMSID2";
	public taxiproject.user.Order Order;
	public boolean onDuty;
	public java.lang.String dutyTopic = "dutyEdit";
	public com.bitreactive.library.android.maps.model.MapUpdate markerUpdate;
	
	
	public TaxiClient() {
		this.subscription = String.format("%s", taxiAlias);
		this.topic = "registerTaxi";
	}
	
	public static String getAlias(String alias) {
		return alias;
	}

	public static String getAlias(Order order) {
		return order.alias;
	}

	public void confirmConnection() {
		System.out.println("connected MQTT in Taxi Client, taxi client: " + this.taxiAlias);
	}

	
	public void printError(String error) {
		System.out.println(error);
	}
	
	

	public String printObject(Order order) {
		return "You have been assinged order #" + order.id +  ". Address: " + order.destination + ". Please confirm.";
	}
	

	public String getOrderTopic(Order order) {
		return order.topic;
	}

	public Order confirmMessage(Order order) {
//		if (order.topic.equals("order")) {
		System.out.println("CONFIRM MESSAGE");
			System.out.println(order.toString());
			order.assignedTaxi = String.format("%s", this.taxiAlias);
			order.topic = "taxiConfirmation";
			order.confirmed = true;
//		}
		return order;
	}

	
	
	
		
	
	
	
	
	public java.lang.String getTaxiAlias() {
		return taxiAlias;
	}

	public void setTaxiAlias(java.lang.String taxiAlias) {
		this.taxiAlias = taxiAlias;
	}

	
	
	//Map-operations
	public MapUpdate markerUpdate(String taxiAlias) {

		if (!isOnDuty()) { 
			System.out.println("Marker generated for " + taxiAlias);
			return(generateMarker(taxiAlias));
		}
		else if (isOnDuty()) {
			System.out.println("Marker deleted for " + taxiAlias);
			return deleteMarker(taxiAlias);
		}
		else
			System.out.println("Impossible.");
		return null;
	}
	
	public MapUpdate generateMarker(String markerID) {
		MapUpdate u = new MapUpdate();
		Position p;
		Marker m;
		
		if (taxiAlias.equals(taxi1)) {
			MapTuple mapPosition = mapPositions[mapPosCounter];
			mapPosCounter++;
			p = new Position(mapPosition.getLat() * 1e6, mapPosition.getLong() * 1e6);
//			taxiPos = String.format("%s,%s", mapPosition.getLat() * 1e6, mapPosition.getLong() * 1e6);
			taxiPos = String.format("%s,%s", mapPosition.getLat(), mapPosition.getLong());
			System.out.println(mapPosition.getLat());
			System.out.println("TAXIPOS " + taxiPos);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
		}
			
		else if (taxiAlias.equals(taxi2)) {
			p = new Position(63.4304808 * 1e6 , 10.394216 * 1e6); //middle of Trondheim
			taxiPos = "63.4304808,10.394216";
			System.out.println("TAXIPOS " + taxiPos);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
		}
		
		else if (taxiAlias.equals(taxi3)) { 
			p = new Position(63.433180* 1e6 , 10.394216 * 1e6);
			System.out.println("taxiAlias: " + taxiAlias);
			taxiPos = "63.433180,10.394216";
			System.out.println("TAXIPOS " + taxiPos);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
		}
			
		else System.out.println("Objekt ikke opprettet ordentlig");
		
		return u;
	}

	
	public MapUpdate deleteMarker(String markerID) {
		MapUpdate u = new MapUpdate();
		Marker m;
		
		m = Marker.createMarker(markerID);
		m.remove();
		u.addMarker(m);
		return u;
	}
	
	public boolean isOnDuty() {
		return onDuty;
	}

//	public void onDuty(){
//		if (onDuty)
//			return;
//		onDuty = true;
//	}
	
	public TaxiPosition onDuty() {
		if (onDuty) return null;
		onDuty = true;
		System.out.println("$$$$$$$ " + taxiPos);
		return new TaxiPosition(taxiAlias, taxiPos);
	}
	
	public void offDuty() {
		if (!onDuty)
			return;
		onDuty = false;
	}
	
	
	
	public void printTaxiAlias(String taxiAlias) {
		System.out.println(taxiAlias);
	}

	public String cancelOrder(Order order) {
		return order.id + " was cancelled by user.";
	}

	public boolean isCancellation(Order order) {
		return order.delete;
	}

//	public void printName(Order order) {
//		System.out.println(order.assignedTaxi);
//	}
	
	
}
