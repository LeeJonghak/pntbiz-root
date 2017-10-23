package core.common.beacon.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import core.common.enums.BooleanType;
import framework.util.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nsyun on 17. 8. 17..
 * 비콘 외부데이터
 */
public class BeaconExternal {

	/**
	 * 비콘 고유번호
	 */
	private Long beaconNum;

	/**
	 * 업체번호
	 */
	private Integer comNum;

	/**
	 * 비콘 UUID
	 */
	private String UUID;

	/**
	 * 비콘 Major Version
	 */
	private Integer majorVer;

	/**
	 * 비콘 Minor Version
	 */
	private Integer minorVer;

	/**
	 * 비콘 층코드
	 */
	private String floor;

	/**
	 * 외부데이터 연동키
	 */
	private String externalId;

	/**
	 * 바코드
	 */
	private String barcode;

	/**
	 * 외부데이터
	 */
	private String externalAttributeRaw = "[]";

	/**
	 * 인가 구분. "0":비인가 "1":인가
	 */
	private BooleanType restrictedZonePermitted;

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

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
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

	public BooleanType getRestrictedZonePermitted() {
		return restrictedZonePermitted;
	}

	public void setRestrictedZonePermitted(BooleanType restrictedZonePermitted) {
		this.restrictedZonePermitted = restrictedZonePermitted;
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

}
