package core.common.enums;

/**
 * Created by ucjung on 2017-06-15.
 */
public enum NotificationType {
    EMAIL(10, "Email"),
    SMS(20, "SMS"),
    SOCKET(20, "SOCKET");

    private Integer value;
    private String command;

    NotificationType(Integer value, String command) {
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
