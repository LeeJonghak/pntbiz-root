package core.admin.authpackage.dao.domain;

import framework.web.util.PagingParam;

public class AuthPackageSearchParam extends PagingParam {
	private String opt;
	private String keyword;		
	
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
		String queryString = "";
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
		
}
