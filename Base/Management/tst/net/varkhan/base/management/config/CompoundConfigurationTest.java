package net.varkhan.base.management.config;

import junit.framework.TestCase;

import java.util.*;


public class CompoundConfigurationTest extends TestCase {

    public void testGet() throws Exception {
        CompoundConfiguration<MapContext> cfg = new CompoundConfiguration<MapContext>(
                new MapContext(null,new HashMap<String,Object>()),
                new MapContext("c1",new HashMap<String,Object>()),
                new MapContext("c2",new HashMap<String,Object>())
        );
        assertEquals("k1", null, cfg.get("", "k1"));
        assertEquals("c1:k1", null, cfg.get("c1", "k1"));
        cfg.context(null).add("k1",1);
        cfg.context(null).add("kA","A");
        cfg.context(null).add("kw","w");
        cfg.context("c1").add("k1",11);
        cfg.context("c1").add("kA","1A");
        cfg.context("c1").add("kx","x");
        cfg.context("c2").add("k1",21);
        cfg.context("c2").add("kA","2A");
        cfg.context("c2").add("ky","y");
        assertEquals("get(k1)", 1, cfg.get("", "k1"));
        assertEquals("get(kA)", "A", cfg.get("", "kA"));
        assertEquals("get(c1,k1)", 11, cfg.get("c1", "k1"));
        assertEquals("get(c1,kA)", "1A", cfg.get("c1", "kA"));
        assertEquals("get(c1,kB)", null, cfg.get("c1", "kB"));
        assertEquals("get(c2,k1)", 21, cfg.get("c2", "k1"));
        assertEquals("get(c2,kA)","2A",cfg.get("c2","kA"));
        assertEquals("get(c2,kB)", null, cfg.get("c2", "kB"));
    }

    public void testContexts() throws Exception {
        CompoundConfiguration<MapContext> cfg = new CompoundConfiguration<MapContext>(
                new MapContext(null,new HashMap<String,Object>()),
                new MapContext("c1",new HashMap<String,Object>()),
                new MapContext("c2",new HashMap<String,Object>())
        );
        Set<String> cx0 = new HashSet<String>();
        for(String cx: cfg.contexts()) cx0.add(cx);
        assertEquals("contexts()", new HashSet<String>(Arrays.asList("","c1","c2")),cx0);
        cfg.context(null).add("k1",1);
        cfg.context(null).add("kA","A");
        cfg.context(null).add("kw","w");
        cfg.context("c1").add("k1",11);
        cfg.context("c1").add("kA","1A");
        cfg.context("c1").add("kx","x");
        cfg.context("c2").add("k1",21);
        cfg.context("c2").add("kA","2A");
        cfg.context("c2").add("ky","y");
        Set<String> cxs = new HashSet<String>();
        for(String cx: cfg.contexts()) cxs.add(cx);
        assertEquals("contexts()", new HashSet<String>(Arrays.asList("","c1","c2")),cxs);
    }

    public void testContext() throws Exception {
        CompoundConfiguration<MapContext> cfg = new CompoundConfiguration<MapContext>(
                new MapContext(null,new HashMap<String,Object>()),
                new MapContext("c1",new HashMap<String,Object>()),
                new MapContext("c2",new HashMap<String,Object>())
        );
        cfg.context(null).add("k1",1);
        cfg.context(null).add("kA","A");
        cfg.context(null).add("kw","w");
        cfg.context("c1").add("k1",11);
        cfg.context("c1").add("kA","1A");
        cfg.context("c1").add("kx","x");
        cfg.context("c2").add("k1",21);
        cfg.context("c2").add("kA","2A");
        cfg.context("c2").add("ky","y");
        // Def
        Configuration.Context cxd = cfg.context(null);
        assertTrue("has(k1)",cxd.has("k1"));
        Map<String,Object> md = new HashMap<String,Object>();
        for(Configuration.Entry e: cxd) {
            assertEquals("cx.ctx()","",e.ctx());
            md.put(e.key(),e.value());
        }
        assertEquals("cx.size()",3,md.size());
        assertEquals("cx.get(k1)",1,md.get("k1"));
        assertEquals("cx.get(kA)","A",md.get("kA"));
        assertEquals("cx",md,cxd.get());
        // c1
        Configuration.Context cx1 = cfg.context("c1");
        assertTrue("has(k1)",cx1.has("k1"));
        Map<String,Object> m1 = new HashMap<String,Object>();
        for(Configuration.Entry e: cx1) {
            assertEquals("cx.ctx()","c1",e.ctx());
            m1.put(e.key(),e.value());
        }
        assertEquals("cx.size()",3,m1.size());
        assertEquals("cx.get(k1)",11,m1.get("k1"));
        assertEquals("cx.get(kA)","1A",m1.get("kA"));
        assertEquals("cx",m1,cx1.get());
        // c2
        Configuration.Context cx2 = cfg.context("c2");
        assertTrue("has(k1)",cx2.has("k1"));
        Map<String,Object> m2 = new HashMap<String,Object>();
        for(Configuration.Entry e: cx2) {
            assertEquals("cx.ctx()","c2",e.ctx());
            m2.put(e.key(),e.value());
        }
        assertEquals("cx.size()",3,m2.size());
        assertEquals("cx.get(k1)",21,m2.get("k1"));
        assertEquals("cx.get(kA)","2A",m2.get("kA"));
        assertEquals("cx",m2,cx2.get());
    }

    public void testAddDef() throws Exception {
        MapConfiguration cfg = new MapConfiguration();
        assertEquals("k1", null, cfg.get("", "k1"));
        assertEquals("c1:k1", null, cfg.get("c1", "k1"));
        cfg.add("k1",1);
        cfg.add("kA","A");
        cfg.add("kw","w");
        assertEquals("get(k1)", 1, cfg.get("", "k1"));
        assertEquals("get(kA)", "A", cfg.get("", "kA"));
    }

    public void testAdd() throws Exception {
        MapConfiguration cfg = new MapConfiguration();
        assertEquals("k1", null, cfg.get("", "k1"));
        assertEquals("c1:k1", null, cfg.get("c1", "k1"));
        cfg.add("c1","k1",11);
        cfg.add("c1","kA","1A");
        cfg.add("c1","kx","x");
        cfg.add("c2","k1",21);
        cfg.add("c2","kA","2A");
        cfg.add("c2","ky","y");
        assertEquals("get(c1,k1)", 11, cfg.get("c1", "k1"));
        assertEquals("get(c1,kA)", "1A", cfg.get("c1", "kA"));
        assertEquals("get(c2,k1)", 21, cfg.get("c2", "k1"));
        assertEquals("get(c2,kA)","2A",cfg.get("c2","kA"));
    }

}