package taxiproject.taxidispatcher;

import no.ntnu.item.arctis.runtime.Block;
import taxiproject.taxiclient.TaxiClient;
import taxiproject.user.Order;
import taxiproject.userclient.UserClient;

public class TaxiDispatcher extends Block {

	public java.util.ArrayList<TaxiClient> TaxiList;

	public String getOrderInfo(Order order) {
		String confirmation = "Order number " + UserClient.counter.toString() + " is registered from " + order.alias + " to address: " + order.address;
		return confirmation;
	}

}
