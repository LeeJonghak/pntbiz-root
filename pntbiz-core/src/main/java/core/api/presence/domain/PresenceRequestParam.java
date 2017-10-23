package core.api.presence.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import framework.web.util.StringUtil;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;


/**
 * 측위 정보 매핑용 객체 모델
 * * JSON Mapper를 통하여 JSON을 바로 맵핑할 수 있다.
 *
 * Created by ucjung on 2017-04-16.
 */
@Component
public class PresenceRequestParam implements Serializable {
    private String mode = "S";
    private String id = "";

    @JsonProperty("SUUID")
    private String SUUID;

    @JsonProperty("UUID")
    private String UUID;
    private Integer majorVer = 0;
    private Integer minorVer = 0;
    private Double lat;
    private Double lng;
    private List<GeoLocation> pos;
    private String sid;
    private String floor = "";
    private Integer battLevel = 0;
    private String heartBeat;
    private String bodytemperature1;
    private String mac;
    private Integer sos;
    @JsonProperty("nodeIdx")
    private Integer nodeIndex = 0;
    private String nodeName = "";
    private List<PresenceScannerPos> scannerPos;

    // Beacon
    private String phoneNumber = "";
    private String deviceInfo = "";
    private Long fcNum = 0L;
    private String fcName = "";
    private Long nodeID = 0L;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        String[] beacon = id.split("\\_");
        if (beacon.length > 1) {
            UUID = beacon[0];
            majorVer = Integer.parseInt(beacon[1]);
            minorVer = Integer.parseInt(beacon[2]);
        }
        this.id = id;
    }

    public String getSUUID() {
        return SUUID;
    }

    public PresenceRequestParam setSUUID(String SUUID) {
        this.SUUID = StringUtil.NVL(SUUID, "");
        return this;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getBattLevel() {
        return battLevel;
    }

    public void setBattLevel(Integer battLevel) {
        this.battLevel = battLevel;
    }

    public String getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(String heartBeat) {
        this.heartBeat = heartBeat;
    }

    public String getBodytemperature1() {
        return bodytemperature1;
    }

    public void setBodytemperature1(String bodytemperature1) {
        this.bodytemperature1 = bodytemperature1;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getSos() {
        return sos;
    }

    public void setSos(Integer sos) {
        this.sos = sos;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<PresenceScannerPos> getScannerPos() {
        return scannerPos;
    }

    public void setScannerPos(List<PresenceScannerPos> scannerPos) {
        this.scannerPos = scannerPos;
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

    public List<GeoLocation> getPos() {
        return pos;
    }

    public void setPos(List<GeoLocation> pos) {
        this.pos = pos;
    }
}
