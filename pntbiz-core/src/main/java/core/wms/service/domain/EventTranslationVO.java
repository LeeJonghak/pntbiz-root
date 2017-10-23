package core.wms.service.domain;

public class EventTranslationVO extends BaseDomain{
	
	private int pnsStageSeqNo;
	
	private String sendType;
	
	private int eventId;
	
	private int contsId;
	
	private int useYn;
	
	private int isDeleted;
	
	private String genderUseYn;
	
	private String genderCondVal;
	
	private String ageUseYn;
	
	private String ageStartVal;
	
	private String ageEndVal;
	
	private String birthUseYn;
	
	private String birthStart;
	
	private String birthEnd;
	
	private String annivUseYn;
	
	private String annivStart;
	
	private String annivEnd;
	
	private String visitCntUseYn;
	
	private int visitCnt = 0;
	
	private String zoneUseYn;
	
	private String zoneVal;
	
	private String blockUseYn;
	
	private String blockVal;
	
	private String seatUseYn;
	
	private String seatVal;
	
	private String benefit1UseYn;
	
	private String benefit1Msg;
	
	private String benefit1SeatBlock;
	
	private String benefit1SeatCol;
	
	private String benefit1SeatNum;
	
	private String benefit2UseYn;
	
	private String benefit2Msg;
	
	private String benefit2SeatBlock;
	
	private String benefit2SeatCol;
	
	private String benefit2SeatNum;
	
	private String orderUseYn;
	
	private int orderUseCnt = 0;
	
	private int createdNo;
	
	private String createdAt;
	
	private int updatedNo;
	
	private String updatedAt;
	
	private String nakchomComm;

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getContsId() {
		return contsId;
	}

	public void setContsId(int contsId) {
		this.contsId = contsId;
	}

	public int getUseYn() {
		return useYn;
	}

	public void setUseYn(int useYn) {
		this.useYn = useYn;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getGenderUseYn() {
		return genderUseYn;
	}

	public void setGenderUseYn(String genderUseYn) {
		this.genderUseYn = genderUseYn;
	}

	public String getGenderCondVal() {
		return genderCondVal;
	}

	public void setGenderCondVal(String genderCondVal) {
		this.genderCondVal = genderCondVal;
	}

	public String getAgeUseYn() {
		return ageUseYn;
	}

	public void setAgeUseYn(String ageUseYn) {
		this.ageUseYn = ageUseYn;
	}

	public String getAgeStartVal() {
		return ageStartVal;
	}

	public void setAgeStartVal(String ageStartVal) {
		this.ageStartVal = ageStartVal;
	}

	public String getAgeEndVal() {
		return ageEndVal;
	}

	public void setAgeEndVal(String ageEndVal) {
		this.ageEndVal = ageEndVal;
	}

	public String getBirthUseYn() {
		return birthUseYn;
	}

	public void setBirthUseYn(String birthUseYn) {
		this.birthUseYn = birthUseYn;
	}

	public String getBirthStart() {
		return birthStart;
	}

	public void setBirthStart(String birthStart) {
		this.birthStart = birthStart;
	}

	public String getBirthEnd() {
		return birthEnd;
	}

	public void setBirthEnd(String birthEnd) {
		this.birthEnd = birthEnd;
	}

	public String getAnnivUseYn() {
		return annivUseYn;
	}

	public void setAnnivUseYn(String annivUseYn) {
		this.annivUseYn = annivUseYn;
	}

	public String getAnnivStart() {
		return annivStart;
	}

	public void setAnnivStart(String annivStart) {
		this.annivStart = annivStart;
	}

	public String getAnnivEnd() {
		return annivEnd;
	}

	public void setAnnivEnd(String annivEnd) {
		this.annivEnd = annivEnd;
	}

	public String getVisitCntUseYn() {
		return visitCntUseYn;
	}

	public void setVisitCntUseYn(String visitCntUseYn) {
		this.visitCntUseYn = visitCntUseYn;
	}

	public int getVisitCnt() {
		return visitCnt;
	}

	public void setVisitCnt(int visitCnt) {
		this.visitCnt = visitCnt;
	}

	public String getZoneUseYn() {
		return zoneUseYn;
	}

	public void setZoneUseYn(String zoneUseYn) {
		this.zoneUseYn = zoneUseYn;
	}

	public String getZoneVal() {
		return zoneVal;
	}

	public void setZoneVal(String zoneVal) {
		this.zoneVal = zoneVal;
	}

	public String getBlockUseYn() {
		return blockUseYn;
	}

	public void setBlockUseYn(String blockUseYn) {
		this.blockUseYn = blockUseYn;
	}

	public String getBlockVal() {
		return blockVal;
	}

	public void setBlockVal(String blockVal) {
		this.blockVal = blockVal;
	}

	public String getSeatUseYn() {
		return seatUseYn;
	}

	public void setSeatUseYn(String seatUseYn) {
		this.seatUseYn = seatUseYn;
	}

	public String getSeatVal() {
		return seatVal;
	}

	public void setSeatVal(String seatVal) {
		this.seatVal = seatVal;
	}

	public String getBenefit1UseYn() {
		return benefit1UseYn;
	}

	public void setBenefit1UseYn(String benefit1UseYn) {
		this.benefit1UseYn = benefit1UseYn;
	}

	public String getBenefit1Msg() {
		return benefit1Msg;
	}

	public void setBenefit1Msg(String benefit1Msg) {
		this.benefit1Msg = benefit1Msg;
	}

	public String getBenefit1SeatBlock() {
		return benefit1SeatBlock;
	}

	public void setBenefit1SeatBlock(String benefit1SeatBlock) {
		this.benefit1SeatBlock = benefit1SeatBlock;
	}

	public String getBenefit1SeatCol() {
		return benefit1SeatCol;
	}

	public void setBenefit1SeatCol(String benefit1SeatCol) {
		this.benefit1SeatCol = benefit1SeatCol;
	}

	public String getBenefit2UseYn() {
		return benefit2UseYn;
	}

	public void setBenefit2UseYn(String benefit2UseYn) {
		this.benefit2UseYn = benefit2UseYn;
	}

	public String getBenefit2Msg() {
		return benefit2Msg;
	}

	public void setBenefit2Msg(String benefit2Msg) {
		this.benefit2Msg = benefit2Msg;
	}

	public String getBenefit2SeatBlock() {
		return benefit2SeatBlock;
	}

	public void setBenefit2SeatBlock(String benefit2SeatBlock) {
		this.benefit2SeatBlock = benefit2SeatBlock;
	}

	public String getBenefit2SeatCol() {
		return benefit2SeatCol;
	}

	public void setBenefit2SeatCol(String benefit2SeatCol) {
		this.benefit2SeatCol = benefit2SeatCol;
	}

	public String getOrderUseYn() {
		return orderUseYn;
	}

	public void setOrderUseYn(String orderUseYn) {
		this.orderUseYn = orderUseYn;
	}

	public int getOrderUseCnt() {
		return orderUseCnt;
	}

	public void setOrderUseCnt(int orderUseCnt) {
		this.orderUseCnt = orderUseCnt;
	}

	public int getPnsStageSeqNo() {
		return pnsStageSeqNo;
	}

	public void setPnsStageSeqNo(int pnsStageSeqNo) {
		this.pnsStageSeqNo = pnsStageSeqNo;
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

	public String getBenefit1SeatNum() {
		return benefit1SeatNum;
	}

	public void setBenefit1SeatNum(String benefit1SeatNum) {
		this.benefit1SeatNum = benefit1SeatNum;
	}

	public String getBenefit2SeatNum() {
		return benefit2SeatNum;
	}

	public void setBenefit2SeatNum(String benefit2SeatNum) {
		this.benefit2SeatNum = benefit2SeatNum;
	}

	public String getNakchomComm() {
		return nakchomComm;
	}

	public void setNakchomComm(String nakchomComm) {
		this.nakchomComm = nakchomComm;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventTranslationVO [pnsStageSeqNo=")
				.append(pnsStageSeqNo).append(", sendType=").append(sendType)
				.append(", eventId=").append(eventId).append(", contsId=")
				.append(contsId).append(", useYn=").append(useYn)
				.append(", isDeleted=").append(isDeleted)
				.append(", genderUseYn=").append(genderUseYn)
				.append(", genderCondVal=").append(genderCondVal)
				.append(", ageUseYn=").append(ageUseYn)
				.append(", ageStartVal=").append(ageStartVal)
				.append(", ageEndVal=").append(ageEndVal)
				.append(", birthUseYn=").append(birthUseYn)
				.append(", birthStart=").append(birthStart)
				.append(", birthEnd=").append(birthEnd).append(", annivUseYn=")
				.append(annivUseYn).append(", annivStart=").append(annivStart)
				.append(", annivEnd=").append(annivEnd)
				.append(", visitCntUseYn=").append(visitCntUseYn)
				.append(", visitCnt=").append(visitCnt).append(", zoneUseYn=")
				.append(zoneUseYn).append(", zoneVal=").append(zoneVal)
				.append(", blockUseYn=").append(blockUseYn)
				.append(", blockVal=").append(blockVal).append(", seatUseYn=")
				.append(seatUseYn).append(", seatVal=").append(seatVal)
				.append(", benefit1UseYn=").append(benefit1UseYn)
				.append(", benefit1Msg=").append(benefit1Msg)
				.append(", benefit1SeatBlock=").append(benefit1SeatBlock)
				.append(", benefit1SeatCol=").append(benefit1SeatCol)
				.append(", benefit1SeatNum=").append(benefit1SeatNum)
				.append(", benefit2UseYn=").append(benefit2UseYn)
				.append(", benefit2Msg=").append(benefit2Msg)
				.append(", benefit2SeatBlock=").append(benefit2SeatBlock)
				.append(", benefit2SeatCol=").append(benefit2SeatCol)
				.append(", benefit2SeatNum=").append(benefit2SeatNum)
				.append(", orderUseYn=").append(orderUseYn)
				.append(", orderUseCnt=").append(orderUseCnt)
				.append(", createdNo=").append(createdNo)
				.append(", createdAt=").append(createdAt)
				.append(", updatedNo=").append(updatedNo)
				.append(", updatedAt=").append(updatedAt).append("]");
		return builder.toString();
	}
	
}
