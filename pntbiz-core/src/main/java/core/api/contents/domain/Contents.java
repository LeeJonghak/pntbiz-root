package core.api.contents.domain;

public class Contents {
	
	private String UUID;
	
	private Integer comNum;
	private Integer conNum;
	private String conType;
	private String conName;	
	private String userID;
	private Integer acNum;	
	private String expFlag;	
	private Integer rssi;
	private String conDesc;
	private Long sDate;
	private Long eDate;
	private Integer modDate;
	private Integer regDate;
	
	private String imgSrc1;
	private String imgSrc2;
	private String imgSrc3;
	private String imgSrc4;
	private String imgSrc5;
	private String text1;
	private String text2;
	private String text3;
	private String soundSrc1;
	private String soundSrc2;
	private String soundSrc3;
	private String url1;
	private String url2;
	private String url3;
	
	private String imgURL1;
	private String imgURL2;
	private String imgURL3;
	private String imgURL4;
	private String imgURL5;
	private String soundURL1;
	private String soundURL2;
	private String soundURL3;
	
	private String acName;

    private Integer refNum;
    private String refType;
    private String refSubType;

    /**
     * 이벤트 번호
     */
    private Integer evtNum;


	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public Integer getConNum() {
		return conNum;
	}
	public void setConNum(Integer conNum) {
		this.conNum = conNum;
	}
	public String getConType() {
		return conType;
	}
	public void setConType(String conType) {
		this.conType = conType;
	}
	public String getConName() {
		return conName;
	}
	public void setConName(String conName) {
		this.conName = conName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public Integer getAcNum() {
		return acNum;
	}
	public void setAcNum(Integer acNum) {
		this.acNum = acNum;
	}
	public String getExpFlag() {
		return expFlag;
	}
	public void setExpFlag(String expFlag) {
		this.expFlag = expFlag;
	}		
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	public String getConDesc() {
		return conDesc;
	}
	public void setConDesc(String conDesc) {
		this.conDesc = conDesc;
	}
	public Long getsDate() {
		return sDate;
	}
	public void setsDate(Long sDate) {
		this.sDate = sDate;
	}
	public Long geteDate() {
		return eDate;
	}
	public void seteDate(Long eDate) {
		this.eDate = eDate;
	}
	public Integer getModDate() {
		return modDate;
	}
	public void setModDate(Integer modDate) {
		this.modDate = modDate;
	}
	public Integer getRegDate() {
		return regDate;
	}
	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
	
	public String getImgSrc1() {
		return imgSrc1;
	}
	public void setImgSrc1(String imgSrc1) {
		this.imgSrc1 = imgSrc1;
	}
	public String getImgSrc2() {
		return imgSrc2;
	}
	public void setImgSrc2(String imgSrc2) {
		this.imgSrc2 = imgSrc2;
	}
	public String getImgSrc3() {
		return imgSrc3;
	}
	public void setImgSrc3(String imgSrc3) {
		this.imgSrc3 = imgSrc3;
	}
	public String getImgSrc4() {
		return imgSrc4;
	}
	public void setImgSrc4(String imgSrc4) {
		this.imgSrc4 = imgSrc4;
	}
	public String getImgSrc5() {
		return imgSrc5;
	}
	public void setImgSrc5(String imgSrc5) {
		this.imgSrc5 = imgSrc5;
	}
	public String getText1() {
		return text1;
	}
	public void setText1(String text1) {
		this.text1 = text1;
	}
	public String getText2() {
		return text2;
	}
	public void setText2(String text2) {
		this.text2 = text2;
	}
	public String getText3() {
		return text3;
	}
	public void setText3(String text3) {
		this.text3 = text3;
	}	
	public String getSoundSrc1() {
		return soundSrc1;
	}
	public void setSoundSrc1(String soundSrc1) {
		this.soundSrc1 = soundSrc1;
	}
	public String getSoundSrc2() {
		return soundSrc2;
	}
	public void setSoundSrc2(String soundSrc2) {
		this.soundSrc2 = soundSrc2;
	}
	public String getSoundSrc3() {
		return soundSrc3;
	}
	public void setSoundSrc3(String soundSrc3) {
		this.soundSrc3 = soundSrc3;
	}
	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	public String getUrl3() {
		return url3;
	}
	public void setUrl3(String url3) {
		this.url3 = url3;
	}
	
	public String getImgURL1() {
		return imgURL1;
	}
	public void setImgURL1(String imgURL1) {
		this.imgURL1 = imgURL1;
	}
	public String getImgURL2() {
		return imgURL2;
	}
	public void setImgURL2(String imgURL2) {
		this.imgURL2 = imgURL2;
	}
	public String getImgURL3() {
		return imgURL3;
	}
	public void setImgURL3(String imgURL3) {
		this.imgURL3 = imgURL3;
	}
	public String getImgURL4() {
		return imgURL4;
	}
	public void setImgURL4(String imgURL4) {
		this.imgURL4 = imgURL4;
	}
	public String getImgURL5() {
		return imgURL5;
	}
	public void setImgURL5(String imgURL5) {
		this.imgURL5 = imgURL5;
	}
	public String getSoundURL1() {
		return soundURL1;
	}
	public void setSoundURL1(String soundURL1) {
		this.soundURL1 = soundURL1;
	}
	public String getSoundURL2() {
		return soundURL2;
	}
	public void setSoundURL2(String soundURL2) {
		this.soundURL2 = soundURL2;
	}
	public String getSoundURL3() {
		return soundURL3;
	}
	public void setSoundURL3(String soundURL3) {
		this.soundURL3 = soundURL3;
	}
	public String getAcName() {
		return acName;
	}
	
	public void setAcName(String acName) {
		this.acName = acName;
	}

    public Integer getRefNum() {
        return refNum;
    }

    public void setRefNum(Integer refNum) {
        this.refNum = refNum;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getRefSubType() {
        return refSubType;
    }

    public void setRefSubType(String refSubType) {
        this.refSubType = refSubType;
    }

    public Integer getEvtNum() {
        return evtNum;
    }

    public void setEvtNum(Integer evtNum) {
        this.evtNum = evtNum;
    }
}


   