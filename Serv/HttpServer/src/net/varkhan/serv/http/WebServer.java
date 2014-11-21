package net.varkhan.serv.http;

import net.varkhan.base.management.logging.compat.log4j.Log;
import net.varkhan.base.management.logging.compat.log4j.LogManager;
import net.varkhan.base.management.state.SimpleStateCheck;
import net.varkhan.base.management.state.health.HealthLevel;
import net.varkhan.base.management.state.health.HealthState;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;
import net.varkhan.serv.BaseServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/15/14
 * @time 6:10 PM
 */
public abstract class WebServer extends BaseServer {
    private static final Log log=LogManager.getLogger(HttpServer.class);
    protected final ServletContextHandler context;
    protected Server server = null;

    public WebServer() {
        server=new Server();
        context=new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
    }

    public void addServlet(String path, HttpServlet serv) {
        context.addServlet(new ServletHolder(serv), path);
    }

    public void configure() throws Exception {
        if(configured) return;
        health.add(new SimpleStateCheck<HealthLevel,HealthState>("ServerThread", "If the main server thread is running", HealthLevel.MAJOR) {
            @Override
            public HealthState state() {
                if(server==null) return HealthState.FAILED;
                if(!server.isRunning()) return HealthState.FAILED;
                if(server.isFailed()) return HealthState.FAILED;
                return HealthState.HEALTHY;
            }

            @Override
            public String reason() {
                if(server==null) return name+" is not defined";
                if(!server.isRunning()) return name+" is not running";
                if(server.isFailed()) return name+" is not failed";
                return name+" is transitioning";

            }

            @Override
            public void update() {
            }
        });
        status.add(new SimpleStateCheck<LifeLevel,LifeState>("ServerThread","If the main server thread is running",LifeLevel.SYSTEM) {
            @Override
            public LifeState state() {
                if(server==null) return LifeState.STOPPED;
                if(server.isStarting()) return LifeState.STARTING;
                if(server.isStarted()) return LifeState.RUNNING;
                if(server.isStopping() || "STOPPING".equalsIgnoreCase(server.getState())) return LifeState.STOPPING;
                if(server.isStopped() || "STOPPED".equalsIgnoreCase(server.getState())) return LifeState.STOPPED;
                if(!server.isRunning()) return LifeState.STOPPED;
                return LifeState.RUNNING;
            }

            @Override
            public String reason() {
                if(server==null) return name+" is not defined";
                if(server.isStarting()) return name+" is starting";
                if(server.isStarted()) return name+" is started";
                if(server.isStopping()) return name+" is stopping";
                if(server.isStopped()) return name+" is stopped";
                if(!server.isRunning()) return name+" is not running";
                if(server.isFailed()) return name+" is failed";
                return name+" is in an unknown state";
            }

            @Override
            public void update() {
            }
        });
        super.configure();
    }

    public void start() throws Exception {
        log.info(this.getClass().getSimpleName()+" starting");
        synchronized(this) {
            configure();
            server.start();
            health.start();
            status.start();
            status.update();
        }
        log.info(this.getClass().getSimpleName()+" started");
    }

    public void stop() throws Exception {
        log.info(this.getClass().getSimpleName()+" stopping");
        synchronized(this) {
            server.stop();
            status.update();
            health.stop();
            status.stop();
        }
    }

    public void join() throws Exception {
        Server sv;
        synchronized(this) {
            sv=server;
        }
        if(sv!=null) sv.join();
    }
}
