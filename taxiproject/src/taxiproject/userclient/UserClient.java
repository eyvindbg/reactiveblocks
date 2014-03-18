package taxiproject.userclient;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class UserClient extends Block {
	
	
	public static Integer counter = 0;
	public java.lang.String alias_client;

	
	public static String getAlias(String alias) {
			return alias;			
	}
	
	public static String getAlias(Order order) {
		return order.alias;
	}
	

	public Order createOrder(String alias) {
		Order order = new Order(alias, counter.toString());
		counter ++;
		return order;
	}

}
