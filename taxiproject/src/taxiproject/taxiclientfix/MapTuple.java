package taxiproject.taxiclientfix;

public class MapTuple { 
	  public final double latitude; 
	  public final double longitude; 
	  
	  public MapTuple(double latitude, double longitude) { 
	    this.latitude = latitude; 
	    this.longitude = longitude; 
	  }

	public double getLat() {
		return latitude;
	}

	public double getLong() {
		return longitude;
	} 
} 