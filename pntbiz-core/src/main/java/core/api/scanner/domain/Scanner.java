package core.api.scanner.domain;

public class Scanner {
	
	private Integer scannerNum;
	private Integer comNum;	
	private String macAddr;
	private Integer majorVer;
	private String scannerName;
	private Double lat;
	private Double lng;
	private Integer rssi;
	private String floor;
	private String modDate;
	private String regDate;
	private String UUID;
	
	public Integer getScannerNum() {
		return scannerNum;
	}
	public void setScannerNum(Integer scannerNum) {
		this.scannerNum = scannerNum;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}	
	public Integer getMajorVer() {
		return majorVer;
	}
	public void setMajorVer(Integer majorVer) {
		this.majorVer = majorVer;
	}
	public String getScannerName() {
		return scannerName;
	}
	public void setScannerName(String scannerName) {
		this.scannerName = scannerName;
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
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
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
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}		
}
