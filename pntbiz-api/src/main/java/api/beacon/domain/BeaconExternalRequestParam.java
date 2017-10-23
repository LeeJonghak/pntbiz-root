package api.beacon.domain;

import java.util.List;
import java.util.Map;

/**
 * Created by nsyun on 17. 7. 20..
 */
public class BeaconExternalRequestParam {

	private String externalId;
	private String barcode;
	private List<Map<String, Object>> externalAttribute;
	private String restrictedZonePermitted;
	private List<Map<String, Object>> restrictedZone;

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

	public List<Map<String, Object>> getExternalAttribute() {
		return externalAttribute;
	}

	public void setJsonAttribute(List<Map<String, Object>> externalAttribute) {
		this.externalAttribute = externalAttribute;
	}

	public void setExternalAttribute(List<Map<String, Object>> externalAttribute) {
		this.externalAttribute = externalAttribute;
	}

	public String getRestrictedZonePermitted() {
		return restrictedZonePermitted;
	}

	public void setRestrictedZonePermitted(String restrictedZonePermitted) {
		this.restrictedZonePermitted = restrictedZonePermitted;
	}

	public List<Map<String, Object>> getRestrictedZone() {
		return restrictedZone;
	}

	public void setRestrictedZone(List<Map<String, Object>> restrictedZone) {
		this.restrictedZone = restrictedZone;
	}
}




