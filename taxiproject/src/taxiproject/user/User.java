package taxiproject.user;

import com.bitreactive.library.android.maps.model.MapUpdate;

import no.ntnu.item.arctis.runtime.Block;

public class User extends Block {

	public java.lang.String subscriptionTopic;
	
	
	public String getOrderTopic(Order order) {
//		System.out.println(order.address);
		System.out.println("'" + order.topic + "'" + " is sent into MQTT on user side from " + order.alias);
		return order.topic;
	}

public MapUpdate printOK(MapUpdate mu) {
		System.out.println("Mapupdate");
		return mu;
	}


	public void printError(String error) {
		System.out.println("error in map: " + error);
	}


}
