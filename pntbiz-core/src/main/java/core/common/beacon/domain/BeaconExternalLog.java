package core.common.beacon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.common.enums.AssignUnassignType;
import core.common.enums.BooleanType;
import framework.util.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nsyun on 17. 8. 14..
 */
public class BeaconExternalLog {

	private Long logNum;

	private AssignUnassignType type;

	private Long beaconNum;

	private String externalId;

	private String barcode;

	private String externalAttributeRaw;

	private BooleanType restrictedZonePermitted;

	private Integer regDate;

	public Long getLogNum() {
		return logNum;
	}

	public void setLogNum(Long logNum) {
		this.logNum = logNum;
	}

	public AssignUnassignType getType() {
		return type;
	}

	public void setType(AssignUnassignType type) {
		this.type = type;
	}

	public Long getBeaconNum() {
		return beaconNum;
	}

	public void setBeaconNum(Long beaconNum) {
		this.beaconNum = beaconNum;
	}

	public void setBeaconNum(Integer beaconNum) {
		this.beaconNum = beaconNum.longValue();
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public BooleanType getRestrictedZonePermitted() {
		return restrictedZonePermitted;
	}

	public void setRestrictedZonePermitted(BooleanType restrictedZonePermitted) {
		this.restrictedZonePermitted = restrictedZonePermitted;
	}

	public Integer getRegDate() {
		return regDate;
	}

	public void setRegDate(Integer regDate) {
		this.regDate = regDate;
	}
}
