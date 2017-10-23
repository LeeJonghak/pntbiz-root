package core.wms.auth.domain;

public class LoginAuthcode {
	
	// Role 번호
	private int roleNum;
	
	// 권한 번호
	private int authNum;
	
	// 권한 코드
	private String authCode;
	
	// 퀀한명
	private String authName;
	
	// 등록시간
	private String regDate;

	public int getRoleNum() {
		return roleNum;
	}

	public void setRoleNum(int roleNum) {
		this.roleNum = roleNum;
	}

	public int getAuthNum() {
		return authNum;
	}

	public void setAuthNum(int authNum) {
		this.authNum = authNum;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}
