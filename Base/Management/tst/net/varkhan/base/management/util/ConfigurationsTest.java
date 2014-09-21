package net.varkhan.base.management.util;

import junit.framework.TestCase;
import net.varkhan.base.management.config.SettableConfiguration;


public class ConfigurationsTest extends TestCase {

    public void testEnvironment() {
        SettableConfiguration.Context ctx = Configurations.configs().context("environment");
        assertTrue("PATH", ctx.has("PATH"));
        assertTrue("PATH", null!=ctx.get("PATH"));
        assertEquals("PATH",System.getenv("PATH"),ctx.get("PATH"));
        assertEquals("var_does_not_exist", null, ctx.get("var_does_not_exist"));
        assertTrue("var_does_not_exist",ctx.add("var_does_not_exist","foo_random_value"));
        assertEquals("var_does_not_exist", "foo_random_value", ctx.get("var_does_not_exist"));
        assertEquals("var_does_not_exist","foo_random_value",System.getenv("var_does_not_exist"));
    }

    public void testProperties() {
        SettableConfiguration.Context ctx = Configurations.configs().context("properties");
        assertTrue("PATH", ctx.has("java.version"));
        assertTrue("PATH", null!=ctx.get("java.version"));
        assertEquals("PATH",System.getProperty("java.version"),ctx.get("java.version"));
        assertEquals("var_does_not_exist", null, ctx.get("var_does_not_exist"));
        assertTrue("var_does_not_exist",ctx.add("var_does_not_exist","foo_random_value"));
        assertEquals("var_does_not_exist", "foo_random_value", ctx.get("var_does_not_exist"));
        assertEquals("var_does_not_exist","foo_random_value",System.getenv("var_does_not_exist"));
    }

}