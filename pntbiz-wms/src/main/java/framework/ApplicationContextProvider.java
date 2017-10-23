package framework;

import framework.web.language.ExposedResourceMessageBundleSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Properties;

/**
 */
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static Properties getConfigProperties() {
        return context.getBean("config", Properties.class);
    }

    public static String getMessage(String code) {

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        SessionLocaleResolver localeResolver = context.getBean("localeResolver", SessionLocaleResolver.class);
        Locale locale = localeResolver.resolveLocale(request);

        ExposedResourceMessageBundleSource messageBundleSource = (ExposedResourceMessageBundleSource)ApplicationContextProvider.getApplicationContext().getParentBeanFactory().getBean("messageSource");
        String message = messageBundleSource.getMessages(locale).getProperty(code);
        return message;
    }

}
