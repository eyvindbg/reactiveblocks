package taxiproject.taxiclient;

import taxiproject.user.Order;
import no.ntnu.item.arctis.runtime.Block;

public class TaxiClient extends Block {
	

	public java.lang.String taxiAlias;
	public java.lang.String subscription;

	
	public TaxiClient() {
		this.subscription = "order";
	}
	
	public static String getAlias(String alias) {
		return alias;
	}

	public static String getAlias(Order order) {
		return order.alias;
	}

	public void confirmConnection() {
		System.out.println("connected MQTT in taxi Client, taxi client number: " + this.taxiAlias);
	}

	public void printError(String error) {
		System.out.println(error);
	}
}
