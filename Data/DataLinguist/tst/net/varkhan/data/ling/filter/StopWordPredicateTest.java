package net.varkhan.data.ling.filter;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 4:33 PM
 */
public class StopWordPredicateTest extends TestCase {

    public void testStopWord() throws Exception {
        StopWordPredicate<Object> p = new StopWordPredicate<Object>(4,2,0.25,1,0.1,2);
        assertTrue(p.invoke("double",null));
        assertFalse(p.invoke("int",null));
        assertFalse(p.invoke("g1p2",null));
        assertTrue(p.invoke("g1p2dltr",null));
        assertFalse(p.invoke("group!es",null));
        assertTrue(p.invoke("group!esgloups",null));
        assertFalse(p.invoke("group!es%gloups",null));
        assertTrue(p.invoke("doubloon",null));
        assertFalse(p.invoke("doublooon",null));
    }

    public void testCountSymbols() throws Exception {
        assertEquals(0,StopWordPredicate.countSymbols("avueyfv"));
        assertEquals(1,StopWordPredicate.countSymbols("avueyf&"));
        assertEquals(1,StopWordPredicate.countSymbols("av#eyfv"));
        assertEquals(2,StopWordPredicate.countSymbols("av@eyf%"));
    }

    public void testCountDigits() throws Exception {
        assertEquals(0,StopWordPredicate.countDigits("avueyfv"));
        assertEquals(1,StopWordPredicate.countDigits("avueyf1"));
        assertEquals(1,StopWordPredicate.countDigits("av2eyfv"));
        assertEquals(2,StopWordPredicate.countDigits("av2eyf1"));
    }

    public void testCountRepeats() throws Exception {
        assertEquals(0,StopWordPredicate.countRepeats("avueyfv"));
        assertEquals(1,StopWordPredicate.countRepeats("avveyfv"));
        assertEquals(2,StopWordPredicate.countRepeats("avvefff"));
        assertEquals(5,StopWordPredicate.countRepeats("avvvvvv"));
    }
}
