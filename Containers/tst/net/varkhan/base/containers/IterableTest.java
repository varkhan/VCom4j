package net.varkhan.base.containers;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/2/13
 * @time 4:52 PM
 */
public class IterableTest extends TestCase {

    public void testEmpty() throws Exception {
        Iterable<Object> ix = new Iterable.Empty<Object>();
        Iterator<? extends Object> ii = ix.iterator();
        assertFalse("indexes().hasNext()", ii.hasNext());
    }

    public void testSingleton() throws Exception {
        int index=333;
        Iterable<Object> ix = new Iterable.Singleton<Object>(index);
        Iterator<? extends Object> ii = ix.iterator();
        assertTrue("indexes().hasNext()", ii.hasNext());
        assertEquals("indexes().next()==index",index,ii.next());
        assertFalse("indexes().hasNext()", ii.hasNext());
    }

    public void testEnumerate() throws Exception {
        Object[] idx0 = new Object[] {};
        Iterable<Object> ix0 = new Iterable.Enumerate<Object>(idx0);
        Iterator<? extends Object> ii0 = ix0.iterator();
        assertFalse("indexes().hasNext()", ii0.hasNext());
        Object[] idx1 = new Object[] {4};
        Iterable<Object> ix1 = new Iterable.Enumerate<Object>(idx1);
        Iterator<? extends Object> ii1 = ix1.iterator();
        assertTrue("indexes().hasNext()", ii1.hasNext());
        assertEquals("indexes().next()==min",idx1[0],ii1.next());
        assertFalse("indexes().hasNext()", ii1.hasNext());
        Object[] idx2 = new Object[] {4, 5, 6};
        Iterable<Object> ix2 = new Iterable.Enumerate<Object>(idx2);
        Iterator<? extends Object> ii2 = ix2.iterator();
        assertTrue("indexes().hasNext()", ii2.hasNext());
        assertEquals("indexes().next()==min",idx2[0],ii2.next());
        assertEquals("indexes().next()==..",idx2[1],ii2.next());
        assertEquals("indexes().next()==..",idx2[2],ii2.next());
        assertFalse("indexes().hasNext()", ii2.hasNext());
    }

    public void testSequence() throws Exception {
        Object[] idx1 = new Object[] {4, 5, 6 };
        Iterable<Object> ix1 = new Iterable.Enumerate<Object>(idx1);
        Object[] idx2 = new Object[] {17, 18};
        Iterable<Object> ix2 = new Iterable.Enumerate<Object>(idx2);
        Iterable<Object> ix = new Iterable.Sequence<Object>(ix1, ix2);
        Iterator<? extends Object> ii = ix.iterator();
        assertTrue("indexes().hasNext()", ii.hasNext());
        assertEquals("indexes().next()==min",idx1[0],ii.next());
        assertEquals("indexes().next()==...",idx1[1],ii.next());
        assertEquals("indexes().next()==...",idx1[2],ii.next());
        assertEquals("indexes().next()==...",idx2[0],ii.next());
        assertEquals("indexes().next()==...",idx2[1],ii.next());
        assertFalse("indexes().hasNext()", ii.hasNext());
    }

}
