package core.api.beacon.domain;

public class Beacon {

	private Integer beaconNum=0;
	private Integer comNum;
	private String UUID;
	private Integer majorVer;
	private Integer minorVer;
	private String macAddr;
	private Float txPower;
	private Integer battery;
	private String beaconName;
	private String beaconDesc;
	private String floor;
	private Double lat;
	private Double lng;
	private String lastDate;
	private String modDate;
	private String regDate;
	private String externalId;

	public Integer getBeaconNum() {
		return beaconNum;
	}
	public void setBeaconNum(Integer beaconNum) {
		this.beaconNum = beaconNum;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
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
	public String getBeaconDesc() {
		return beaconDesc;
	}
	public void setBeaconDesc(String beaconDesc) {
		this.beaconDesc = beaconDesc;
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
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	public String getModDate() {
		return modDate;
	}
	public void setModDate(String modDate) {
		this.modDate = modDate;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
