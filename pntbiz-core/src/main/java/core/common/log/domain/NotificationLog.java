package core.common.log.domain;

import core.common.enums.InterfaceCommandType;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import framework.util.JsonUtils;

/**
 * Created by ucjung on 2017-09-26.
 */
public class NotificationLog {
    private Integer logNum;
    private Integer comNum;
    private Integer beaconNum;
    private InterfaceCommandType interfaceCommandType;
    private ScannerPresenceRedis beaconInfo;
    private ZoneInOutState eventZoneInOutState;

    public Integer getLogNum() {
        return logNum;
    }

    public void setLogNum(Integer logNum) {
        this.logNum = logNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Integer getBeaconNum() {
        return beaconNum;
    }

    public void setBeaconNum(Integer beaconNum) {
        this.beaconNum = beaconNum;
    }

    public InterfaceCommandType getInterfaceCommandType() {
        return interfaceCommandType;
    }

    public void setInterfaceCommandType(InterfaceCommandType interfaceCommandType) {
        this.interfaceCommandType = interfaceCommandType;
    }

    public String getBeaconInfo() {
        return JsonUtils.writeValue(beaconInfo);
    }

    public ScannerPresenceRedis getBeaconObjectInfo() {
        return beaconInfo;
    }

    public void setBeaconInfo(String beaconInfo) {
        this.beaconInfo = JsonUtils.readValue(beaconInfo, ScannerPresenceRedis.class);
    }

    public void setBeaconInfo(ScannerPresenceRedis beaconInfo) {
        this.beaconInfo = beaconInfo;
    }

    public String getEventZoneInOutState() {
        return JsonUtils.writeValue(eventZoneInOutState);
    }
    public ZoneInOutState getEventZoneInOutStateObject() {
        return eventZoneInOutState;
    }

    public void setEventZoneInOutState(String eventZoneInOutState) {
        this.eventZoneInOutState = JsonUtils.readValue(eventZoneInOutState, ZoneInOutState.class);
    }

    public void setEventZoneInOutState(ZoneInOutState eventZoneInOutState) {
        this.eventZoneInOutState = eventZoneInOutState;
    }
}
