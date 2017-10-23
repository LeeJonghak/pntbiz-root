package core.wms.service.domain;

/**
 * 도메인 - 세미나 관리
 */
public class AttendanceSeminarMgr {

    private String UUID;
    private Integer majorVer;
    private Integer minorVer;
    private String macAddr;
    private String subject;
    private Long modDate;
    private Long regDate;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Integer getMajorVer() {
        return majorVer;
    }

    public void setMajorVer(Integer majorVer) {
        this.majorVer = majorVer;
    }

    public Integer getMinorVer() {
        return minorVer;
    }

    public void setMinorVer(Integer minorVer) {
        this.minorVer = minorVer;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getModDate() {
        return modDate;
    }

    public void setModDate(Long modDate) {
        this.modDate = modDate;
    }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }
}
