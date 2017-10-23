package core.wms.map.domain;

/**
 */
public class ContentsMapping {

    private Long conNum;

    private String refType;

    private String refSubType;

    private Long refNum;

    private Integer evtNum;


    public Long getConNum() {
        return conNum;
    }

    public void setConNum(Long conNum) {
        this.conNum = conNum;
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

    public Long getRefNum() {
        return refNum;
    }

    public void setRefNum(Long refNum) {
        this.refNum = refNum;
    }

    public Integer getEvtNum() {
        return evtNum;
    }

    public void setEvtNum(Integer evtNum) {
        this.evtNum = evtNum;
    }
}
