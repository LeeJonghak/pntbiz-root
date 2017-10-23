package framework.exception;

/**
 * Created by ucjung on 2017-06-05.
 */
public enum ExceptionType {
    NULL_POINT_EXCEPTION("200", "error : NULL Point Excetion"),
    JSON_PARSE_EXCEPTION("200", "error : Json Parse Excetion"),
    REQUEST_DATA_VALID_EXCEPTION("200", "error : Invalid request data"),
    EXTERNAL_UNKNOWN_COMMAND("200", "error : Unknown command of external interfaces");

    private String exceptionCode;
    private String exceptionMessage;

    public String getExceptionCode() {
        return exceptionCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    ExceptionType(String code, String message) {
        this.exceptionCode = code;
        this.exceptionMessage = message;
    }

}
