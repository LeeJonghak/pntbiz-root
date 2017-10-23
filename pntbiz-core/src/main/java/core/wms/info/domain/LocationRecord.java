package core.wms.info.domain;

public class LocationRecord {
	private Integer logNum;
	private Integer comNum;
	private String recType;
	private String recID;
	private String reqName;
	private String opt;
	private String useDesc;
	private String remoteIP;
	private Integer regDate;
	
	public Integer getLogNum() {
		return logNum;
	}
	public void setLogNum(Integer logNum) {
		this.logNum = logNum;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public String getRecType() {
		return recType;
	}
	public void setRecType(String recType) {
		this.recType = recType;
	}
	public String getRecID() {
		return recID;
	}
	public void setRecID(String recID) {
		this.recID = recID;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}	
	public String getUseDesc() {
		return useDesc;
	}
	public void setUseDesc(String useDesc) {
		this.useDesc = useDesc;
	}
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	
}
