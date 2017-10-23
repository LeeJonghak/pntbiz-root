package wms.component.language;

import framework.ApplicationContextProvider;
import framework.web.language.ExposedResourceMessageBundleSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 */
@Controller
public class LanguageController {

    /*@Autowired
    private SessionLocaleResolver localeResolver;*/

    /*@Autowired
    private AcceptHeaderLocaleResolver localeResolver;*/

    @Autowired
    LocaleResolver localeResolver;

    @RequestMapping(value = "/language/languages.json", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> language(HttpServletRequest request) throws IOException {

        Locale locale = localeResolver.resolveLocale(request);

        Map<String, String> languageMap = new HashMap<String, String>();
        ExposedResourceMessageBundleSource message = (ExposedResourceMessageBundleSource) ApplicationContextProvider.getApplicationContext().getParentBeanFactory().getBean("messageSource");
        Properties properties = message.getMessages(locale);
        for(Object key: properties.keySet()) {
            String keyName = (String)key;
            String value = properties.getProperty(keyName);
            languageMap.put(keyName, value);
        }

        return languageMap;
    }

}
