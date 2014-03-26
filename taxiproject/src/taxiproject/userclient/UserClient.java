package taxiproject.userclient;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class UserClient extends Block {
	
	public static Integer counter = 0;
	public java.lang.String clientAlias;
	public java.lang.String address="";
	public int currentOrder = -1;
	public java.lang.String subscriptionTopic;

	
	
	
	public UserClient() {
	}
	
	
	
	public int getCurrentOrder() {
		if (currentOrder == -1) {
			System.out.println("No orders made yet");
			return 0;
		}
		System.out.println("current order is: " + currentOrder + " from " + this.clientAlias);
		return currentOrder;
	}

	public void setCurrentOrder(int currentOrder) {
		this.currentOrder = currentOrder;
	}

	public static String getAlias(String alias) {
			return alias;			
	}
	
	public static String getAlias(Order order) {
		return order.alias;
	}
	
	public Order setOrderAddress(Order order, String address) {
		order.address = address;
		return order;
	}
	

	public Order createOrder(String alias) {
		Order order = new Order(alias);
		
		currentOrder = counter;
		
		order.id = counter.toString();
		order.address = address;
		counter ++;
		return order;
	}
	

	
	public String getOrderTopic(Order order) {
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


	public void printError(String error) {
		System.out.println(error);
	}



	public String confirmToUser(Order order) {
		String confirmation;
		if (order.confirmed)
			confirmation = "A taxi with the ID "+ order.assignedTaxi + " is on the way to your address: " + order.address;
		else 
			confirmation = "An order with id: " + order.id + " has been received at the taxi dispatch. Please wait while we find a taxi";
		return confirmation;
	}
	
	
	
//	public Order deleteOrder(int orderNumber) {
//		
//		
//		order.setDelete(true);
//		return order;
//	}

	
}
