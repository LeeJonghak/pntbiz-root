package core.api.service.domain;

public class Attendance {

	private Integer logNum;
	private String UUID;
	private String sidNum;
	private String sName;
	private String subject;
	private String sPhoneNumber;
	private String attdDate;
	private Integer regDate;
	private Integer modDate;
	
	public Integer getLogNum() {
		return logNum;
	}
	public void setLogNum(Integer logNum) {
		this.logNum = logNum;
	}	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getSidNum() {
		return sidNum;
	}
	public void setSidNum(String sidNum) {
		this.sidNum = sidNum;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}	
	public String getsPhoneNumber() {
		return sPhoneNumber;
	}
	public void setsPhoneNumber(String sPhoneNumber) {
		this.sPhoneNumber = sPhoneNumber;
	}
	public String getAttdDate() {
		return attdDate;
	}
	public void setAttdDate(String attdDate) {
		this.attdDate = attdDate;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	public Integer getModDate() {
		return modDate;
	}
	public void setModDate(Integer modDate) {
		this.modDate = modDate;
	}	
	
}
