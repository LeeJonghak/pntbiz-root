package core.admin.auth.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class LoginSearchParam extends PagingParam {
	private String opt;
	private String keyword;
	private int comNum;
	
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
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&"+ opt + "=" + keyword;
		return queryString;
	}

	public int getComNum() {
		return comNum;
	}

	public void setComNum(int comNum) {
		this.comNum = comNum;
	}
}
