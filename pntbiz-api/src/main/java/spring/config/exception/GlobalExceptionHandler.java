package spring.config.exception;

import framework.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucjung on 2017-04-20.
 */
@ControllerAdvice
@RestController
@Deprecated
public class GlobalExceptionHandler {

    static final private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public Object handleBaseException(HttpServletRequest request, BaseException e){
        logger.error("[ERROR-]\t{}\t{}\t{}\n{}", request.getMethod(), request.getRequestURI(), e.getErrorCode(), e.getMessage());
        logger.error("{}", e.getMessage());
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("result", "error");
        res.put("code", e.getErrorCode());
        if (e.getMessage() != null) res.put("message", e.getMessage());
        return res;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception e) throws IOException {

        logger.error("[ERROR-]\t{}\t{}\t{}", request.getMethod(), request.getRequestURI(), 500, e.getStackTrace());
        logger.error("{}", e.getStackTrace());


        Map<String, Object> res = new HashMap<String, Object>();
        res.put("result", "error");
        res.put("code", "500");
        return res;
    }

}
