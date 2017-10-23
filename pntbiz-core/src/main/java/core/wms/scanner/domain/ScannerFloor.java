package core.wms.scanner.domain;

public class ScannerFloor {
	private Integer floorNum;
	private String floor;
	private String floorName;
	private Double swLat;
	private Double swLng;
	private Double neLat;
	private Double neLng;
	private Float deg;
	private String imgSrc;
	private String imgURL;
	
	public Integer getFloorNum() {
		return floorNum;
	}
	public void setFloorNum(Integer floorNum) {
		this.floorNum = floorNum;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public Double getSwLat() {
		return swLat;
	}
	public void setSwLat(Double swLat) {
		this.swLat = swLat;
	}
	public Double getSwLng() {
		return swLng;
	}
	public void setSwLng(Double swLng) {
		this.swLng = swLng;
	}
	public Double getNeLat() {
		return neLat;
	}
	public void setNeLat(Double neLat) {
		this.neLat = neLat;
	}
	public Double getNeLng() {
		return neLng;
	}
	public void setNeLng(Double neLng) {
		this.neLng = neLng;
	}
	public Float getDeg() {
		return deg;
	}
	public void setDeg(Float deg) {
		this.deg = deg;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	
}
