package core.wms.contents.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class ContentsMappingSearchParam extends PagingParam {
	private Integer comNum;
	private String refType;
	private String refSubType;
	private String conType;
	private String opt;
	private String keyword;
		
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}	
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getRefSubType() {
		return refSubType;
	}
	public void setRefSubType(String refSubType) {
		this.refSubType = refSubType;
	}
	public String getConType() {
		return conType;
	}
	public void setConType(String conType) {
		this.conType = conType;
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
		String refType = StringUtil.NVL(getRefType());
		String refSubType = StringUtil.NVL(getRefSubType());
		String conType = StringUtil.NVL(getConType());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(refType)) ? "" : "&refType=" + refType;
		queryString += ("".equals(refSubType)) ? "" : "&refSubType=" + refSubType;
		queryString += ("".equals(conType)) ? "" : "&conType=" + conType;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
		
}
