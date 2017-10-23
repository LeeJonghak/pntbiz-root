package core.wms.scanner.domain;

public class ScannerBeaconPos {
	private String UUID;
	private Integer majorVer;
	private Integer minorVer;
	private String macAddr;
	private Float txPower;
	private Integer battery;
	private String beaconName;
	private String floor;
	private Double lat;
	private Double lng;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getMajorVer() {
		return majorVer;
	}
	public void setMajorVer(Integer majorVer) {
		this.majorVer = majorVer;
	}
	public Integer getMinorVer() {
		return minorVer;
	}
	public void setMinorVer(Integer minorVer) {
		this.minorVer = minorVer;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public Float getTxPower() {
		return txPower;
	}
	public void setTxPower(Float txPower) {
		this.txPower = txPower;
	}
	public Integer getBattery() {
		return battery;
	}
	public void setBattery(Integer battery) {
		this.battery = battery;
	}
	public String getBeaconName() {
		return beaconName;
	}
	public void setBeaconName(String beaconName) {
		this.beaconName = beaconName;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
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
	
}
