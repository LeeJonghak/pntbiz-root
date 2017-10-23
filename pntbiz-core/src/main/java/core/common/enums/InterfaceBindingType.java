package core.common.enums;

import framework.database.EnumValue;

/**
 * Created by ucjung on 2017-08-29.
 */
public enum InterfaceBindingType implements EnumValue<String>{

    LOCATION_COMMON("LC"),      // 측위 신호 전체 공통
    FLOOR_COMMON("FC"),         // 층 공통
    FLOOR("FF"),                // 층
    GEOFENCE_COMMON("GC"),      // Geofence 공통
    GEOFENCE_GROUP("GG");       // Geofence 그룹

    private String value;
    InterfaceBindingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
