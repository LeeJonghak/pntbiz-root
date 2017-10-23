package core.common.presence.bo.domain;

import core.common.enums.BooleanType;
import core.common.enums.ZoneInOutStateType;
import core.common.enums.ZoneType;

/**
 * Created by ucjung on 2017-06-15.
 */
public class ZoneInOutState {
    private String zoneId;
    private String name;

    private ZoneInOutStateType beforeState;
    private ZoneInOutStateType state;
    private ZoneType zoneType;

    private Integer enterTimeInterval = 0;
    private Integer stayTimeInterval = 0;
    private Integer leaveTimeInterval = 0;

    private Integer inTime = 0;
    private Integer stayTime = 0;
    private Integer outTime = 0;

    private Integer fcGroupNum;     // ZoneType이 Geofence인 경우 펜스의 그룹키
    private String fcGroupName;     // ZoneType이 Geofence인 경우 펜스의 그룹명

    private Integer logNum = 0;
    private BooleanType permitted = BooleanType.TRUE;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZoneInOutStateType getState() {
        return state;
    }

    public ZoneInOutStateType getBeforeState() {
        return beforeState;
    }

    public void setBeforeState(ZoneInOutStateType beforeState) {
        this.beforeState = beforeState;
    }

    public void setState(ZoneInOutStateType state) {
        this.state = state;
    }

    public ZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public Integer getEnterTimeInterval() {
        return enterTimeInterval;
    }

    public void setEnterTimeInterval(Integer enterTimeInterval) {
        this.enterTimeInterval = enterTimeInterval;
    }

    public Integer getStayTimeInterval() {
        return stayTimeInterval;
    }

    public void setStayTimeInterval(Integer stayTimeInterval) {
        this.stayTimeInterval = stayTimeInterval;
    }

    public Integer getLeaveTimeInterval() {
        return leaveTimeInterval;
    }

    public void setLeaveTimeInterval(Integer leaveTimeInterval) {
        this.leaveTimeInterval = leaveTimeInterval;
    }

    public Integer getInTime() {
        return inTime;
    }

    public void setInTime(Integer inTime) {
        this.inTime = inTime;
    }

    public Integer getStayTime() {
        return stayTime;
    }

    public void setStayTime(Integer stayTime) {
        this.stayTime = stayTime;
    }

    public Integer getOutTime() {
        return outTime;
    }

    public void setOutTime(Integer outTime) {
        this.outTime = outTime;
    }

    public Integer getFcGroupNum() {
        return fcGroupNum;
    }

    public void setFcGroupNum(Integer fcGroupNum) {
        this.fcGroupNum = fcGroupNum;
    }

    public String getFcGroupName() {
        return fcGroupName;
    }

    public void setFcGroupName(String fcGroupName) {
        this.fcGroupName = fcGroupName;
    }

    public Integer getLogNum() {
        return logNum;
    }

    public void setLogNum(Integer logNum) {
        this.logNum = logNum;
    }

    public void changeState(ZoneInOutStateType state) {
        if (this.state != state) {
            this.beforeState = this.state;
        }
        this.state = state;
    }

    public BooleanType getPermitted() {
        return permitted;
    }

    public void setPermitted(BooleanType permitted) {
        this.permitted = permitted;
    }
}
