package core.wms.service.domain;

import framework.web.util.PagingParam;

public class BaseDomain extends PagingParam{

	private String retCode;
	
	private String retMsg;
	
	private String srchType;
	
	private String srchWords;
	
	private int totalCount = 0;
	
	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getSrchType() {
		return srchType;
	}

	public void setSrchType(String srchType) {
		this.srchType = srchType;
	}

	public String getSrchWords() {
		return srchWords;
	}

	public void setSrchWords(String srchWords) {
		this.srchWords = srchWords;
	}
	
}
