package core.wms.info.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class LocationInquirySearchParam extends PagingParam {
	private Integer comNum;
	private String sDate;
	private String eDate;
	private String os;
	private String service;
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
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
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
		String os = StringUtil.NVL(getOs());
		String service = StringUtil.NVL(getService());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(comNum)) ? "" : "&comNum=" + comNum;
		queryString += ("".equals(sDate)) ? "" : "&sDate=" + sDate;
		queryString += ("".equals(eDate)) ? "" : "&eDate=" + eDate;
		queryString += ("".equals(os)) ? "" : "&os=" + os;
		queryString += ("".equals(service)) ? "" : "&service=" + service;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
}