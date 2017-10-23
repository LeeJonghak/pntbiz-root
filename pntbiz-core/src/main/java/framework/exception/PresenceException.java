package framework.exception;

/**
 * Created by ucjung on 2017-06-05.
 */
public class PresenceException extends RuntimeException{
    private boolean isLog = true;
    private ExceptionType exceptionType;

    public PresenceException(ExceptionType exceptionEnum) {
        this.exceptionType = exceptionEnum;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }

    public String getErrorCode() {
        return exceptionType.getExceptionCode();
    }

    public String getMessage() {
        return exceptionType.getExceptionMessage();
    }

}
