package taxiproject.taxi;

import no.ntnu.item.arctis.runtime.Block;

public class Taxi extends Block {

	public java.lang.String subscription;
	
	public Taxi () {
		this.subscription = "order";
	}

	public String echoTest() {
		System.out.println("Subscription!!!");
		return "Test successful";
	}

}
