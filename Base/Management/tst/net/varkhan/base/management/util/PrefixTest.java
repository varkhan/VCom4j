package net.varkhan.base.management.util;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PrefixTest extends TestCase {

    public void testEnumeratePrefixesAsc() throws Exception {
        List<String> pfx = new ArrayList<String>();
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesAsc('.', "")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList(""),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesAsc('.', "foo")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("foo",""),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesAsc('.', ".foo")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList(".foo",""),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesAsc('.', "foo.")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("foo.","foo",""),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesAsc('.', "foo..bar.baz")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("foo..bar.baz","foo..bar","foo.","foo",""),pfx);
    }

    public void testEnumeratePrefixesDes() throws Exception {
        List<String> pfx = new ArrayList<String>();
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesDes('.', "")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList(""),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesDes('.', "foo")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("","foo"),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesDes('.', ".foo")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("",".foo"),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesDes('.', "foo.")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("","foo","foo."),pfx);
        pfx.clear();
        for(String p: Prefix.enumeratePrefixesDes('.', "foo..bar.baz")) {
            pfx.add(p);
        }
        assertEquals(Arrays.asList("","foo","foo.","foo..bar","foo..bar.baz"),pfx);
    }

}