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
	
	public String userPos;
	public String userAddress;
	
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
		
		order.userPos = this.userPos;
		
		return order;
	}

	
	public String getTopic(Order order) {
//		System.out.println(order.address);
//		System.out.println("'"+order.topic + "'" + " is sent into MQTT on user side from " + order.alias);
		return order.topic;
	}

	public String getUserPos() {
		return userPos;
	}


	public void setUserPos(String userPos) {
		this.userPos = userPos;
	}


	public void confirmConnection() {
		System.out.println("MQTT CONNECTED: " + this.clientAlias);
	}



	public String createTopic() {
//		System.out.println(String.format("taxi,dispatch,%s", clientAlias));
		return String.format("taxi,dispatch,%s", clientAlias);
	}

	public String confirmToUser(Order order) {
		String confirmation;
		if (order.confirmed) {
			confirmation = order.assignedTaxi + " is on its way to you. Your order number is: " + order.id;
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
			System.out.println("Marker generated for " + userAlias);
			p = new Position(63.42291 * 1e6, 10.39428 * 1e6);
			userPos = "63.42291,10.39428";
			userAddress = "Samfundet";
			m = Marker.createMarker(userAlias).position(p).hue(Marker.HUE_ROSE);
			m.description(String.format("%s",this.userAlias));
			u.addMarker(m);
		}
			
		else if (userAlias.equals(user2)) {
			System.out.println("Marker generated for " + userAlias);
			p = new Position(63.44888 * 1e6 , 10.44381 * 1e6); 
			userPos = "63.44888,10.44381";
			userAddress = "Lade";
			m = Marker.createMarker(userAlias).position(p).hue(Marker.HUE_ROSE);
			m.description(String.format("%s",this.userAlias));
			u.addMarker(m);
			}
		else
			System.out.println("Object does not exist");
		
		return u;
		}


	public boolean isConfirmed(Order order) {
		return order.confirmed;
	}


	public boolean isCompleted(Order order) {
		return order.completed;
	}

	
	
	public void updateUserPos(Order order) {
		this.setUserPos(order.destination);
	}


	public boolean isValidOrder(Order order) {
		boolean validOrder = currentOrder != null && !order.isDelete();
		
		if (!validOrder)
			System.out.println("INVALID BUTTON ACTION: CANCEL");
		return validOrder;
	}


	public String getCancelError() {
		return "INVALID BUTTON ACTION: CANCEL";
	}


	public String getDestination(Order order) {
		return order.destination;
	}


	public String getLocation() {
		return userAddress;
	}


	public boolean isQueued(Order order) {
		return order.getQueue() > -1 && !order.completed;
	}


	public String userIsQueued(Order order) {
		return "No available taxis at the moment. Your place in the queue: " + order.getQueue();
	}


	
	
	
	
}
