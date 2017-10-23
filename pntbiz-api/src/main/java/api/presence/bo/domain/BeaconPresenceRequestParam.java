package api.presence.bo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Beacon Model Presence Request Parameter Value Object
 *
 * 비콘모델에서 수신되는 측위 데이타를 위한 Value Object
 *
 * Created by ucjung on 2017-06-13.
 */
public class BeaconPresenceRequestParam {
    private String mode;            // Beacon 기반 구분코드 "B" : 비콘
    private String UUID;            // 사이트 UUID
    private String sid;
    private Double lat;             // 위도
    private Double lng;             // 경도
    private String floor;           // 층정보
    private Long nodeID;         // 노드 번호
    private Long fcNum;          // 지오펜스 번호
    private String fcName;          // 지오펜스 명
    private String phoneNumber;     // 폰넘버 || 폰MAC Address
    private String deviceInfo;      // 폰넘버 || 폰MAC Address

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @JsonProperty("UUID")
    public String getUUID() {
        return UUID;
    }

    @JsonProperty("UUID")
    public void setUUID(String UUID) {
        this.UUID = UUID.toUpperCase();
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

    public Long getNodeID() {
        return nodeID;
    }

    public void setNodeID(Long nodeID) {
        this.nodeID = nodeID;
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
}
