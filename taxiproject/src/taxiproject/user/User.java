package taxiproject.user;

import no.ntnu.item.arctis.runtime.Block;

public class User extends Block {

	public java.lang.String subscriptionTopic;
	public java.lang.String publishTopic;
	
	public String getOrderTopic(Order order) {
		return order.topic;
	}

}
