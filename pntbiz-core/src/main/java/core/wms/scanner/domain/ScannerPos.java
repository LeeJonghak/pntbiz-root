package core.wms.scanner.domain;

public class ScannerPos {

	private String UUID;
	private String macAddr;
	private String majorVer;
	private String scannerName;
	private String sid;
	private Double lat;
	private Double lng;
	private Float rssi;
	private Float srssi;
	private Float mrssi;
	private Float drssi;
	private Float exMeter;
	private Float calPoint;
	private Float maxSig;
	private Float maxBuf;
	private String fwVer;
	private String floor;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}	
	public String getMajorVer() {
		return majorVer;
	}	
	public String getScannerName() {
		return scannerName;
	}
	public void setScannerName(String scannerName) {
		this.scannerName = scannerName;
	}
	public void setMajorVer(String majorVer) {
		this.majorVer = majorVer;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
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
	public Float getRssi() {
		return rssi;
	}
	public void setRssi(Float rssi) {
		this.rssi = rssi;
	}
	public Float getSrssi() {
		return srssi;
	}
	public void setSrssi(Float srssi) {
		this.srssi = srssi;
	}
	public Float getMrssi() {
		return mrssi;
	}
	public void setMrssi(Float mrssi) {
		this.mrssi = mrssi;
	}
	public Float getDrssi() {
		return drssi;
	}
	public void setDrssi(Float drssi) {
		this.drssi = drssi;
	}
	public Float getExMeter() {
		return exMeter;
	}
	public void setExMeter(Float exMeter) {
		this.exMeter = exMeter;
	}
	public Float getCalPoint() {
		return calPoint;
	}
	public void setCalPoint(Float calPoint) {
		this.calPoint = calPoint;
	}
	public Float getMaxSig() {
		return maxSig;
	}
	public void setMaxSig(Float maxSig) {
		this.maxSig = maxSig;
	}
	public Float getMaxBuf() {
		return maxBuf;
	}
	public void setMaxBuf(Float maxBuf) {
		this.maxBuf = maxBuf;
	}
	public String getFwVer() {
		return fwVer;
	}
	public void setFwVer(String fwVer) {
		this.fwVer = fwVer;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	
}
