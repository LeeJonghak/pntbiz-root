package core.wms.info.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class LocationRecordSearchParam extends PagingParam {
	private Integer comNum;
	private String sDate;
	private String eDate;
	private String opt;
	private String keyword;	
	
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}	
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
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
		String comNum = StringUtil.NVL(getComNum().toString(), "0");
		String sDate = StringUtil.NVL(getsDate());
		String eDate = StringUtil.NVL(geteDate());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(comNum)) ? "" : "&comNum=" + comNum;
		queryString += ("".equals(sDate)) ? "" : "&sDate=" + sDate;
		queryString += ("".equals(eDate)) ? "" : "&eDate=" + eDate;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
}