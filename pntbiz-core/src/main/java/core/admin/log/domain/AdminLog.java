package core.admin.log.domain;

public class AdminLog {
	private Integer logNum;
	private String adminID;
	private String tableName;
	private String crudType;
	private String pk;
	private Integer regDate;
	
	public Integer getLogNum() {
		return logNum;
	}
	public void setLogNum(Integer logNum) {
		this.logNum = logNum;
	}	
	public String getAdminID() {
		return adminID;
	}
	public void setAdminID(String adminID) {
		this.adminID = adminID;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCrudType() {
		return crudType;
	}
	public void setCrudType(String crudType) {
		this.crudType = crudType;
	}		
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
}