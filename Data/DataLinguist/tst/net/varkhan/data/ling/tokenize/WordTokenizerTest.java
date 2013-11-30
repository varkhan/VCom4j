package net.varkhan.data.ling.tokenize;

import junit.framework.TestCase;
import net.varkhan.base.functor.Expander;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 5:13 PM
 */
public class WordTokenizerTest extends TestCase {

    public void testTokenizer() throws Exception {
        Expander<String,Reader,Object> tkz = new WordTokenizer<Object>();
        String str = "My new fool-proof tokenizer is running, and spits (out) words!";
        Iterable<String> tkn = tkz.invoke(new StringReader(str),null);
        Iterator<String> itr = tkn.iterator();
        assertTrue(itr.hasNext());
        assertEquals("My",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("new",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("fool-proof",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("tokenizer",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("is",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("running",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("and",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("spits",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("out",itr.next());
        assertTrue(itr.hasNext());
        assertEquals("words",itr.next());
        assertFalse(itr.hasNext());
    }

}
