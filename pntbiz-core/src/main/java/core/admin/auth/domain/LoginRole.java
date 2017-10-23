package core.admin.auth.domain;

public class LoginRole {
	
	private int roleNum;
	
	private String roleName;

	private int authCount;
	
	private String regDate;

	public int getRoleNum() {
		return roleNum;
	}

	public void setRoleNum(int roleNum) {
		this.roleNum = roleNum;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getAuthCount() {
		return authCount;
	}

	public void setAuthCount(int authCount) {
		this.authCount = authCount;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}
