package core.wms.geofencing.domain;

public class GeofencingGroupMapping {
	
	private Integer fcGroupNum;
	private Long fcNum;	
	private Integer fromFcGroupNum;
	private Long fromFcNum;
	
	public Integer getFcGroupNum() {
		return fcGroupNum;
	}
	public void setFcGroupNum(Integer fcGroupNum) {
		this.fcGroupNum = fcGroupNum;
	}
	public Long getFcNum() {
		return fcNum;
	}
	public void setFcNum(Long fcNum) {
		this.fcNum = fcNum;
	}
	public Integer getFromFcGroupNum() {
		return fromFcGroupNum;
	}
	public void setFromFcGroupNum(Integer fromFcGroupNum) {
		this.fromFcGroupNum = fromFcGroupNum;
	}
	public Long getFromFcNum() {
		return fromFcNum;
	}
	public void setFromFcNum(Long fromFcNum) {
		this.fromFcNum = fromFcNum;
	}	
    
}