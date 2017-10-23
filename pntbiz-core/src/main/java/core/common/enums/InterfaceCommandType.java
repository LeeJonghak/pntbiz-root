package core.common.enums;

import framework.database.EnumValue;

/**
 * Created by ucjung on 2017-06-15.
 */
public enum InterfaceCommandType implements EnumValue<String> {
    LOCATION_CHANGE("LC"),
    FLOOR_IN("FI"),
    FLOOR_STAY("FS"),
    FLOOR_OUT("FO"),
    GEOFENCE_IN("GI"),
    GEOFENCE_STAY("GS"),
    GEOFENCE_OUT("GO"),
    RESTRICTION_IN("RI"),
    RESTRICTION_STAY("RS"),
    RESTRICTION_OUT("RO"),
    LOST_SIGNAL("LS"),
    SOS_ON("SN"),
    SOS_OFF("SF")
    ;
    private String value;
    InterfaceCommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
