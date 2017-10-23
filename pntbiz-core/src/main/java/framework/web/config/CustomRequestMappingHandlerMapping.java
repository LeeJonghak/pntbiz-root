package framework.web.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * Created by ucjung on 2017-05-19.
 */
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    static final private Logger logger = LoggerFactory.getLogger(CustomRequestMappingHandlerMapping.class);

    @Autowired
    private ApplicationContext applicationContext;

    private @Value("#{config['site.shortName']?:''}") String siteShortName;
    private String customClassPackagePrefix = "custom";


    /**
     * Common Class와 SiteCustom Class를 선별하여 HandlerMethod를 등록
     * * 특정 Class가 Common Class인지 SiteCustom Class인지 확인
     * * Common Class인 경우 SiteCustm Class의 존재 및 동일 Method 존재여부를 확인
     * * 존재할 경우 CommonClass의 Method를 Handler
     */
    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        boolean canRegisterHandlerMethod = true;

        Class currentClass = method.getDeclaringClass();
        String currentClassName = currentClass.getName();

        if (currentClassName.startsWith(customClassPackagePrefix) == true) {
            if (siteShortName.equals("")) {
                canRegisterHandlerMethod = false;
            } else if (!currentClassName.startsWith(customClassPackagePrefix + "." + siteShortName)) {
                canRegisterHandlerMethod = false;
            }
        } else {
            String siteCustomClassName = siteShortName + currentClass.getSimpleName();
            Object siteCustomBean = getBeanByName(siteCustomClassName);
            if (siteCustomBean != null && !siteShortName.equals("")) {
                if (isSiteDeclaredMethod(siteCustomBean.getClass(), method) == true)
                    canRegisterHandlerMethod = false;
            }
        }

        if (canRegisterHandlerMethod == true) {
            logger.debug("========================> {} is mapping {} class  {} method", method.getAnnotation(RequestMapping.class).value()[0], currentClass.getName(), method.getName());
            super.registerHandlerMethod(handler, method, mapping);
        }
    }

    // 사이트 Cusrom Class에 현재 Mapping할 메소드와 동일한 Method가 있는지 확인 한다.
    private boolean isSiteDeclaredMethod(Class siteCustomClass, Method orignalMethod) {
        boolean isSiteDeclaredMethod = false;
        String sourceRequestMappingValue = orignalMethod.getAnnotation(RequestMapping.class).value()[0];
        for (Method method : siteCustomClass.getDeclaredMethods()) {
            RequestMapping siteRequestMappingAnnotation = method.getAnnotation(RequestMapping.class);
            if (siteRequestMappingAnnotation != null) {
                if (siteRequestMappingAnnotation.value()[0].equals(sourceRequestMappingValue)) {
                    isSiteDeclaredMethod = true;
                }
            }
        }
        return isSiteDeclaredMethod;
    }

    private Object getBeanByName(String className) {
        Object result = null;
        try{
            result = applicationContext.getBean(StringUtils.uncapitalize(className));
        } catch (NoSuchBeanDefinitionException ex) {
        }

        if (result == null) {
            try{
                result = applicationContext.getBean(className);
            } catch (NoSuchBeanDefinitionException ex) {
            }
        }

        return result;
    }
}
