package core.admin.user.domain;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer comNum;
	private Integer userNum;	
	private String userType;
	private String userID;
	private String userName;	
	private String mobileID;
	private String birth;
	private String gender;	
	private String anniversary;
	private Integer visitCnt;	
	private String agreeLocation;
	private String agreeContents;
	private Integer modDate;
	private Integer regDate;
	
	private String genderText;
	private String agreeLocationText;
	private String agreeContentsText;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public Integer getUserNum() {
		return userNum;
	}
	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileID() {
		return mobileID;
	}
	public void setMobileID(String mobileID) {
		this.mobileID = mobileID;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAnniversary() {
		return anniversary;
	}
	public void setAnniversary(String anniversary) {
		this.anniversary = anniversary;
	}
	public Integer getVisitCnt() {
		return visitCnt;
	}
	public void setVisitCnt(Integer visitCnt) {
		this.visitCnt = visitCnt;
	}
	public String getAgreeLocation() {
		return agreeLocation;
	}
	public void setAgreeLocation(String agreeLocation) {
		this.agreeLocation = agreeLocation;
	}
	public String getAgreeContents() {
		return agreeContents;
	}
	public void setAgreeContents(String agreeContents) {
		this.agreeContents = agreeContents;
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
	public String getGenderText() {
		return genderText;
	}
	public void setGenderText(String genderText) {
		this.genderText = genderText;
	}
	public String getAgreeLocationText() {
		return agreeLocationText;
	}
	public void setAgreeLocationText(String agreeLocationText) {
		this.agreeLocationText = agreeLocationText;
	}
	public String getAgreeContentsText() {
		return agreeContentsText;
	}
	public void setAgreeContentsText(String agreeContentsText) {
		this.agreeContentsText = agreeContentsText;
	}
}
