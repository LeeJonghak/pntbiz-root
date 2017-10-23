package core.wms.event.domain;

/**
 * 이벤트 유형
 * @author nohsoo 2015-04-16
 */
public class EventType {

    /**
     * 업체번호
     */
    private int comNum;

    /**
     * 이벤트 타입코드
     */
    private String evtTypeCode;

    /**
     * 이벤트 타입명
     */
    private String evtTypeName;

    public int getComNum() {
        return comNum;
    }

    public void setComNum(int comNum) {
        this.comNum = comNum;
    }

    public String getEvtTypeCode() {
        return evtTypeCode;
    }

    public void setEvtTypeCode(String evtTypeCode) {
        this.evtTypeCode = evtTypeCode;
    }

    public String getEvtTypeName() {
        return evtTypeName;
    }

    public void setEvtTypeName(String evtTypeName) {
        this.evtTypeName = evtTypeName;
    }
}
