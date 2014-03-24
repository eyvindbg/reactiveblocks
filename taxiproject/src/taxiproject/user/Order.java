package taxiproject.user;

public class Order {
	
	public String id="";
	public String address="";
	public String alias="";
	public String taxiType="";
	public String topic="";
	
	public Order (String alias) {
		this.alias = alias;
		this.topic = "order";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTaxiType() {
		return taxiType;
	}
	public void setTaxiType(String taxiType) {
		this.taxiType = taxiType;
	}
	public String getAlias() {
		return alias;
	}

}
