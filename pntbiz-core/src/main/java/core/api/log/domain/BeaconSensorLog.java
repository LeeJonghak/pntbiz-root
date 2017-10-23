package core.api.log.domain;

public class BeaconSensorLog {
	private Integer logNum;
	private String SUUID;
	private String UUID;
	private String collector;
	private Double lat;
	private Double lng;
	private String floor;
	private Double co2;
	private Double o2;
	private Double dust;
	private Double temperature;
	private Double humidity;
	private Double battery;
	private Integer regDate;
	
	public Integer getLogNum() {
		return logNum;
	}
	public void setLogNum(Integer logNum) {
		this.logNum = logNum;
	}
	public String getSUUID() {
		return SUUID;
	}
	public void setSUUID(String sUUID) {
		SUUID = sUUID;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}	
	public String getCollector() {
		return collector;
	}
	public void setCollector(String collector) {
		this.collector = collector;
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
	public Double getCo2() {
		return co2;
	}
	public void setCo2(Double co2) {
		this.co2 = co2;
	}	
	public Double getO2() {
		return o2;
	}
	public void setO2(Double o2) {
		this.o2 = o2;
	}
	public Double getDust() {
		return dust;
	}
	public void setDust(Double dust) {
		this.dust = dust;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public Double getBattery() {
		return battery;
	}
	public void setBattery(Double battery) {
		this.battery = battery;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	
}