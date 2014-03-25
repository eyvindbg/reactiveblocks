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


	public MapUpdate createMapUpdate() {
		MapUpdate u = new MapUpdate();
		
		Position p1 = new Position(10.39221659117403 * 1e6 , 63.43048084459458 * 1e6);
		u.setCenter(p1);
		u.setZoom(15);
		
		Marker m1 = Marker.createMarker("m1").position(p1).hue(Marker.HUE_MAGENTA);
		u.addMarker(m1);
		
		return u;
	}
	
	
	

	public void printError(String error) {
		System.out.println(error);
	}

}
