package core.admin.company.domain;

public class Company {
	
	private Integer comNum;
	private String UUID;
	private String comName;
	private String comBizNum;
	private String lat;
	private String lng;
	private String oauthEnabled;
	private String regDate;	
	
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getComBizNum() {
		return comBizNum;
	}
	public void setComBizNum(String comBizNum) {
		this.comBizNum = comBizNum;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getOauthEnabled() {
		return oauthEnabled;
	}

	public void setOauthEnabled(String oauthEnabled) {
		this.oauthEnabled = oauthEnabled;
	}

	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}

