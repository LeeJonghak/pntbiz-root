package core.wms.scanner.domain;

public class ScannerDeploy {
	private Integer servNum;
	private Integer depNum;
	private String depType;
	private String depName;
	private String depPath;
	private String depFile;
	private String depContents;
	private StringBuffer depJson;
	private Integer modDate;
	private Integer regDate;
	private Integer comNum;
	private String ftpHost;
	
	public Integer getServNum() {
		return servNum;
	}
	public void setServNum(Integer servNum) {
		this.servNum = servNum;
	}
	public Integer getDepNum() {
		return depNum;
	}
	public void setDepNum(Integer depNum) {
		this.depNum = depNum;
	}		
	public String getDepType() {
		return depType;
	}
	public void setDepType(String depType) {
		this.depType = depType;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getDepPath() {
		return depPath;
	}
	public void setDepPath(String depPath) {
		this.depPath = depPath;
	}
	public String getDepFile() {
		return depFile;
	}
	public void setDepFile(String depFile) {
		this.depFile = depFile;
	}		
	public String getDepContents() {
		return depContents;
	}
	public void setDepContents(String depContents) {
		this.depContents = depContents;
	}		
	public StringBuffer getDepJson() {
		return depJson;
	}
	public void setDepJson(StringBuffer depJson) {
		this.depJson = depJson;
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
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public String getFtpHost() {
		return ftpHost;
	}
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}		
	
}
