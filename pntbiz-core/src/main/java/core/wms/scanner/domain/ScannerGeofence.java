package core.wms.scanner.domain;

public class ScannerGeofence {
	
	private String UUID;
	private Integer fcNum;
	private String fcType;
	private String fcShape;
	private String fcName;
	private String floor;
	private Integer evtEnter;
	private Integer evtLeave;
	private Integer evtStay;
	private Integer numEnter;
	private Integer numLeave;
	private Integer numStay;
	private String field1;
	private String field2;
	private String field3;
	private String isNodeEnable;
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getFcNum() {
		return fcNum;
	}
	public void setFcNum(Integer fcNum) {
		this.fcNum = fcNum;
	}
	public String getFcType() {
		return fcType;
	}
	public void setFcType(String fcType) {
		this.fcType = fcType;
	}
	public String getFcShape() {
		return fcShape;
	}
	public void setFcShape(String fcShape) {
		this.fcShape = fcShape;
	}
	public String getFcName() {
		return fcName;
	}
	public void setFcName(String fcName) {
		this.fcName = fcName;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public Integer getEvtEnter() {
		return evtEnter;
	}
	public void setEvtEnter(Integer evtEnter) {
		this.evtEnter = evtEnter;
	}
	public Integer getEvtLeave() {
		return evtLeave;
	}
	public void setEvtLeave(Integer evtLeave) {
		this.evtLeave = evtLeave;
	}
	public Integer getEvtStay() {
		return evtStay;
	}
	public void setEvtStay(Integer evtStay) {
		this.evtStay = evtStay;
	}
	public Integer getNumEnter() {
		return numEnter;
	}
	public void setNumEnter(Integer numEnter) {
		this.numEnter = numEnter;
	}
	public Integer getNumLeave() {
		return numLeave;
	}
	public void setNumLeave(Integer numLeave) {
		this.numLeave = numLeave;
	}
	public Integer getNumStay() {
		return numStay;
	}
	public void setNumStay(Integer numStay) {
		this.numStay = numStay;
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	public String getIsNodeEnable() { return isNodeEnable; }
	public void setIsNodeEnable(String isNodeEnable) { this.isNodeEnable = isNodeEnable; }
}
