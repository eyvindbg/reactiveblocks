package taxiproject.userclient;

import com.bitreactive.library.android.maps.model.MapUpdate;
import com.bitreactive.library.android.maps.model.Marker;
import com.bitreactive.library.android.maps.model.Position;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class UserClient extends Block {
	
	public static Integer counter = 0;
	public java.lang.String clientAlias;
	public Order currentOrder = null;
	public String subscriptionTopic;
	
	public String position;

	public static Integer userCounter = 0;
	
	
	public UserClient() {
		
	}
	
	
	public static String getAlias(String alias) {
			return alias;			
	}
	
	public static String getAlias(Order order) {
		return order.alias;
	}

	public Order createOrder(String clientAlias) {
		Order order = new Order(clientAlias);
		order.id = counter.toString();
		order.destination = destination;
		counter ++;
		currentOrder = order;
		
		order.userPos = this.position;
		
		return order;
	}

	
	public String getTopic(Order order) {
//		System.out.println(order.address);
//		System.out.println("'"+order.topic + "'" + " is sent into MQTT on user side from " + order.alias);
		return order.topic;
	}

	public void confirmConnection() {
		System.out.println("connected MQTT in User Client, user client: " + this.clientAlias);
	}



	public String createTopic() {
//		System.out.println(String.format("taxi,dispatch,%s", clientAlias));
		return String.format("taxi,dispatch,%s", clientAlias);
	}

	public String confirmToUser(Order order) {
		String confirmation;
		if (order.confirmed) {
			confirmation = order.assignedTaxi + " is on the way to your address: " + order.destination + ". Your order number is: " + order.id;
			currentOrder = order;
		}
		else 
			confirmation = "An order with id: " + order.id + " has been received at the taxi dispatch. Please wait while we find a taxi";
		return confirmation;
	}
	

	public Order cancelOrder(Order order) { // Assume that one user only has _one_ order at a time
		order.setDelete(true);
		order.topic = "cancel";
		System.out.println("Order " + order.id + " was cancelled by user. Assigned taxi: " + order.assignedTaxi);
		return order;
	}



	public String cancelExec(Order order) {
		return "You cancelled your order: " + currentOrder.id;
	}

	String userAlias; //create this field
	public final String user1 = "UserMSID0";
	public final String user2 = "UserMSID1";
	public java.lang.String address;
	public java.lang.String destination;
	
	
	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}
	
	
	
	//Map-operations

	
	public MapUpdate markerUpdate(String userAlias) {
		MapUpdate u = new MapUpdate();
		Position p;
		Marker m;
		
		if (userAlias.equals(user1)) {
			System.out.println(userAlias + " added to map.");
			p = new Position(63.42291 * 1e6, 10.39428 * 1e6);
			this.position = "63.42291,10.39428";
			m = Marker.createMarker(userAlias).position(p).hue(Marker.HUE_ROSE);
			m.description(String.format("%s",this.userAlias));
			u.addMarker(m);
		}
			
		else if (userAlias.equals(user2)) {
			System.out.println(userAlias + " added to map.");
			p = new Position(63.42769 * 1e6 , 10.39657 * 1e6); 
			this.position = "63.42769,10.39657";
			m = Marker.createMarker(userAlias).position(p).hue(Marker.HUE_ROSE);
			m.description(String.format("%s",this.userAlias));
			u.addMarker(m);
			}
		else
			System.out.println("Object does not exist");
		
		return u;
		}

	
}
