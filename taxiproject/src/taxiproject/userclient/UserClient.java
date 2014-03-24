package taxiproject.userclient;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class UserClient extends Block {
	
	
	public static Integer counter = 0;
	public java.lang.String clientAlias;
	public java.lang.String address="";
	public int currentOrder = -1;
	
	
	
	public int getCurrentOrder() {
		if (currentOrder == -1) {
			System.out.println("No orders made yet");
			return 0;
		}
			
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
	

	public Order deleteOrder(int orderNumber) {
		
		
//		order.setDelete(true);
//		return order;
	}

	
}
