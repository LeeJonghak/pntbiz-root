package core.common.beacon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.common.enums.BooleanType;
import core.common.enums.ZoneType;
import framework.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

/**
 * Created by nsyun on 17. 7. 19..
 */
public class BeaconRestrictedZone {

	private Long beaconNum;

	/**
	 * FLOOR : 층코드, GEOFENCE 지오펜스
	 */
	private ZoneType zoneType;

	/**
	 * 층코드(floor) 지오펜스 또는 식별번호(fcNum)
	 */
	private String zoneId;

	/**
	 * true: 출입가능(인가구역), false: 통제구역(비인가구역, 기본값)
	 */
	private BooleanType permitted = BooleanType.FALSE;

	private String additionalAttributeRaw = "{}";

	private Integer startDate;

	private Integer endDate;

	private Integer modDate;

	private Integer regDate;

	public Long getBeaconNum() {
		return beaconNum;
	}

	public void setBeaconNum(Long beaconNum) {
		this.beaconNum = beaconNum;
	}

	public void setBeaconNum(Integer beaconNum) {
		this.beaconNum = beaconNum.longValue();
	}

	public ZoneType getZoneType() {
		return zoneType;
	}

	public void setZoneType(ZoneType zoneType) {
		this.zoneType = zoneType;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public BooleanType getPermitted() {
		return permitted;
	}

	public void setPermitted(BooleanType permitted) {
		this.permitted = permitted;
	}

	@JsonIgnore
	public String getAdditionalAttributeRaw() {
		return additionalAttributeRaw;
	}

	public void setAdditionalAttributeRaw(String additionalAttributeRaw) {
		this.additionalAttributeRaw = additionalAttributeRaw;
	}

	public void setAdditionalAttribute(Map<String, Object> attribute) {
		this.additionalAttributeRaw = JsonUtils.writeValue(attribute);
	}

	public void putAdditionalAttribute(String key, Object value) {
		if(this.additionalAttributeRaw==null || this.additionalAttributeRaw=="") {
			this.additionalAttributeRaw = "{}";
		}
		Map<String, Object> attribute = this.getAdditionalAttribute();
		attribute.put(key, value);
		this.setAdditionalAttribute(attribute);

	}

	public Map<String, Object> getAdditionalAttribute() {
		if(StringUtils.isBlank(this.additionalAttributeRaw)) {
			this.additionalAttributeRaw = "{}";
		}
		Map<String, Object> map = JsonUtils.readValue(additionalAttributeRaw, Map.class);
		return map;
	}

	public Integer getStartDate() {
		return startDate;
	}

	public void setStartDate(Integer startDate) {
		this.startDate = startDate;
	}

	public Integer getEndDate() {
		return endDate;
	}

	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
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
}
