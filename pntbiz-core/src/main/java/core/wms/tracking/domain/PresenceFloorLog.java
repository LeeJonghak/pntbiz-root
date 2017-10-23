package core.wms.tracking.domain;

/**
 * Created by nsyun on 17. 9. 22..
 */
public class PresenceFloorLog {

	private Long logNum;

	private String SUUID;

	private String UUID;

	private Integer majorVer;

	private Integer minorVer;

	private String floor;

	private String permitted;

	private String beaconName;

	private String targetName;

	private String logDesc;

	private Integer inDate;

	private Integer outDate;

	private Integer regDate;

	public Long getLogNum() {
		return logNum;
	}

	public void setLogNum(Long logNum) {
		this.logNum = logNum;
	}

	public String getSUUID() {
		return SUUID;
	}

	public void setSUUID(String SUUID) {
		this.SUUID = SUUID;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
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

	public Integer getRegDate() {
		return regDate;
	}

	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
}
