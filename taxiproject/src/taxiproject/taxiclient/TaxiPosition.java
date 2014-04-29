package taxiproject.taxiclient;

public class TaxiPosition {

	
	public String taxiPos;
	public String taxiAlias;
	
	
	
	public TaxiPosition(String taxiAlias, String taxiPos) {
		this.taxiAlias = taxiAlias;
		this.taxiPos = taxiPos;
	}

	

	public TaxiPosition(String taxiAlias) {
		this.taxiAlias = taxiAlias;
	}



	public String getTaxiPos() {
		return taxiPos;
	}

	public void setTaxiPos(String taxiPos) {
		this.taxiPos = taxiPos;
	}

	public String getTaxiAlias() {
		return taxiAlias;
	}

	public void setTaxiAlias(String taxiAlias) {
		this.taxiAlias = taxiAlias;
	}
	
	
	
	
	
}
