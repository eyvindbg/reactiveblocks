package taxiproject.taxiclientfix;

import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

import no.ntnu.item.arctis.runtime.Block;

public class TaxiClientFix extends Block {

	

	public String taxiAlias;
	public String subscription;
	public Order order;
	public String topic = "";
	public String taxiPos;
	public boolean onDutyClicked;
	public boolean offDutyClicked;
	public MapTuple[] mapPositions = {new MapTuple(63.418289, 10.409144), new MapTuple(63.430300, 10.377515) , new MapTuple(63.4304808, 10.394216), 
			new MapTuple(63.433180 , 10.394216), new MapTuple(63.428916, 10.3923114), new MapTuple(63.4348329, 10.4129671), 
			new MapTuple(63.4284735, 10.4008918), new MapTuple(63.4333946, 10.3990679)};
	
	public int mapPosCounter = 0;
	
	public final String taxi1 = "TaxiMSID0";
	public final String taxi2 = "TaxiMSID1";
	public final String taxi3 = "TaxiMSID2";
	public taxiproject.user.Order Order;
	public boolean onDuty = false;
	public java.lang.String dutyTopic = "dutyEdit";
	public com.bitreactive.library.android.maps.model.MapUpdate markerUpdate;
	public boolean activeOrder;
	public boolean alreadyConfirmed;
	
	
	public TaxiClientFix() {
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
	
	public String print(String json){
		System.out.println("JEG ER JO PÅ ANDRE SIDEN OGSÅ!");
		return json;
	}
	

	public String receivedOrder(Order order) {
		activeOrder = true;
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
			onDuty = true;
			System.out.println("Marker generated for " + taxiAlias);
			return(generateMarker(taxiAlias));
		}
		else if (isOnDuty()) {
			onDuty = false;
			System.out.println("Marker deleted for " + taxiAlias);
			return deleteMarker(taxiAlias);
		}
		else
			System.out.println("Impossible.");
		return null;
	}
	
	public MapUpdate generateMarker(String markerID) {
		MapUpdate u = new MapUpdate();
		Marker m;
		MapTuple mapPosition = mapPositions[mapPosCounter];
		Position p = new Position(mapPosition.getLat() * 1e6, mapPosition.getLong() * 1e6);
		mapPosCounter++;
		
		if (taxiAlias.equals(taxi1)) {
			taxiPos = String.format("%s,%s", mapPosition.getLat(), mapPosition.getLong());
			System.out.println(mapPosition.getLat());
			System.out.println("TAXIPOS " + taxiPos);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
		}
			
		else if (taxiAlias.equals(taxi2)) {
//			p = new Position(63.4304808 * 1e6 , 10.394216 * 1e6); //middle of Trondheim
//			taxiPos = "63.4304808,10.394216";
			taxiPos = String.format("%s,%s", mapPosition.getLat(), mapPosition.getLong());
			System.out.println("TAXIPOS " + taxiPos);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
		}
		
		else if (taxiAlias.equals(taxi3)) {
//			p = new Position(63.42500* 1e6 , 10.36585 * 1e6);
//			taxiPos = "63.433180,10.394216";
			taxiPos = String.format("%s,%s", mapPosition.getLat(), mapPosition.getLong());
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

	
	public TaxiPosition getTaxiPos() {
		return new TaxiPosition(taxiAlias, taxiPos);
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

	
	public void onDutyClicked() {
		onDutyClicked = true;
	}
	public void offDutyClicked() {
		offDutyClicked = true;
	}

	public boolean changeDuty(boolean duty) {
		
		if ((duty && onDutyClicked) || (!duty && offDutyClicked)) {
			onDutyClicked = false;
			offDutyClicked = false;
			System.out.println("CHANGEDUTY: INVALID BUTTON ACTION!");
			return false;
		}
		
		else {
			onDutyClicked = false;
			offDutyClicked = false;
			return true;
		}
	}

	public boolean handleConfirm() {
		if (!activeOrder || alreadyConfirmed) {
			System.out.println("CONFIRM: INVALID BUTTON ACTION!");
			return false;
		}
		alreadyConfirmed = true;
		return true;
	}

	public void releaseConfirm(Order order) {
			activeOrder = false;
			alreadyConfirmed = false;
	}

	
	public boolean isCompleted(Order order) {
		return order.completed;
	}

	public void handleDecline() {
		if (!activeOrder)
	}
	
	

	
	
}