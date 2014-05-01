package taxiproject.taxidispatcherfix;

import taxiproject.taxiclientfix.TaxiPosition;

public class TaxiOrderPair {
	public final String orderId;

	public final TaxiPosition taxiPosition;
	
	public TaxiOrderPair(String orderId, TaxiPosition taxiPosition) {
		this.orderId = orderId;
		this.taxiPosition = taxiPosition;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public TaxiPosition getTaxiPosition() {
		return taxiPosition;
	}
	
	
}