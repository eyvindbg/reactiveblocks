package taxiproject.taxidispatcherfix;

import taxiproject.taxiclientfix.TaxiPosition;

public class TaxiOrderPair {
	public final String orderId;
	public String assignedTaxi;
	public TaxiPosition taxiPosition;
	
	public TaxiOrderPair(String orderId, String assignedTaxi) {
		this.orderId = orderId;
		this.assignedTaxi = assignedTaxi;
	}
	
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

	public String getAssignedTaxi() {
		return assignedTaxi;
	}
	
	
	
}