package core.api.service.domain;

/**
 * Created by nohsoo on 15. 7. 28.
 */
public class AttendanceSeminar {

    private Long logNum;

    private String UUID;

    private Integer majorVer;

    private Integer minorVer;

    private String macAddr;

    private String phoneNumber;

    private String deviceInfo;

    private String state;

    private String attdDate;

    private String subject;

    private Long regDate;

    public Long getLogNum() {
        return logNum;
    }

    public void setLogNum(Long logNum) {
        this.logNum = logNum;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAttdDate() {
        return attdDate;
    }

    public void setAttdDate(String attdDate) {
        this.attdDate = attdDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }
}
