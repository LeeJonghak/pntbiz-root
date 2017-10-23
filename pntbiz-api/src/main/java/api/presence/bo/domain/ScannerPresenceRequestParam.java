package api.presence.bo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.api.presence.domain.GeoLocation;
import core.api.scanner.domain.ScannerPostion;

import java.util.List;

/**
 * Scanner Model Presence Request Parameter Value Object
 *
 * 스캐너모델에서 수신되는 측위 데이타를 위한 Value Object
 *
 * Created by ucjung on 2017-06-13.
 */
public class ScannerPresenceRequestParam {
    private String id;                          // 비콘 고유 ID : [Beancon UUID]_[Major Version]_[Minor Version]
    private List<GeoLocation> pos;              // 측위 위치정보 목록

    private String UUID;                        // Beacon UUID (Request의 id.split("_")[0] )
    private String sid;
    private String floor;                       // 층정보
    private Integer nodeIdx;                    // 노드 번호
    private String nodeName;                    // 노드 명
    private Integer battLevel;                  // 베터리 정보
    private String mac;                         // beacon mac address

    private Integer sos;                        // SOS 여부
    private Integer heartbeat;                  // 심박수
    private List<ScannerPostion> scannerPos;    // 스캐너 위치 정보
    private String[] beaconId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.toUpperCase();
        beaconId = this.id.split("\\_");
    }

    public List<GeoLocation> getPos() {
        return pos;
    }

    public void setPos(List<GeoLocation> pos) {
        this.pos = pos;
    }

    @JsonProperty("UUID")
    public String getSUUID() {
        return UUID;
    }

    @JsonProperty("UUID")
    public void setSUUID(String SUUID) {
        this.UUID = SUUID;
    }

    @JsonIgnore
    public String getUUID() {
        if (beaconId.length > 1) {
            return beaconId[0];
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getMajorVer() {
        if (beaconId.length > 1) {
            return Integer.parseInt(beaconId[1]);
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Integer getMinorVer() {
        if (beaconId.length > 2) {
            return Integer.parseInt(beaconId[2]);
        } else {
            return null;
        }
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

    public Integer getBattLevel() {
        return battLevel;
    }

    public void setBattLevel(Integer battLevel) {
        this.battLevel = battLevel;
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

    public Integer getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Integer heartbeat) {
        this.heartbeat = heartbeat;
    }

    public List<ScannerPostion> getScannerPos() {
        return scannerPos;
    }

    public void setScannerPos(List<ScannerPostion> scannerPos) {
        this.scannerPos = scannerPos;
    }

    public Double getLat() {
        return pos.get(pos.size() - 1).getLat();
    }

    public Double getLng() {
        return pos.get(pos.size() - 1).getLng();
    }

    public Integer getBattery() {
        return battLevel;
    }
}
