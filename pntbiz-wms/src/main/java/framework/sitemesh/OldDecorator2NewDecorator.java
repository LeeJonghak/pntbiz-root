package framework.sitemesh;


import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.compatability.Content2HTMLPage;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.BaseWebAppDecorator;
import com.opensymphony.module.sitemesh.RequestConstants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Adapts a SiteMesh 2 {@link com.opensymphony.module.sitemesh.Decorator} to a
 * SiteMesh 3 {@link com.opensymphony.sitemesh.Decorator}.
 *
 * @author Joe Walnes
 * @since SiteMesh 3
 */
public class OldDecorator2NewDecorator extends BaseWebAppDecorator implements RequestConstants {

	private final com.opensymphony.module.sitemesh.Decorator oldDecorator;

	public OldDecorator2NewDecorator(com.opensymphony.module.sitemesh.Decorator oldDecorator) {
		this.oldDecorator = oldDecorator;
	}

	protected void render(Content content, HttpServletRequest request, HttpServletResponse response,
						  ServletContext servletContext, SiteMeshWebAppContext webAppContext)
			throws IOException, ServletException {

		request.setAttribute(PAGE, new Content2HTMLPage(content, request));

		// see if the URI path (webapp) is set
		if (oldDecorator.getURIPath() != null) {
			// in a security conscious environment, the servlet container
			// may return null for a given URL
			if (servletContext.getContext(oldDecorator.getURIPath()) != null) {
				servletContext = servletContext.getContext(oldDecorator.getURIPath());
			}
		}

		Map<String, Object> viewProperty = (Map<String, Object>)request.getAttribute("viewProperty");
		String viewVersion = "";
		if(viewProperty!=null && viewProperty.containsKey("viewVersion")) {
			viewVersion = (String)viewProperty.get("viewVersion");
		}
		String pagePath = oldDecorator.getPage().replace("${viewVersion}", viewVersion);

		RequestDispatcher dispatcher = servletContext.getRequestDispatcher(pagePath);
		dispatcher.include(request, response);

		request.removeAttribute(PAGE);
	}

}