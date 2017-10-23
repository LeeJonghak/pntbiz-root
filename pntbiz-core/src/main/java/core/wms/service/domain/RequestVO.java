package core.wms.service.domain;

public class RequestVO extends BaseDomain{
	
	private String srchType;
	
	private String srchWords;

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
