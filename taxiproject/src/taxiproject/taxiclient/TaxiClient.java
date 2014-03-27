package taxiproject.taxiclient;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class TaxiClient extends Block {
	

	public java.lang.String taxiAlias;
	public java.lang.String subscription;
	public taxiproject.user.Order Order;
	public boolean onDuty;

	
	public TaxiClient() {
		this.subscription = "order";
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
	
	
//	public MapUpdate createMapUpdate() {
//		MapUpdate u = new MapUpdate();
//		
//		Position p1 = new Position(10.39221659117403 * 1e6 , 63.43048084459458 * 1e6);
//		u.setCenter(p1);
//		u.setZoom(15);
//		
//		Marker m1 = Marker.createMarker("m1").position(p1).hue(Marker.HUE_MAGENTA);
//		u.addMarker(m1);
//		
//		return u;
//	}

	public String printObject(Order order) {
		return "An order has been placed at address: " + order.address + ". Confirm?";
	}
	

	public String getOrderTopic(Order order) {
		return order.topic;
	}

	public Order confirmToDispatch(Order order) {
		order.assignedTaxi = String.format("%s", taxiAlias);
		order.topic = "taxiConfirmation";
		order.confirmed = true;
		return order;
	}

	
	
	public String markerUpdate(String taxiAlias) {
		Marker m;
		
		if (taxiAlias.equals("TaxiMSID0")) {
			m = Marker.createMarker("m1");
		}
		return m;

	
	}
	
	
		
	
	
	
	
	public MapUpdate generateMarker(String taxiAlias, String markerID) {
		MapUpdate u = new MapUpdate();
		Position p;
		Marker m;
		
		if (taxiAlias.equals("TaxiMSID0")) {
			if (isOnDuty()) {
				p = new Position(63.430300 * 1e6, 10.377515 * 1e6);
				m = Marker.createMarker("m1").position(p).hue(Marker.HUE_GREEN);
				m.description(String.format("%s",this.taxiAlias));
				u.addMarker(m);
			}
			else {
				m = Marker.createMarker("m1");
				m.remove();
				u.addMarker(m);
				return u;
			}
		}
		
		else if (taxiAlias.equals("TaxiMSID1")) {
			p = new Position(63.4304808 * 1e6 , 10.394216 * 1e6); //middle of Trondheim
			m = Marker.createMarker("m2").position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
			}
			
		
		
		else if (taxiAlias.equals("TaxiMSID2")) { 
			p = new Position(63.433180* 1e6 , 10.394216 * 1e6);
			m = Marker.createMarker("m3").position(p).hue(Marker.HUE_GREEN);
			m.description(String.format("%s",this.taxiAlias));
			u.addMarker(m);
			}
			
		else
			System.out.println("Objekt ikke opprettet ordentlig");
//		System.out.println(taxiAlias);
		
		return u;
		}

	
	public MapUpdate deleteMarker(String taxiAlias) {
		MapUpdate u = new MapUpdate();
		Marker m;
		
		m = Marker.createMarker("m1");
		m.remove();
		u.addMarker(m);
		return u;
	}
	
	
	
	
	public boolean isOnDuty() {
		return onDuty;
	}

	public void onDuty(){
		onDuty = true;
	}
	
	public void offDuty() {
		onDuty = false;
	}
	
	
	
	public void printTaxiAlias(String taxiAlias) {
		System.out.println(taxiAlias);
	}

//	public void printName(Order order) {
//		System.out.println(order.assignedTaxi);
//	}
	
	
}
