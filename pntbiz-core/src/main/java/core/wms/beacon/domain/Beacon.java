package core.wms.beacon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import framework.util.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class Beacon {

    private Long beaconNum;
    private Integer comNum;
    private String UUID;
    private Integer majorVer;
    private Integer minorVer;
    private String macAddr;
    private Float txPower;
    private Integer battery;
    private String beaconType;
    private String beaconTypeLangCode;
    private String beaconName;
    private String beaconDesc;
    private String floor;
    private Double lat;
    private Double lng;
    private String state;
    private String stateReason;
    private String lastDate;
    private String modDate;
    private String regDate;
    private String imgSrc;
    private String imgUrl;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private Integer beaconGroupNum;
    private String beaconGroupName;
    private String externalId;
    private String externalAttributeRaw = "[]";
    private String barcode;

    public Long getBeaconNum() {
        return beaconNum;
    }

    public void setBeaconNum(Long beaconNum) {
        this.beaconNum = beaconNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
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

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public Float getTxPower() {
        return txPower;
    }

    public void setTxPower(Float txPower) {
        this.txPower = txPower;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }
    
    public String getBeaconType() {
		return beaconType;
	}

	public void setBeaconType(String beaconType) {
		this.beaconType = beaconType;
	}
	
	public String getBeaconTypeLangCode() {
		return beaconTypeLangCode;
	}

	public void setBeaconTypeLangCode(String beaconTypeLangCode) {
		this.beaconTypeLangCode = beaconTypeLangCode;
	}

	public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public String getBeaconDesc() {
        return beaconDesc;
    }

    public void setBeaconDesc(String beaconDesc) {
        this.beaconDesc = beaconDesc;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateReason() {
        return stateReason;
    }

    public void setStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @JsonIgnore
    public String getExternalAttributeRaw() {
        return externalAttributeRaw;
    }

    public void setExternalAttributeRaw(String externalAttributeRaw) {
        this.externalAttributeRaw = externalAttributeRaw;
    }

    public void setExternalAttribute(List<Map<String, Object>> attribute) {
        this.externalAttributeRaw = JsonUtils.writeValue(attribute);
    }

    public void addExternalAttribute(String key, Object value, String displayName) {
        List<Map<String, Object>> listMap = getExternalAttribute();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("value", value);
        map.put("displayName", displayName);
        listMap.add(map);
        this.setExternalAttribute(listMap);
    }

    public void addExternalAttribute(String key, Object value) {
        addExternalAttribute(key, value, "");
    }

    public List<Map<String, Object>> getExternalAttribute() {
        if(externalAttributeRaw==null || externalAttributeRaw=="") {
            externalAttributeRaw = "[]";
        }
        List<Map<String, Object>> list = JsonUtils.readValue(externalAttributeRaw, ArrayList.class);
        return list;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public Integer getBeaconGroupNum() {
		return beaconGroupNum;
	}

	public void setBeaconGroupNum(Integer beaconGroupNum) {
		this.beaconGroupNum = beaconGroupNum;
	}

	public String getBeaconGroupName() {
		return beaconGroupName;
	}
	
	public void setBeaconGroupName(String beaconGroupName) {
		this.beaconGroupName = beaconGroupName;
	}
	
}
