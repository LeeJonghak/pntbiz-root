package core.common.enums;

/**
 * Created by ucjung on 2017-06-16.
 */
public enum PresenceEventType {
    FLOOR_IN(10),
    FLOOR_OUT(11),
    SOS_ON(21),
    SOS_OFF(22),
    GEOFENCE_IN(31),
    GEOFENCE_OUT(32);

    private Integer value;
    PresenceEventType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
