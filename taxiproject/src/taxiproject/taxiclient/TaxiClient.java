package taxiproject.taxiclient;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.user.Order;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

public class TaxiClient extends Block {
	

	public java.lang.String taxiAlias;
	public java.lang.String subscription;
	public taxiproject.user.Order Order;

	
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
	
	
	public MapUpdate createMapUpdate() {
		MapUpdate u = new MapUpdate();
		
		Position p1 = new Position(10.39221659117403 * 1e6 , 63.43048084459458 * 1e6);
		u.setCenter(p1);
		u.setZoom(15);
		
		Marker m1 = Marker.createMarker("m1").position(p1).hue(Marker.HUE_MAGENTA);
		u.addMarker(m1);
		
		return u;
	}

	public String printObject(Order order) {
		return "An order has been placed at address: " + order.address + ". Confirm?";
	}

	public String getOrderTopic(Order order) {
		return order.topic;
	}

	public Order confirmToDispatch(Order order) {
		order.topic = "taxiConfirmation";
		return order;
	}
	
	
}
