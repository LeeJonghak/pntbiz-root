package core.wms.scanner.domain;

public class ScannerBeaconNode {	
	private String UUID;
	private Integer nodeNum;
	private Integer comNum;
	private Integer nodeID;
	private String nodeName;
	private String floor;
	private Double lat;
	private Double lng;
		
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getNodeNum() {
		return nodeNum;
	}
	public void setNodeNum(Integer nodeNum) {
		this.nodeNum = nodeNum;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}	
	public Integer getNodeID() {
		return nodeID;
	}
	public void setNodeID(Integer nodeID) {
		this.nodeID = nodeID;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
