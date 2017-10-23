package core.wms.tracking.domain;

public class PresenceSetmap {
	
	private Integer comNum;
	private String mapType;
	private Integer initZoom;
	private String initFloor;
	private Integer checkTimeInterval;
	private Integer removeTimeInterval;
	private Integer moveTimeInterval;
	private Float moveUnit;
	private String modDate;
	private String regDate;
	
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}		
	public String getMapType() {
		return mapType;
	}
	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
	public Integer getInitZoom() {
		return initZoom;
	}
	public void setInitZoom(Integer initZoom) {
		this.initZoom = initZoom;
	}	
	public String getInitFloor() {
		return initFloor;
	}
	public void setInitFloor(String initFloor) {
		this.initFloor = initFloor;
	}
	public Integer getCheckTimeInterval() {
		return checkTimeInterval;
	}
	public void setCheckTimeInterval(Integer checkTimeInterval) {
		this.checkTimeInterval = checkTimeInterval;
	}
	public Integer getRemoveTimeInterval() {
		return removeTimeInterval;
	}
	public void setRemoveTimeInterval(Integer removeTimeInterval) {
		this.removeTimeInterval = removeTimeInterval;
	}
	public Integer getMoveTimeInterval() {
		return moveTimeInterval;
	}
	public void setMoveTimeInterval(Integer moveTimeInterval) {
		this.moveTimeInterval = moveTimeInterval;
	}	
	public Float getMoveUnit() {
		return moveUnit;
	}
	public void setMoveUnit(Float moveUnit) {
		this.moveUnit = moveUnit;
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
	
}