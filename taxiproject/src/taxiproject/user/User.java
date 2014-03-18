package taxiproject.user;

import no.ntnu.item.arctis.runtime.Block;

public class User extends Block {

	public java.lang.String subscriptionTopic;
	
	public User() {
		this.subscriptionTopic = "taxi,dispatch";
	}
	
	public String getOrderTopic(Order order) {
		return order.topic;
	}

}
