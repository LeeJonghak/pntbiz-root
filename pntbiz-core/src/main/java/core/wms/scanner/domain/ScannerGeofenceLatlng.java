package core.wms.scanner.domain;

public class ScannerGeofenceLatlng {
	
	private Integer fcNum;
	private Double lat;
	private Double lng;
	private Integer orderSeq;
	private Integer radius;	
	
	public Integer getFcNum() {
		return fcNum;
	}
	public void setFcNum(Integer fcNum) {
		this.fcNum = fcNum;
	}
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
	
}
