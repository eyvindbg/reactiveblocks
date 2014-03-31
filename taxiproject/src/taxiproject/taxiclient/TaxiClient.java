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
	
	public class Tuple { 
		  public final double x; 
		  public final double y; 
		  
		  public Tuple(double x, double y) { 
		    this.x = x; 
		    this.y = y; 
		  } 
	} 
	
	public Tuple[] mapPositions = {new Tuple(63.430300, 10.377515) , new Tuple(63.4304808, 10.394216), 
			new Tuple(63.433180 , 10.394216), new Tuple(63.428916, 10.3923114), new Tuple(63.4348329, 10.4129671), 
			new Tuple(63.4284735, 10.4008918), new Tuple(63.4333946, 10.3990679)};
	
	public int mapPosCounter = 0;
	
	public final String taxi1 = "TaxiMSID0";
	public final String taxi2 = "TaxiMSID1";
	public final String taxi3 = "TaxiMSID2";
	public taxiproject.user.Order Order;
	public boolean onDuty;
	public java.lang.String dutyTopic = "dutyEdit";
	
	
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
		return "You have been assinged order #" + order.id +  ". Address: " + order.address + ". Please confirm.";
	}
	

	public String getOrderTopic(Order order) {
		return order.topic;
	}

	public Order confirmMessage(Order order) {
//		if (order.topic.equals("order")) {
			order.assignedTaxi = String.format("%s", taxiAlias);
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
		Marker m = null;
		String markerID = "";
		if (taxiAlias.equals("TaxiMSID0")) {
			setTaxiAlias("TaxiMSID0");
			m = Marker.createMarker("m0");
		}
		else if (taxiAlias.equals("TaxiMSID1")) {
			setTaxiAlias("TaxiMSID1");
			m = Marker.createMarker("m1");
		}
		else if (taxiAlias.equals("TaxiMSID2")) {
			setTaxiAlias("TaxiMSID2");
			m = Marker.createMarker("m2");
		}
		markerID = m.getId();
		
		if (isOnDuty()) { 
			return(generateMarker(markerID));
		}
		else if (!isOnDuty()) { 
			return deleteMarker(markerID);
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
			Tuple mapPosition = mapPositions[mapPosCounter];
			mapPosCounter++;
			p = new Position(mapPosition.x * 1e6, mapPosition.y * 1e6);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
		}
			
		else if (taxiAlias.equals(taxi2)) {
			p = new Position(63.4304808 * 1e6 , 10.394216 * 1e6); //middle of Trondheim
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
			}
		
		else if (taxiAlias.equals(taxi3)) { 
			p = new Position(63.433180* 1e6 , 10.394216 * 1e6);
			m = Marker.createMarker(markerID).position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
			}
			
		else
			System.out.println("Objekt ikke opprettet ordentlig");
		
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
	
	public String onDuty() {
		if (onDuty) return null;
		onDuty = true;
		return taxiAlias;
	}
	
	public void offDuty() {
		if (!onDuty)
			return;
		onDuty = false;
	}
	
	
	
	public void printTaxiAlias(String taxiAlias) {
		System.out.println(taxiAlias);
	}

//	public void printName(Order order) {
//		System.out.println(order.assignedTaxi);
//	}
	
	
}
