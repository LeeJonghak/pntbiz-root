package framework.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ucjung on 2017-04-20.
 */
public class BaseException extends RuntimeException {
    private boolean isLog = true;
    private String errorCode;
    private String message;

    public BaseException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(boolean log) {
        isLog = log;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
