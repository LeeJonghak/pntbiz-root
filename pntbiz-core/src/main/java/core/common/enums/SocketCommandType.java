package core.common.enums;

/**
 * Created by ucjung on 2017-06-15.
 */
public enum SocketCommandType {
    UPDATE_MARKER(10, "UpdateMarker"),
    NOTIFICATION(20, "Notification");

    private Integer value;
    private String command;

    SocketCommandType(Integer value, String command) {
        this.value = value;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Integer getValue() {
        return value;
    }
}
