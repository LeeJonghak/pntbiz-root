package core.api.log.domain;

/**
 * Table Name: TB_LOG_PRESENCE_BEACON
 */
public class PresenceBeaconLog {

    // length: 11
    private Long logNum;

    // length: 36
    private String UUID;

    // length: 5
    private Integer majorVer;

    // length: 5
    private Integer minorVer;

    private Double lat;

    private Double lng;

    // length: 50
    private String floor;

    // length: 50
    private String deviceInfo;

    // length: 20
    private String phoneNumber;

    private Long fcNum;

    private String fcName;

    private Long nodeID;

    // length: 10
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getFcNum() {
        return fcNum;
    }

    public void setFcNum(Long fcNum) {
        this.fcNum = fcNum;
    }

    public String getFcName() {
        return fcName;
    }

    public void setFcName(String fcName) {
        this.fcName = fcName;
    }

    public Long getNodeID() {
        return nodeID;
    }

    public void setNodeID(Long nodeID) {
        this.nodeID = nodeID;
    }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }
}
