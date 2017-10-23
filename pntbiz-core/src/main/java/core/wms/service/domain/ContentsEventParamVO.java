package core.wms.service.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class ContentsEventParamVO extends PagingParam {

	private Integer comNum;
	
	private String conType;
	
	private String opt;
	
	private String keyword;
	
	private String eventType;

	public Integer getComNum() {
		return comNum;
	}

	public void setComNum(Integer comNum) {
		this.comNum = comNum;
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getQueryString() {	
		String conType = StringUtil.NVL(getConType());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(conType)) ? "" : "&"+ conType + "=" + conType;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
	
}
