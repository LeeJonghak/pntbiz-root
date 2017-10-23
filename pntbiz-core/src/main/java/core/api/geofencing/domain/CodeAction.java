package core.api.geofencing.domain;

/**
 */
public class CodeAction {

    private Integer comNum;
    private Integer codeNum;
    private String codeType;
    private String code;
    private String codeName;
    private Integer modDate;
    private Integer regDate;

    private Integer refNum;
    private String refType;
    private String refSubType;

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Integer getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(Integer codeNum) {
        this.codeNum = codeNum;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
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
}
