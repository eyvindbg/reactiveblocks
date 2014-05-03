package taxiproject.user;

public class Order {
	
	public String id = "";
	public String alias = "";
	public String taxiType = "";
	public String topic = "";
	public String assignedTaxi = null;
	public boolean confirmed = false;
	public boolean delete = false;
	public boolean decline = false;

	public String userPos = "";
	public String destination = "";
	public boolean completed;
	public int queue = -1;
	

	public boolean isDecline() {
		return decline;
	}
	
	public void setDecline(boolean decline) {
		this.decline = decline;
	}

	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

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
