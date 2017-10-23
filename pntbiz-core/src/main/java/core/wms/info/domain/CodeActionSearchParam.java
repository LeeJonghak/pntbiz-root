package core.wms.info.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class CodeActionSearchParam extends PagingParam {
	private Integer comNum;
	private String opt;
	private String keyword;	
	
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
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
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(comNum)) ? "" : "&comNum=" + comNum;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
}
