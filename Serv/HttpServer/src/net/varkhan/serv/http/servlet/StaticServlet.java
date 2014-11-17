package net.varkhan.serv.http.servlet;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.Resource;

import java.net.URL;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/6/12
 * @time 1:36 PM
 */
public class StaticServlet extends DefaultServlet {
    private final String resourcePath;

    public StaticServlet(String resourcePath) {
        this.resourcePath=resourcePath;
    }

    @Override
    public Resource getResource(String pathInContext) {
        if (!pathInContext.startsWith(resourcePath)) {
            pathInContext = resourcePath + pathInContext;
        }
        URL url = this.getClass().getResource(pathInContext);
        try {
            return Resource.newResource(url);
        }
        catch (Exception e) {
            return null;
        }
    }

}
