package spring.config.exception;

import framework.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ucjung on 2017-09-06.
 */
public class BaseExceptionResolver implements HandlerExceptionResolver {
    static final private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    @ResponseBody
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        httpServletResponse.reset();
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");

        ModelAndView model = new ModelAndView(new MappingJackson2JsonView());
        if (e instanceof BaseException){
            BaseException ex = (BaseException) e;
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            model.addObject("result", "fail");
            model.addObject("code", ex.getErrorCode());
            if (ex.getMessage() != null)
                model.addObject("message", ex.getMessage());

            logger.error("{} : {}", ex.getErrorCode(), ex.getMessage());
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            model.addObject("result", "error");
            model.addObject("code", HttpStatus.INTERNAL_SERVER_ERROR.value());

            logger.error("{} : {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
        return model;
    }
}
