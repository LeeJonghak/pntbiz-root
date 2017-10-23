package core.api.log.domain;

/**
 * Created by jhlee on 2017-04-16.
 */
public class ISMSosData {

    private String UUID;
    private Integer majorVer;
    private Integer minorVer;
    private Double lat;
    private Double lng;
    private String floor;
    private Integer battery;
    private String sosStatus;
    private String columnCode;
    private String areaCode;
    private String sosID;
    private String creationDate;

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

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getSosStatus() {
        return sosStatus;
    }

    public void setSosStatus(String sosStatus) {
        this.sosStatus = sosStatus;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getSosID() {
        return sosID;
    }

    public void setSosID(String sosID) {
        this.sosID = sosID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
