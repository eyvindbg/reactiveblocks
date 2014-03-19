package taxiproject.taxidispatcher;

import taxiproject.taxiclient.TaxiClient;
import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class TaxiDispatcher extends Block {

	public java.util.ArrayList<TaxiClient> TaxiList;

	public String getOrderTopic(Order order) {
		return order.topic;
	}

}
