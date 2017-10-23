package core.common.code.domain;

public class Code {
	
	private String gCD;
	private String sCD;
	private String gName;
	private String sName;
	private String langCode;

	private String pgCD; // 변경시 사용
	private String psCD; // 변경시 사용
	
	public String getgCD() {
		return gCD;
	}
	public void setgCD(String gCD) {
		this.gCD = gCD;
	}
	public String getsCD() {
		return sCD;
	}
	public void setsCD(String sCD) {
		this.sCD = sCD;
	}
	public String getgName() {
		return gName;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getPgCD() {
		return pgCD;
	}
	public void setPgCD(String pgCD) {
		this.pgCD = pgCD;
	}
	public String getPsCD() {
		return psCD;
	}
	public void setPsCD(String psCD) {
		this.psCD = psCD;
	}
	
	
}
