package core.wms.tracking.domain;

public class PresenceGfLog {
	private Integer logNum;
	private String SUUID;
	private String UUID;
	private Integer majorVer;
	private Integer minorVer;
	private Integer fcNum;
	private String fcName;
	private String floor;
	private String permitted;
	private String beaconName;
	private String targetName;
	private String logDesc;
	private Integer inDate;
	private Integer outDate;
	private Integer regDate;
	private Integer fcNumCnt;
	private String viewDate;


	private Integer enterCount;
	private Integer leaveCount;
	private Integer stayCount;

	public Integer getInDate() {
        return inDate;
    }
    public void setInDate(Integer inDate) {
        this.inDate = inDate;
    }
    public Integer getOutDate() {
        return outDate;
    }
    public void setOutDate(Integer outDate) {
        this.outDate = outDate;
    }
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
	public Integer getFcNum() {
		return fcNum;
	}
	public void setFcNum(Integer fcNum) {
		this.fcNum = fcNum;
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

	public String getPermitted() {
		return permitted;
	}

	public void setPermitted(String permitted) {
		this.permitted = permitted;
	}

	public String getBeaconName() {
		return beaconName;
	}
	public void setBeaconName(String beaconName) {
		this.beaconName = beaconName;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getLogDesc() {
		return logDesc;
	}
	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	public Integer getFcNumCnt() {
		return fcNumCnt;
	}
	public void setFcNumCnt(Integer fcNumCnt) {
		this.fcNumCnt = fcNumCnt;
	}
	public String getViewDate() {
		return viewDate;
	}
	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}
	public Integer getEnterCount() {
		return enterCount;
	}
	public void setEnterCount(Integer enterCount) {
		this.enterCount = enterCount;
	}
	public Integer getLeaveCount() {
		return leaveCount;
	}
	public void setLeaveCount(Integer leaveCount) {
		this.leaveCount = leaveCount;
	}
	public Integer getStayCount() {
		return stayCount;
	}
	public void setStayCount(Integer stayCount) {
		this.stayCount = stayCount;
	}

}
