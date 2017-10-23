package core.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import framework.database.EnumValue;

/**
 * Created by ucjung on 2017-06-15.
 */
public enum ZoneType implements EnumValue<String> {
    FLOOR("F"),
    GEOFENCE("G"),
    LOGICALAREA("L");

    private String value;

    ZoneType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

	@JsonValue
	public String getNameJsonValue() {
		String string = super.toString();
		if(string!=null) {
			return string.toLowerCase();
		} else {
			return null;
		}
	}
}
