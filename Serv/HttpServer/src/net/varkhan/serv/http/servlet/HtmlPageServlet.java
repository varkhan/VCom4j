package net.varkhan.serv.http.servlet;

import net.varkhan.base.containers.array.CharArrays;
import net.varkhan.core.pres.format.HtmlDocFormatter;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.widget.page.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/17/12
 * @time 6:30 PM
 */
public class HtmlPageServlet extends HttpServlet {

    protected Page<HtmlFormatter,String[],Map<String,String[]>> page;

    public HtmlPageServlet() {
    }

    public HtmlPageServlet(Page<HtmlFormatter,String[],Map<String,String[]>> page) {
        this.page=page;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        render(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        render(req, resp);
    }

    @SuppressWarnings({ "unchecked" })
    protected void render(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HtmlDocFormatter fmt = new HtmlDocFormatter(resp.getWriter());
        fmt.setBaseUrl(req.getServletPath());
        String path=req.getPathInfo();
        String[] loc;
        if(path==null) loc = new String[0];
        else {
            if(path.startsWith("/")) path=path.substring(1);
            if(path.endsWith("/")) path=path.substring(0,path.length()-1);
            loc = CharArrays.split(path,'/',Integer.MAX_VALUE);
        }
        Map<String,String[]> par=(Map<String,String[]>) req.getParameterMap();
        fmt.setTitle(page.title(loc,par));
        Collection<String> cssurls = page.getLinks("cssurl", loc, par);
        if(cssurls!=null) for(String url: cssurls) fmt.addCssUrl(url);
        Collection<String> cssdefs = page.getLinks("cssdef", loc, par);
        if(cssdefs!=null) for(String def: cssdefs) fmt.addCssDef(def);
        fmt.open();
        fmt.append("\n");
        page.render(fmt, loc, par);
        fmt.append("\n");
        fmt.close();
    }


}
