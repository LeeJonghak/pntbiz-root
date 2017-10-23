package core.admin.log.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class AdminLogSearchParam extends PagingParam {
	private String crudType;
	private String opt;
	private String keyword;	
	
	public String getCrudType() {
		return crudType;
	}
	public void setCrudType(String crudType) {
		this.crudType = crudType;
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
		String crudType = StringUtil.NVL(getCrudType());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(crudType)) ? "" : "&crudType" + "=" + crudType;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&"+ opt + "=" + keyword;
		return queryString;
	}
}