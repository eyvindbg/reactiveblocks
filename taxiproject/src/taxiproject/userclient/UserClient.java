package taxiproject.userclient;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class UserClient extends Block {
	
	
	public static Integer counter = 0;
	public java.lang.String clientAlias;
	public java.lang.String address;

	
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
		Order order = new Order(alias, counter.toString(), address);
		counter ++;
		return order;
	}

}
