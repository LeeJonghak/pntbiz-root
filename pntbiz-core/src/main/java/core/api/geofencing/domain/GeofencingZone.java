package core.api.geofencing.domain;

public class GeofencingZone extends Geofencing {
	
	private Double lat;
    private Double lng;
    private Integer orderSeq;
    private Integer radius;    
    
	/**
     * ZONE타입
     * TB_GEOFENCING_BPZONE > zoneType
     *
     * @author nohsoo 2015-03-10
     */    
    private String zoneType;
    
	public Double getLat() {
		return lat;
	}	
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Integer getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
	public Integer getRadius() {
		return radius;
	}
	public void setRadius(Integer radius) {
		this.radius = radius;
	}
	public String getZoneType() {
		return zoneType;
	}
	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}    	
    
}
