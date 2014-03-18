package taxiproject.taxiclient;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class TaxiClient extends Block {
	
	public static String getAlias(String alias) {
		return alias;
	}

	public static String getAlias(Order order) {
		return order.alias;
	}
}
