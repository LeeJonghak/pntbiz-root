package core.wms.scanner.domain;

public class ScannerServer {
	private Integer servNum;
	private Integer comNum;
	private String ftpHost;
	private Integer ftpPort;
	private String ftpID;
	private String ftpPW;
	private String ftpPrivateKey;
	private Integer modDate;
	private Integer regDate;
	
	public Integer getServNum() {
		return servNum;
	}
	public void setServNum(Integer servNum) {
		this.servNum = servNum;
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
	public Integer getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpID() {
		return ftpID;
	}
	public void setFtpID(String ftpID) {
		this.ftpID = ftpID;
	}
	public String getFtpPW() {
		return ftpPW;
	}
	public void setFtpPW(String ftpPW) {
		this.ftpPW = ftpPW;
	}	
	public String getFtpPrivateKey() {
		return ftpPrivateKey;
	}
	public void setFtpPrivateKey(String ftpPrivateKey) {
		this.ftpPrivateKey = ftpPrivateKey;
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
	
}
