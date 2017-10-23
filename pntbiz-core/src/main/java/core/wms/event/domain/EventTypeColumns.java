package core.wms.event.domain;

/**
 * 이벤트 컬럼(속성)
 * @author nohsoo 2015-04-16
 */
public class EventTypeColumns {

    /**
     * 컬럼 고유번호
     */
    private int evtColNum;

    /**
     * 이벤트유형(참조키)
     */
    private String evtTypeCode;

    /**
     * 컬럼 아이디
     */
    private String evtColID;

    /**
     * 컬럼 종류
     */
    private String evtColType;

    /**
     * 컬럼명
     */
    private String evtColName;

    /**
     * 선택가능 항목
     */
    private String evtColItems;

    public int getEvtColNum() {
        return evtColNum;
    }

    public void setEvtColNum(int evtColNum) {
        this.evtColNum = evtColNum;
    }

    public String getEvtTypeCode() {
        return evtTypeCode;
    }

    public void setEvtTypeCode(String evtTypeCode) {
        this.evtTypeCode = evtTypeCode;
    }

    public String getEvtColID() {
        return evtColID;
    }

    public void setEvtColID(String evtColID) {
        this.evtColID = evtColID;
    }

    public String getEvtColType() {
        return evtColType;
    }

    public void setEvtColType(String evtColType) {
        this.evtColType = evtColType;
    }

    public String getEvtColName() {
        return evtColName;
    }

    public void setEvtColName(String evtColName) {
        this.evtColName = evtColName;
    }

    public String getEvtColItems() {
        return evtColItems;
    }

    public void setEvtColItems(String evtColItems) {
        this.evtColItems = evtColItems;
    }
}
