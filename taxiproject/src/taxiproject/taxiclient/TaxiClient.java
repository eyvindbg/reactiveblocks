package taxiproject.taxiclient;

import no.ntnu.item.arctis.runtime.Block;

public class TaxiClient extends Block {

	public java.lang.String taxiAlias;
	public java.lang.String subscription;
	public taxiproject.user.Order Order;
	public boolean onDuty;
	public java.lang.String dutyTopic;
	public com.bitreactive.library.android.maps.model.MapUpdate markerUpdate;
}