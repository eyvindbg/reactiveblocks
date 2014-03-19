package taxiproject.taxi;

import no.ntnu.item.arctis.runtime.Block;

public class Taxi extends Block {

	public java.lang.String subscription;
	
	public Taxi () {
		this.subscription = "order";
	}


	public void confirmConnection() {
		System.out.println("connected MQTT in taxi");
	}


	public void printError(String error) {
		System.out.println(error);
		
	}

}
