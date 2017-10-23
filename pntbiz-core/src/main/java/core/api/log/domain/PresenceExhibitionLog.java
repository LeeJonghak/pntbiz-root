package core.api.log.domain;

public class PresenceExhibitionLog {
	private Integer logNum;
	private String UUID;
	private Double lat;
	private Double lng;
	private String floor;
	private Integer conNum;
	private Integer fcNum;
	private String evtType;
	private String deviceInfo;
	private String phoneNumber;
	private Integer regDate;
	
	public Integer getLogNum() {
		return logNum;
	}
	public void setLogNum(Integer logNum) {
		this.logNum = logNum;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
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
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public Integer getConNum() {
		return conNum;
	}
	public void setConNum(Integer conNum) {
		this.conNum = conNum;
	}
	public Integer getFcNum() {
		return fcNum;
	}
	public void setFcNum(Integer fcNum) {
		this.fcNum = fcNum;
	}	
	public String getEvtType() {
		return evtType;
	}
	public void setEvtType(String evtType) {
		this.evtType = evtType;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	
}
