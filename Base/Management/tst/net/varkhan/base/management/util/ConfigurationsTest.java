package net.varkhan.base.management.util;

import junit.framework.TestCase;
import net.varkhan.base.management.config.Configuration;
import net.varkhan.base.management.config.MapConfiguration;
import net.varkhan.base.management.config.SettableConfiguration;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;


public class ConfigurationsTest extends TestCase {

    public void testEnvironment() {
        SettableConfiguration.Context ctx = Configurations.sysconf().context("environment");
        assertTrue("PATH", ctx.has("PATH"));
        assertTrue("PATH", null!=ctx.get("PATH"));
        assertEquals("PATH",System.getenv("PATH"),ctx.get("PATH"));
        assertEquals("var_does_not_exist", null, ctx.get("var_does_not_exist"));
        assertTrue("var_does_not_exist",ctx.add("var_does_not_exist","foo_random_value"));
        assertEquals("var_does_not_exist", "foo_random_value", ctx.get("var_does_not_exist"));
        assertEquals("var_does_not_exist","foo_random_value",System.getenv("var_does_not_exist"));
    }

    public void testProperties() {
        SettableConfiguration.Context ctx = Configurations.sysconf().context("properties");
        assertTrue("PATH", ctx.has("java.version"));
        assertTrue("PATH", null!=ctx.get("java.version"));
        assertEquals("PATH",System.getProperty("java.version"),ctx.get("java.version"));
        assertEquals("var_does_not_exist", null, ctx.get("var_does_not_exist"));
        assertTrue("var_does_not_exist",ctx.add("var_does_not_exist","foo_random_value"));
        assertEquals("var_does_not_exist", "foo_random_value", ctx.get("var_does_not_exist"));
        assertEquals("var_does_not_exist","foo_random_value",System.getenv("var_does_not_exist"));
    }

    public void testLoadSave() throws Exception {
        // Java really needs a heredoc notation
        String cfgdata = ""
                // empty line
                + "\n"
                // no context defined, empty value
                + "foo.bar=\n"
                // no context defined, leading spaces
                + "\t  foo.baz=c0\n"
                + "   # comment\n"
                + "foo.baz1=c0\n"
                // Escaped, single line
                + "foo.\\=baz=\n"
                // Not a comment
                + "\\#not.a.comment=\n"
                // Escaped, double line
                + "foo.baz1=\\\n"
                + "\n"
                // Escaped, triple line
                + "foo.baz2=\\\n"
                + "\\\n"
                + "doof\n"
                + "foo.baz3=c0\n"
                // Context switch
                + "[dd]\n"
                + "foo.bar=c1\n"
                + "foo.baz1=c1\n"
                + "[] remaining text ignored\n"
                + "foo.baz3=c3\n"
                + "[dd]\n"
                + "foo.baz4=c4\n"
                + "[*]\n"
                + "foo.baz5=c5\n"
                ;
        Reader r = new StringReader(cfgdata);
        MapConfiguration cfg = new MapConfiguration();
        Configurations.loadConfig(cfg,r);
        Iterator<String> contexts=cfg.contexts().iterator();
        assertTrue("has def context", contexts.hasNext());
        assertEquals("def context is ''", "", contexts.next());
        assertTrue("has +1 context", contexts.hasNext());
        assertEquals("+1 context is 'dd'", "dd", contexts.next());
        assertFalse("!has +2 context", contexts.hasNext());
        assertEquals("*:foo.bar", "", cfg.get(null, "foo.bar"));
        assertEquals("*:foo.baz", "c0", cfg.get(null, "foo.baz"));
        assertEquals("*:foo.=baz", "", cfg.get(null, "foo.=baz"));
        assertEquals("*:foo.baz1", "\n", cfg.get(null, "foo.baz1"));
        assertEquals("*:foo.baz2", "\n\ndoof", cfg.get(null, "foo.baz2"));
        assertEquals("*:foo.baz3", "c3", cfg.get(null, "foo.baz3"));
        assertEquals("*:foo.baz5", "c5", cfg.get(null, "foo.baz5"));
        assertEquals("dd:foo.bar", "c1", cfg.get("dd", "foo.bar"));
        assertEquals("dd:foo.baz1", "c1", cfg.get("dd", "foo.baz1"));
        assertEquals("dd:foo.baz4","c4",cfg.get("dd","foo.baz4"));
        StringWriter w = new StringWriter();
        Configurations.saveConfig(cfg,w);
        assertEquals("cfg",""
                     + "[]\n"
                     + "\\#not.a.comment=\n"
                     + "foo.bar=\n"
                     + "foo.baz=c0\n"
                     + "foo.baz2=\\\n"
                     + "\\\n"
                     + "doof\n"
                     + "foo.baz3=c3\n"
                     + "foo.baz1=\\\n"
                     + "\n"
                     + "foo.baz5=c5\n"
                     + "foo.\\=baz=\n"
                     + "\n"
                     + "[dd]\n"
                     + "foo.bar=c1\n"
                     + "foo.baz4=c4\n"
                     + "foo.baz1=c1\n"
                     + "\n"
                     ,w.toString());
    }

}