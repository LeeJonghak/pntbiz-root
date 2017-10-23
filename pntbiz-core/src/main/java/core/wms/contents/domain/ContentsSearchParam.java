package core.wms.contents.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class ContentsSearchParam extends PagingParam {
	private Integer comNum;
	private String conType;
	private String conType2;
	private String opt;
	private String keyword;
		
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
	public String getConType2() {
		return conType2;
	}
	public void setConType2(String conType2) {
		this.conType2 = conType2;
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
		String conType = StringUtil.NVL(getConType());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(conType)) ? "" : "&conType=" + conType;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
		
}
