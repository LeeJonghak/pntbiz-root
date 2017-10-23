package core.wms.event.domain;

/**
 * 이벤트 값
 * @author nohsoo 2015-04-16
 */
public class EventValues {

    /**
     * 고유번호
     */
    private int evtValueNum;

    /**
     * 이벤트 번호(참조키)
     */
    private int evtNum;

    /**
     * 이벤트 컬럼번호(참조키)
     */
    private int evtColNum;

    /**
     * 값(범위 시작)
     */
    private String beginValue;

    /**
     * 값(범위 종료)
     */
    private String endValue;

    public int getEvtValueNum() {
        return evtValueNum;
    }

    public void setEvtValueNum(int evtValueNum) {
        this.evtValueNum = evtValueNum;
    }

    public int getEvtNum() {
        return evtNum;
    }

    public void setEvtNum(int evtNum) {
        this.evtNum = evtNum;
    }

    public int getEvtColNum() {
        return evtColNum;
    }

    public void setEvtColNum(int evtColNum) {
        this.evtColNum = evtColNum;
    }

    public String getBeginValue() {
        return beginValue;
    }

    public void setBeginValue(String beginValue) {
        this.beginValue = beginValue;
    }

    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }
}
