package core.wms.service.domain;

public class EventResVO {
	
	private String sendType;
	
	private int eventId;
	
	private String eventNm;
	
	private String eventDesc;
	
	private int useYN;
	
	private int isDeleted;
	
	private String createdAt;
	
	private String createdNo;
	
	private String updatedAt;
	
	private String updatedNo;
	
	private String contsNm;
	
	private String contsId;
	
	private String contsType;
	
	private String contsTypeNm;

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

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public int getUseYN() {
		return useYN;
	}

	public void setUseYN(int useYN) {
		this.useYN = useYN;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedNo() {
		return createdNo;
	}

	public void setCreatedNo(String createdNo) {
		this.createdNo = createdNo;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedNo() {
		return updatedNo;
	}

	public void setUpdatedNo(String updatedNo) {
		this.updatedNo = updatedNo;
	}

	public String getContsNm() {
		return contsNm;
	}

	public void setContsNm(String contsNm) {
		this.contsNm = contsNm;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
	}

	public String getContsTypeNm() {
		return contsTypeNm;
	}

	public void setContsTypeNm(String contsTypeNm) {
		this.contsTypeNm = contsTypeNm;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventResVO [sendType=").append(sendType)
				.append(", eventId=").append(eventId).append(", eventNm=")
				.append(eventNm).append(", eventDesc=").append(eventDesc)
				.append(", useYN=").append(useYN).append(", isDeleted=")
				.append(isDeleted).append(", createdAt=").append(createdAt)
				.append(", createdNo=").append(createdNo)
				.append(", updatedAt=").append(updatedAt)
				.append(", updatedNo=").append(updatedNo).append(", contsNm=")
				.append(contsNm).append(", contsId=").append(contsId)
				.append(", contsType=").append(contsType)
				.append(", contsTypeNm=").append(contsTypeNm).append("]");
		return builder.toString();
	}
	
}
