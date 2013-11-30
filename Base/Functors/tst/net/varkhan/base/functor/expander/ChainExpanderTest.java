package net.varkhan.base.functor.expander;

import junit.framework.TestCase;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/13
 * @time 6:27 PM
 */
public class ChainExpanderTest extends TestCase {

    public void testChain() {
        ChainExpander<String, String[][], Object> e = new ChainExpander<String,String[][],Object>(
                new ArrayExpander<String,Object>(), new ArrayExpander<String[],Object>()
        );
        Iterator<String> it00 = e.invoke(new String[][]{},null).iterator();
        assertFalse("!chain([]).hasNext()",it00.hasNext());
        Iterator<String> it10 = e.invoke(new String[][]{new String[]{}},null).iterator();
        assertFalse("!chain([[]]).hasNext()",it10.hasNext());
        Iterator<String> it11 = e.invoke(new String[][]{new String[]{"foo"}},null).iterator();
        assertTrue("chain([[\"foo\"]]).hasNext() 0",it11.hasNext());
        assertEquals("chain([[\"foo\"]]).next() 0","foo",it11.next());
        assertFalse("!chain([[\"foo\"]]).hasNext() 1",it11.hasNext());
        Iterator<String> it210 = e.invoke(new String[][]{new String[]{"foo"},new String[]{}},null).iterator();
        assertTrue("chain([[\"foo\"][]]).hasNext() 0",it210.hasNext());
        assertEquals("chain([[\"foo\"][]]).next() 0","foo",it210.next());
        assertFalse("!chain([[\"foo\"][]]).hasNext() 1",it210.hasNext());
        Iterator<String> it201 = e.invoke(new String[][]{new String[]{},new String[]{"foo"}},null).iterator();
        assertTrue("chain([[][\"foo\"]]).hasNext() 0",it201.hasNext());
        assertEquals("chain([[][\"foo\"]]).next() 0","foo",it201.next());
        assertFalse("!chain([[][\"foo\"]]).hasNext() 1",it201.hasNext());
        Iterator<String> it221 = e.invoke(new String[][]{new String[]{"bar","baz"},new String[]{"foo"}},null).iterator();
        assertTrue("chain([[\"bar\",\"baz\"][\"foo\"][]]).hasNext() 0",it221.hasNext());
        assertEquals("chain([[\"bar\",\"baz\"][\"foo\"]]).next() 0","bar",it221.next());
        assertEquals("chain([[\"bar\",\"baz\"][\"foo\"]]).next() 1","baz",it221.next());
        assertEquals("chain([[\"bar\",\"baz\"][\"foo\"]]).next() 2","foo",it221.next());
        assertFalse("!chain([[\"bar\",\"baz\"][\"foo\"]]).hasNext() 3",it221.hasNext());
    }


}
