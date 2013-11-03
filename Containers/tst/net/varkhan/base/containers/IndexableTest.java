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
public class IndexableTest extends TestCase {

    public void testEmpty() throws Exception {
        Indexable ix = Indexable.EMPTY;
        assertTrue("isEmpty()",ix.isEmpty());
        assertEquals("size()", 0, ix.size());
        assertFalse("has(0)", ix.has(0));
        Index ii = ix.indexes();
        assertFalse("indexes().hasPrevious()",ii.hasPrevious());
        assertFalse("indexes().hasNext()", ii.hasNext());
        try {
            ii.current();
            fail("indexes().current()");
        }
        catch(Index.InvalidIndexException e) {
            // success
        }
        try {
            ii.previous();
            fail("indexes().previous()");
        }
        catch(Index.InvalidIndexException e) {
            // success
        }
        try {
            ii.next();
            fail("indexes().next()");
        }
        catch(Index.InvalidIndexException e) {
            // success
        }
    }

    public void testSingleton() throws Exception {
        int index=333;
        Indexable ix = new Indexable.Singleton(index);
        assertFalse("isEmpty()",ix.isEmpty());
        assertEquals("size()", 1, ix.size());
        assertFalse("has(0)", ix.has(0));
        assertTrue("has(index)", ix.has(index));
        Index ii = ix.indexes();
        assertFalse("indexes().hasPrevious()",ii.hasPrevious());
        assertTrue("indexes().hasNext()", ii.hasNext());
        assertEquals("indexes().next()==index",index,ii.next());
        assertEquals("indexes().current()==index",index,ii.current());
        assertTrue("indexes().hasPrevious()",ii.hasPrevious());
        assertFalse("indexes().hasNext()", ii.hasNext());
    }

    public void testRange() throws Exception {
        int min0=4, max0=4;
        Indexable ix0 = new Indexable.Range(min0, max0);
        assertFalse("isEmpty()", ix0.isEmpty());
        assertEquals("size()", max0-min0+1, ix0.size());
        assertFalse("has(0)", ix0.has(0));
        assertTrue("has(min)", ix0.has(min0));
        assertTrue("has(max)", ix0.has(max0));
        Index ii0 = ix0.indexes();
        assertFalse("indexes().hasPrevious()", ii0.hasPrevious());
        assertTrue("indexes().hasNext()", ii0.hasNext());
        assertEquals("indexes().next()==min",min0,ii0.next());
        assertEquals("indexes().current()==min", min0, ii0.current());
        assertTrue("indexes().hasPrevious()",ii0.hasPrevious());
        assertFalse("indexes().hasNext()", ii0.hasNext());
        int min1=4, max1=5;
        Indexable ix1 = new Indexable.Range(min1, max1);
        assertFalse("isEmpty()", ix1.isEmpty());
        assertEquals("size()", max1-min1+1, ix1.size());
        assertFalse("has(0)", ix1.has(0));
        assertTrue("has(min)", ix1.has(min1));
        assertTrue("has(max)", ix1.has(max1));
        Index ii1 = ix1.indexes();
        assertFalse("indexes().hasPrevious()", ii1.hasPrevious());
        assertTrue("indexes().hasNext()", ii1.hasNext());
        assertEquals("indexes().next()==min",min1,ii1.next());
        assertEquals("indexes().current()==min", min1, ii1.current());
        assertEquals("indexes().next()==..",min1+1,ii1.next());
        assertEquals("indexes().current()==..", min1+1, ii1.current());
        assertTrue("indexes().hasPrevious()",ii1.hasPrevious());
        assertFalse("indexes().hasNext()", ii1.hasNext());
        int min2=4, max2=6;
        Indexable ix2 = new Indexable.Range(min2, max2);
        assertFalse("isEmpty()", ix2.isEmpty());
        assertEquals("size()", max2-min2+1, ix2.size());
        assertFalse("has(0)", ix2.has(0));
        assertTrue("has(min)", ix2.has(min2));
        assertTrue("has(max)", ix2.has(max2));
        Index ii2 = ix2.indexes();
        assertFalse("indexes().hasPrevious()", ii2.hasPrevious());
        assertTrue("indexes().hasNext()", ii2.hasNext());
        assertEquals("indexes().next()==min",min2,ii2.next());
        assertEquals("indexes().current()==min", min2, ii2.current());
        assertEquals("indexes().next()==..",min2+1,ii2.next());
        assertEquals("indexes().current()==..", min2+1, ii2.current());
        assertEquals("indexes().next()==..",min2+2,ii2.next());
        assertEquals("indexes().current()==..", min2+2, ii2.current());
        assertTrue("indexes().hasPrevious()",ii2.hasPrevious());
        assertFalse("indexes().hasNext()", ii2.hasNext());
    }

    public void testEnumerate() throws Exception {
        long[] idx0 = new long[] {};
        Indexable ix0 = new Indexable.Enumerate(idx0);
        assertTrue("isEmpty()", ix0.isEmpty());
        assertEquals("size()", idx0.length, ix0.size());
        assertFalse("has(0)", ix0.has(0));
        Index ii0 = ix0.indexes();
        assertFalse("indexes().hasPrevious()", ii0.hasPrevious());
        assertFalse("indexes().hasNext()", ii0.hasNext());
        long[] idx1 = new long[] {4};
        Indexable ix1 = new Indexable.Enumerate(idx1);
        assertFalse("isEmpty()", ix1.isEmpty());
        assertEquals("size()", idx1.length, ix1.size());
        assertFalse("has(0)", ix1.has(0));
        assertTrue("has(min)", ix1.has(idx1[0]));
        assertTrue("has(max)", ix1.has(idx1[idx1.length-1]));
        Index ii1 = ix1.indexes();
        assertFalse("indexes().hasPrevious()", ii1.hasPrevious());
        assertTrue("indexes().hasNext()", ii1.hasNext());
        assertEquals("indexes().next()==min",idx1[0],ii1.next());
        assertEquals("indexes().current()==min", idx1[0], ii1.current());
        assertFalse("indexes().hasPrevious()", ii1.hasPrevious());
        assertFalse("indexes().hasNext()", ii1.hasNext());
        long[] idx2 = new long[] {4, 5, 6};
        Indexable ix2 = new Indexable.Enumerate(idx2);
        assertFalse("isEmpty()", ix2.isEmpty());
        assertEquals("size()", idx2.length, ix2.size());
        assertFalse("has(0)", ix2.has(0));
        assertTrue("has(min)", ix2.has(idx2[0]));
        assertTrue("has(max)", ix2.has(idx2[idx2.length-1]));
        Index ii2 = ix2.indexes();
        assertFalse("indexes().hasPrevious()", ii2.hasPrevious());
        assertTrue("indexes().hasNext()", ii2.hasNext());
        assertEquals("indexes().next()==min",idx2[0],ii2.next());
        assertEquals("indexes().current()==min", idx2[0], ii2.current());
        assertEquals("indexes().next()==..",idx2[1],ii2.next());
        assertEquals("indexes().current()==..", idx2[1], ii2.current());
        assertEquals("indexes().next()==..",idx2[2],ii2.next());
        assertEquals("indexes().current()==..", idx2[2], ii2.current());
        assertTrue("indexes().hasPrevious()",ii2.hasPrevious());
        assertFalse("indexes().hasNext()", ii2.hasNext());
    }

    public void testSequence() throws Exception {
        long[] idx1 = new long[] {4, 5, 6 };
        Indexable ix1 = new Indexable.Enumerate(idx1);
        long[] idx2 = new long[] {17, 18};
        Indexable ix2 = new Indexable.Enumerate(idx2);
        Indexable ix = new Indexable.Sequence(ix1, ix2);
        assertFalse("isEmpty()", ix2.isEmpty());
        assertEquals("size()", idx1.length+idx2.length, ix.size());
        assertFalse("has(0)", ix.has(0));
        assertTrue("has(min1)", ix.has(idx1[0]));
        assertTrue("has(max1)", ix.has(idx1[idx1.length-1]));
        assertTrue("has(min2)", ix.has(idx2[0]));
        assertTrue("has(max2)", ix.has(idx2[idx2.length-1]));
        Index ii = ix.indexes();
        assertFalse("indexes().hasPrevious()", ii.hasPrevious());
        assertTrue("indexes().hasNext()", ii.hasNext());
        assertEquals("indexes().next()==min",idx1[0],ii.next());
        assertEquals("indexes().current()==min", idx1[0], ii.current());
        assertEquals("indexes().next()==...",idx1[1],ii.next());
        assertEquals("indexes().next()==...",idx1[2],ii.next());
        assertEquals("indexes().next()==...",idx2[0],ii.next());
        assertEquals("indexes().next()==...",idx2[1],ii.next());
        assertTrue("indexes().hasPrevious()",ii.hasPrevious());
        assertFalse("indexes().hasNext()", ii.hasNext());
    }

}
