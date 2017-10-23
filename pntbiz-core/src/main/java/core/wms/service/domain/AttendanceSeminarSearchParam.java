package core.wms.service.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class AttendanceSeminarSearchParam extends PagingParam {
	private Integer comNum;
	private String UUID;
	private String attdDate;
	private String opt;
	private String keyword;
		
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
	public String getAttdDate() {
		return attdDate;
	}
	public void setAttdDate(String attdDate) {
		this.attdDate = attdDate;
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
		String attdDate = StringUtil.NVL(getAttdDate());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(attdDate)) ? "" : "&attdDate=" + attdDate;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
		
}
