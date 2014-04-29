package taxiproject.user;

public class Order {
	
	public String id = "";
	public String alias = "";
	public String taxiType = "";
	public String topic = "";
	public String assignedTaxi = null;
	public boolean confirmed = false;
	public boolean delete = false;
	public String userPos = "";
	public String destination = "";
	
	public Order (String alias) {
		this.alias = alias;
		this.topic = "order";
	}

	public boolean isDelete() {
		return delete;
	}
	
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
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

	@Override
	public String toString() {
		return "Order [id=" + id + ", user Position=" + userPos +", destination=" + destination + ", alias=" + alias
				+ ", assignedTaxi=" + assignedTaxi + ", taxiType=" + taxiType + ", topic=" + topic + ", delete="
				+ delete + "]";
	}

	
	
}
