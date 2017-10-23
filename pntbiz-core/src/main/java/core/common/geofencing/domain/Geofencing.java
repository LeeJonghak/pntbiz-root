package core.common.geofencing.domain;

import framework.web.Validation;
import org.hibernate.validator.constraints.NotEmpty;

/**
 */
public class Geofencing {

    private Integer comNum;
    @NotEmpty(groups = { Validation.Modify.class})
    private Long fcNum;

    /**
     * 펜스타입(S:시스템정의/G:일반)
     */
    private String fcType = "G";
    @NotEmpty
    private String fcShape;
    @NotEmpty
    private String fcName;
    private String floor;
    private Integer evtEnter = 0;
    private Integer evtLeave = 0;
    private Integer evtStay = 0;

    private Integer numEnter;
    private Integer numLeave;
    private Integer numStay;

    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;

    private String isNodeEnable = "N";

    private String userID;
    private Integer modDate;
    private Integer regDate;

    private Integer fcGroupNum;
    private String fcGroupName;

    /**
     * BPZONE(ZONE타입), length:4
     */
    private String zoneType;

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Long getFcNum() {
        return fcNum;
    }

    public void setFcNum(Long fcNum) {
        this.fcNum = fcNum;
    }

    public String getFcType() {
        return fcType;
    }

    public void setFcType(String fcType) {
        this.fcType = fcType;
    }

    public String getFcShape() {
        return fcShape;
    }

    public void setFcShape(String fcShape) {
        this.fcShape = fcShape;
    }

    public String getFcName() {
        return fcName;
    }

    public void setFcName(String fcName) {
        this.fcName = fcName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getEvtEnter() {
        return evtEnter;
    }

    public void setEvtEnter(Integer evtEnter) {
        this.evtEnter = evtEnter;
    }

    public Integer getEvtLeave() {
        return evtLeave;
    }

    public void setEvtLeave(Integer evtLeave) {
        this.evtLeave = evtLeave;
    }

    public Integer getEvtStay() {
        return evtStay;
    }

    public void setEvtStay(Integer evtStay) {
        this.evtStay = evtStay;
    }

    public Integer getNumEnter() {
        return numEnter;
    }

    public void setNumEnter(Integer numEnter) {
        this.numEnter = numEnter;
    }

    public Integer getNumLeave() {
        return numLeave;
    }

    public void setNumLeave(Integer numLeave) {
        this.numLeave = numLeave;
    }

    public Integer getNumStay() {
        return numStay;
    }

    public void setNumStay(Integer numStay) {
        this.numStay = numStay;
    }

    public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

    public String getIsNodeEnable() {
        return isNodeEnable;
    }

    public void setIsNodeEnable(String isNodeEnable) {
        this.isNodeEnable = isNodeEnable;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getModDate() {
        return modDate;
    }

    public void setModDate(Integer modDate) {
        this.modDate = modDate;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
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

}
