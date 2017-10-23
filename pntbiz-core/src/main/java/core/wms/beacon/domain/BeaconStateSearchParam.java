package core.wms.beacon.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class BeaconStateSearchParam extends PagingParam {

	private Integer comNum;
	private String field;
	private String sort;
	private String opt;
	private String keyword;
	private String state;

	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
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
		this.keyword = keyword.trim();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getQueryString() {
		String field = StringUtil.NVL(getField());
		String sort = StringUtil.NVL(getSort());
		String opt = StringUtil.NVL(getOpt());
		String keyword = StringUtil.NVL(getKeyword());

		StringBuffer sb = new StringBuffer();
		sb.append(("".equals(field)) ? "" : "&field=" + field);
		sb.append(("".equals(sort)) ? "" : "&sort=" + sort);
		if(!"".equals(opt) && !"".equals(keyword)){
			sb.append("&opt=");
			sb.append(opt);
			sb.append("&keyword=");
			sb.append(keyword);
		}
		return sb.toString();
	}

}
