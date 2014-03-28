package taxiproject.taxi;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

import no.ntnu.item.arctis.runtime.Block;

public class Taxi extends Block {

	public java.lang.String subscription;

	
	public Taxi () {
		this.subscription = "order";
	}


	public void confirmConnection() {
		System.out.println("connected MapMQTT in taxi");
	}
	
	public String createTopic() {
		return "taxiCreate";
	}


	 
	public MapUpdate createMapOrigin() {
		MapUpdate u = new MapUpdate();
		
		Position p1 = new Position(63.430480 * 1e6 , 10.392216 * 1e6);
		Marker marker = Marker.createMarker("Map Origin").position(p1).hue(Marker.HUE_ORANGE);
		
		u.setCenter(p1);
		u.setZoom(15);
		
		u.addMarker(marker);
		return u;
	}
	
	public void hei() {
		System.out.println("hei");
	}
	
	

	public void printError(String error) {
		System.out.println(error);
	}


	public void createMovement() {
	}


	public void prepareToSend() {
	}


	public String print(String something) {
		System.out.println(something);
		return something;
	}



}
