package core.wms.event.domain;

/**
 * 이벤트
 * @author nohsoo 2015-04-16
 */
public class Event {

    /**
     * 이벤트 고유번호
     */
    private int evtNum;

    /**
     * 이벤트 타입코드
     */
    private String evtTypeCode;

    /**
     * 업체번호
     */
    private int comNum;

    /**
     * 이벤트명
     */
    private String evtName;

    /**
     * 이벤트 설명
     */
    private String evtDesc;

    /**
     * 등록시간
     */
    private int regDate;

    public int getEvtNum() {
        return evtNum;
    }

    public void setEvtNum(int evtNum) {
        this.evtNum = evtNum;
    }

    public String getEvtTypeCode() {
        return evtTypeCode;
    }

    public void setEvtTypeCode(String evtTypeCode) {
        this.evtTypeCode = evtTypeCode;
    }

    public int getComNum() {
        return comNum;
    }

    public void setComNum(int comNum) {
        this.comNum = comNum;
    }

    public String getEvtName() {
        return evtName;
    }

    public void setEvtName(String evtName) {
        this.evtName = evtName;
    }

    public String getEvtDesc() {
        return evtDesc;
    }

    public void setEvtDesc(String evtDesc) {
        this.evtDesc = evtDesc;
    }

    public int getRegDate() {
        return regDate;
    }

    public void setRegDate(int regDate) {
        this.regDate = regDate;
    }
}
