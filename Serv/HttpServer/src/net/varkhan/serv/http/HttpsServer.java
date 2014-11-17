package net.varkhan.serv.http;

import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.widget.page.HtmlWidget;
import net.varkhan.serv.http.servlet.AdminServlet;
import net.varkhan.serv.http.servlet.HtmlPageServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;

import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/17/12
 * @time 6:10 PM
 */
public class HttpsServer extends HttpServer {

    private final String keystore;
    protected String basepath="/";
    protected int    port    =8080;

    public HttpsServer(String keystore) {
        this.keystore=keystore;
    }

    public void configure() throws Exception {
        SslSocketConnector sslConnector=new SslSocketConnector();
        sslConnector.setServer(server);
        sslConnector.setKeystore(keystore);
        sslConnector.setPort(port);

        server.setConnectors(new Connector[] { sslConnector });
        context.setContextPath(basepath);
        super.configure();
    }

    public static void main(String[] args) throws Exception {
        HttpsServer sv=new HttpsServer("keystore.jks");
        sv.addServlet("/adm/*", new AdminServlet(sv));
        HtmlWidget<HtmlFormatter,String[],Map<String,String[]>> main=new HtmlWidget<HtmlFormatter,String[],Map<String,String[]>>("main");
        main.setName("main");
        main.setTitle("Main page");
        main.setDesc("Main page");
        sv.addServlet("/*", new HtmlPageServlet(main));
        sv.start();
        sv.join();
    }

}
