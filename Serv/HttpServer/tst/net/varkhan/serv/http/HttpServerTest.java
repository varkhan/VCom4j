package net.varkhan.serv.http;

import junit.framework.TestCase;
import net.varkhan.base.management.state.health.HealthState;
import net.varkhan.base.management.state.lifecycle.LifeState;

import java.util.Random;


public class HttpServerTest extends TestCase {

    public void testLifecycle() throws Exception {
        HttpServer sv = new HttpServer();
        // Not a whole lot of things use the 15000-16000 port range, so we should be safe
        sv.port = 15000 + new Random().nextInt(1000);
        sv.start();
        Thread.sleep(100);
        assertEquals("running", HealthState.HEALTHY, sv.health().state());
        assertEquals("running", LifeState.RUNNING, sv.status().state());
        sv.stop();
        sv.join();
        Thread.sleep(100);
        assertEquals("stopped", HealthState.FAILED, sv.health().state());
        assertEquals("running", LifeState.STOPPED, sv.status().state());
    }

}