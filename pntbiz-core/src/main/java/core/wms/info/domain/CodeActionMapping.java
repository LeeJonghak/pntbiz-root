package core.wms.info.domain;

/**
 */
public class CodeActionMapping {

    private Integer codeNum;
    private String refType;
    private String refSubType;
    private Long refNum;

    public Integer getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(Integer codeNum) {
        this.codeNum = codeNum;
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
}
