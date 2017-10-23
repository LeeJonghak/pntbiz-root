package core.wms.service.domain;

import framework.web.util.StringUtil;


public class ServiceEventVO extends BaseDomain{
	
	private String sendType;
	
	private int eventId;
	
	private String eventNm;
	
	private String eventMemo;
	
	private int contsId;
	
	private int createdNo;
	
	private String createdAt;
	
	private int updatedNo;
	
	private String updatedAt;
	
	private int pnsStageSeqNo;
	
	private int pnsId;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventNm() {
		return eventNm;
	}

	public void setEventNm(String eventNm) {
		this.eventNm = eventNm;
	}

	public String getEventMemo() {
		return eventMemo;
	}

	public void setEventMemo(String eventMemo) {
		this.eventMemo = eventMemo;
	}

	public int getContsId() {
		return contsId;
	}

	public void setContsId(int contsId) {
		this.contsId = contsId;
	}

	public int getCreatedNo() {
		return createdNo;
	}

	public void setCreatedNo(int createdNo) {
		this.createdNo = createdNo;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getUpdatedNo() {
		return updatedNo;
	}

	public void setUpdatedNo(int updatedNo) {
		this.updatedNo = updatedNo;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public int getPnsStageSeqNo() {
		return pnsStageSeqNo;
	}

	public void setPnsStageSeqNo(int pnsStageSeqNo) {
		this.pnsStageSeqNo = pnsStageSeqNo;
	}

	public int getPnsId() {
		return pnsId;
	}

	public void setPnsId(int pnsId) {
		this.pnsId = pnsId;
	}
	
	public String queryString(){
		return String.format("srchType=%s&srchWords=%s", StringUtil.NVL(getSrchType()), StringUtil.NVL(getSrchWords()));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceEventVO [sendType=").append(sendType)
				.append(", eventId=").append(eventId).append(", eventNm=")
				.append(eventNm).append(", eventMemo=").append(eventMemo)
				.append(", contsId=").append(contsId).append(", createdNo=")
				.append(createdNo).append(", createdAt=").append(createdAt)
				.append(", updatedNo=").append(updatedNo)
				.append(", updatedAt=").append(updatedAt)
				.append(", pnsStageSeqNo=").append(pnsStageSeqNo).append("]");
		return builder.toString();
	}
	
}
