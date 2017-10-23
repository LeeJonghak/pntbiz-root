package core.admin.calibration.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class CalibrationSearchParam extends PagingParam {
	private Integer calNum;
	private String maker;
	private String telecom;
	private String opt;
	private String keyword;
	
	public Integer getCalNum() {
		return calNum;
	}
	public void setCalNum(Integer calNum) {
		this.calNum = calNum;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	public String getTelecom() {
		return telecom;
	}
	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}	
	public String getQueryString() {	
		String maker = StringUtil.NVL(getMaker());
		String telecom = StringUtil.NVL(getTelecom());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(maker)) ? "" : "&maker=" + maker;
		queryString += ("".equals(telecom)) ? "" : "&telecom=" + telecom;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
		
}
