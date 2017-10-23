package core.wms.tracking.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class PresenceBeaconLogSearchParam extends PagingParam {
	private String UUID;
	private String opt;
	private String keyword;	
	
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
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
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());
		String queryString = "";
		queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
		return queryString;
	}
		
}
