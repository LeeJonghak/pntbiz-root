package core.wms.tracking.domain;

public class PresenceTargetSearchParam {
	private Integer comNum;
	private String SUUID;
	private String UUID;
	private Integer majorVer;
    private Integer minorVer;
	private String opt;
	private String keyword;
	private String sDate;
	private String eDate;
	private Long beaconNum;
	
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
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
	public Integer getMajorVer() {
		return majorVer;
	}
	public void setMajorVer(Integer majorVer) {
		this.majorVer = majorVer;
	}
	public Integer getMinorVer() {
		return minorVer;
	}
	public void setMinorVer(Integer minorVer) {
		this.minorVer = minorVer;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}	
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	public Long getBeaconNum() {
		return beaconNum;
	}
	public void setBeaconNum(Long beaconNum) {
		this.beaconNum = beaconNum;
	}		
	
}
