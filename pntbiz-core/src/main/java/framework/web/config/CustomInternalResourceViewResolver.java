package framework.web.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

/**
 * InternalResourceViewResolver Customizing
 *
 * 사이트별로 다른 View를 제공하여야 될 경우
 * ViewResolver 레벨에서 다른 View로 경로를 변경하기 위하여 작성함
 *
 * Created by ucjung on 2017-05-15.
 */
public class CustomInternalResourceViewResolver extends UrlBasedViewResolver {

    static final private Logger logger = LoggerFactory.getLogger(CustomInternalResourceViewResolver.class);
    private static final boolean jstlPresent = ClassUtils.isPresent("javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());
    private Boolean alwaysInclude;

    private static final String customViewPath = "/_custom/";
    private @Value("#{config['site.shortName']?:''}") String siteShortName;
	private @Value("#{config['view.version']?:'v1'}") String viewVersion;
	private @Value("#{config['staticURL']?:''}") String staticUrl;

    @Autowired
    private ServletContext servletContext;

    public CustomInternalResourceViewResolver() {
        Class viewClass = this.requiredViewClass();
        if(InternalResourceView.class == viewClass && jstlPresent) {
            viewClass = JstlView.class;
        }

        this.setViewClass(viewClass);
    }

    public CustomInternalResourceViewResolver(String prefix, String suffix) {
        this();
        this.setPrefix(prefix);
        this.setSuffix(suffix);
    }

    protected Class<?> requiredViewClass() {
        return InternalResourceView.class;
    }

    public void setAlwaysInclude(boolean alwaysInclude) {
        this.alwaysInclude = Boolean.valueOf(alwaysInclude);
    }

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {

		String versionViewName = getSiteCustomView(viewName);
        InternalResourceView view = (InternalResourceView)super.buildView(versionViewName);
        if(this.alwaysInclude != null) {
            view.setAlwaysInclude(this.alwaysInclude.booleanValue());
        }

		view.setBeanName(versionViewName);
        view.setPreventDispatchLoop(true);
        return view;
    }

    /*
     * Site Custom View가 있을 경우 해당 View Name으로 변경한다.
     * 2017-08-08 nsyun View 버전 기능 추가
     */
    private String getSiteCustomView(String viewName) throws MalformedURLException {

        String siteCustomViewName = customViewPath + siteShortName + viewName;
		String siteCustomViewPath = getPrefix() + viewVersion + siteCustomViewName + getSuffix();

        return (servletContext.getResource(siteCustomViewPath) == null) ? "/"+viewVersion+viewName
				: "/"+viewVersion+siteCustomViewName;
    }

	@Override
	protected Object getCacheKey(String viewName, Locale locale) {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		HttpServletRequest request = sra.getRequest();

		if(request.getParameter("viewVersion")!=null) {
			viewVersion = request.getParameter("viewVersion");
		}
		if(viewVersion==null) {
			viewVersion = "";
		}

		Map<String, Object> viewProperty = (Map<String, Object>)request.getAttribute("viewProperty");
		if(viewProperty==null) {
			viewProperty = new HashMap<String, Object>();
		}
		String staticUrlLastChar = "";
		if(staticUrl.length()>0) {
			staticUrlLastChar = staticUrl.substring(staticUrl.length() - 1, staticUrl.length());
		}
		viewProperty.put("viewPathPrefix", this.getPrefix()+viewVersion);
		viewProperty.put("viewVersion", viewVersion);
		viewProperty.put("staticUrl", staticUrl+(StringUtils.equals(staticUrlLastChar, "/")?"":"/")+viewVersion);
		request.setAttribute("viewProperty", viewProperty);

		return super.getCacheKey(viewVersion+viewName, locale);
	}

}

