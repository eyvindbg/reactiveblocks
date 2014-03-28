package taxiproject.taxiclient;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

public class TaxiClient extends Block {
	

	public String taxiAlias;
	public String subscription;
	public Order order;
	public String topic="";
	
	
	
	public final String taxi1 = "TaxiMSID0";
	public final String taxi2 = "TaxiMSID1";
	public final String taxi3 = "TaxiMSID2";
	public taxiproject.user.Order Order;
	public boolean onDuty;
	
	
	public TaxiClient() {
		this.subscription = "order";
		this.topic= "registerTaxi";
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
		return "An order has been placed at address: " + order.address + ". Confirm?";
	}
	

	public String getOrderTopic(Order order) {
		return order.topic;
	}

	public Order confirmMessage(Order order) {
		if (order.topic.equals("order")) {
			order.assignedTaxi = String.format("%s", taxiAlias);
			order.topic = "taxiConfirmation";
			order.confirmed = true;
		}
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
			System.out.println("Impossible");
		return null;
	}
	
	
	
	public MapUpdate generateMarker(String markerID) {
		MapUpdate u = new MapUpdate();
		Position p;
		Marker m;
		
		if (taxiAlias.equals(taxi1)) {
				p = new Position(63.430300 * 1e6, 10.377515 * 1e6);
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

	public void onDuty(){
		if (onDuty)
			return;
		onDuty = true;
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
