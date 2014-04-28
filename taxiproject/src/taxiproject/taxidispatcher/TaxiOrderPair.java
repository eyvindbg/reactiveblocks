package taxiproject.taxidispatcher;

public class TaxiOrderPair {
	public final String orderId;

	public final String taxiAlias;
	
	public TaxiOrderPair(String orderId, String taxiAlias) {
		this.orderId = orderId;
		this.taxiAlias = taxiAlias;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public String getTaxiAlias() {
		return taxiAlias;
	}
	
	
}