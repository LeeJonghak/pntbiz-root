package core.wms.geofencing.domain;

public class GeofencingGroup {
	
	private Integer comNum;
	private Integer fcGroupNum;
	private String fcGroupName;
	private Integer fcCount;
	private Integer modDate;
    private Integer regDate;    
    
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}	
	public Integer getFcGroupNum() {
		return fcGroupNum;
	}
	public void setFcGroupNum(Integer fcGroupNum) {
		this.fcGroupNum = fcGroupNum;
	}
	public String getFcGroupName() {
		return fcGroupName;
	}
	public void setFcGroupName(String fcGroupName) {
		this.fcGroupName = fcGroupName;
	}
	public Integer getFcCount() {
		return fcCount;
	}
	public void setFcCount(Integer fcCount) {
		this.fcCount = fcCount;
	}
	public Integer getModDate() {
		return modDate;
	}
	public void setModDate(Integer modDate) {
		this.modDate = modDate;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}	
    
}