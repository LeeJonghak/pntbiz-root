package core.admin.user.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class UserSearchParam extends PagingParam {
	
	private Integer comNum;
	private String gender;
	private String opt;
	private String keyword;	
	
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
		String gender = StringUtil.NVL(getGender());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(gender)) ? "" : "&gender=" + gender;
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&"+ opt + "=" + keyword;
		return queryString;
	}
		
}
