package core.api.presence.domain;

/**
 * Created by jhlee on 2017-04-15.
 */
public class PresenceBeaconLocation {

    private String SUUID;
    private String UUID;
    private Integer majorVer;
    private Integer minorVer;
    private String floor;
    private Double lat;
    private Double lng;
    private Integer nodeIdx;
    private String nodeName;
    private Integer battery;
    private Integer sec;

    public String getSUUID() {
        return SUUID;
    }

    public void setSUUID(String SUUID) {
        this.SUUID = SUUID;
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
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

    public Integer getNodeIdx() {
        return nodeIdx;
    }

    public void setNodeIdx(Integer nodeIdx) {
        this.nodeIdx = nodeIdx;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Integer getSec() {
        return sec;
    }

    public void setSec(Integer sec) {
        this.sec = sec;
    }
}
