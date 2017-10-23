package core.wms.scanner.domain;

public class ScannerBeaconEdge {
	private String UUID;
	private Integer edgeNum;
	private Integer comNum;
	private String floor;
	private Integer startPoint;
	private Integer endPoint; 
	private String type;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getEdgeNum() {
		return edgeNum;
	}
	public void setEdgeNum(Integer edgeNum) {
		this.edgeNum = edgeNum;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public Integer getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Integer startPoint) {
		this.startPoint = startPoint;
	}
	public Integer getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Integer endPoint) {
		this.endPoint = endPoint;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
