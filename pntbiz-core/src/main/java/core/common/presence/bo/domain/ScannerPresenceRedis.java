package core.common.presence.bo.domain;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ucjung on 2017-06-15.
 */
public class ScannerPresenceRedis implements Serializable {

    private static final long serialVersionUID = 5864491417497008104L;

    private String SUUID;
    private String UUID;

    private Integer majorVer;
    private Integer minorVer;

    private Double lat;
    private Double lng;

    private String floor;

    private Integer sos;
    private Integer battery;

    private ZoneInOutState floorInOutState;

    private Map<String, ZoneInOutState> geofencesInOutState;

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

    public Integer getSos() {
        return sos;
    }

    public void setSos(Integer sos) {
        this.sos = sos;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public ZoneInOutState getFloorInOutState() {
        return floorInOutState;
    }

    public void setFloorInOutState(ZoneInOutState floorInOutState) {
        this.floorInOutState = floorInOutState;
    }

    public Map<String, ZoneInOutState> getGeofencesInOutState() {
        return geofencesInOutState;
    }

    public void setGeofencesInOutState(Map<String, ZoneInOutState> geofencesInOutState) {
        this.geofencesInOutState = geofencesInOutState;
    }
}
