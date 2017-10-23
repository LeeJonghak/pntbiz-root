package core.wms.auth.domain;

public class LoginOtp {
	
	private String userID;
	private String otpNum;
	private Integer regDate;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getOtpNum() {
		return otpNum;
	}
	public void setOtpNum(String otpNum) {
		this.otpNum = otpNum;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	
}