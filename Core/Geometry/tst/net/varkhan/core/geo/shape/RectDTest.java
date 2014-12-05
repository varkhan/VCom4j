package net.varkhan.core.geo.shape;

import junit.framework.TestCase;


public class RectDTest extends TestCase {

    public void testContains() throws Exception {
        RectD r = new RectD(new PointD(0,0,0,0),new PointD(1,1,1,1));
        assertTrue("contains(0,0,0,0)",r.contains(0,0,0,0));
        assertTrue("contains((0,0,0,0))",r.contains(new PointD(0,0,0,0)));
        assertTrue("contains(1,1,1,1)",r.contains(1,1,1,1));
        assertTrue("contains((1,1,1,1))",r.contains(new PointD(1,1,1,1)));
        assertTrue("contains(0,1,0,1)",r.contains(0,1,0,1));
        assertTrue("contains((0,1,0,1))",r.contains(new PointD(0,1,0,1)));
        assertTrue("contains(.5,.5,.5,.5)",r.contains(.5,.5,.5,.5));
        assertTrue("contains((.5,.5,.5,.5))",r.contains(new PointD(.5,.5,.5,.5)));
        assertFalse("!contains(0,-1,0,1)",r.contains(0,-1,0,1));
        assertFalse("!contains((0,-1,0,1))",r.contains(new PointD(0,-1,0,1)));
        assertFalse("!contains(-1,0,0,0)",r.contains(-1,0,0,0));
        assertFalse("!contains((-1,0,0,0))",r.contains(new PointD(-1,0,0,0)));
        assertFalse("!contains(1,-1,1,1)",r.contains(1,-1,1,1));
        assertFalse("!contains((1,-1,1,1))",r.contains(new PointD(1,-1,1,1)));
        assertFalse("!contains(-1,1,.5,.5)",r.contains(-1,1,.5,.5));
        assertFalse("!contains((-1,1,.5,.5))",r.contains(new PointD(-1,1,.5,.5)));
    }

    public void testRad2() throws Exception {
        RectD r = new RectD(new PointD(0,0,0,0),new PointD(1,1,1,1));
        assertEquals("rad2",4.0/4.0,r.rad2());
    }

    public void testMsr() throws Exception {
        RectD r = new RectD(new PointD(0,0,0,0),new PointD(1,1,1,1));
        assertEquals("area",8.0,r.msr(3));
        assertEquals("volume",1.0,r.msr(4));
        assertEquals("volume",1.0,r.msr());
    }

}