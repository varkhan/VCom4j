package net.varkhan.serv.http;

import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.widget.page.HtmlWidget;
import net.varkhan.serv.http.servlet.AdminServlet;
import net.varkhan.serv.http.servlet.HtmlPageServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.bio.SocketConnector;

import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/17/12 log
 * @time 6:10 PM
 */
public class HttpServer extends WebServer {

    protected String basepath="/";
    protected int    port    =8080;

    public void configure() throws Exception {
        SocketConnector connector = new SocketConnector();
        connector.setServer(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });
        context.setContextPath(basepath);
        super.configure();
    }

    public static void main(String[] args) throws Exception{
        HttpServer sv = new HttpServer();
        sv.addServlet("/adm/*",new AdminServlet(sv));
        HtmlWidget<HtmlFormatter,String[],Map<String,String[]>> main=new HtmlWidget<HtmlFormatter,String[],Map<String,String[]>>("main");
        main.setName("main");
        main.setTitle("Main page");
        main.setDesc("Main page");
        sv.addServlet("/*",new HtmlPageServlet(main));
        sv.start();
        sv.join();
    }

}
