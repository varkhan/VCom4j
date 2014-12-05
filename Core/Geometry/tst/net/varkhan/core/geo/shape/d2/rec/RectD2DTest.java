package net.varkhan.core.geo.shape.d2.rec;

import junit.framework.TestCase;


public class RectD2DTest extends TestCase {

    public void testContains() throws Exception {
        RectD2D r = new RectD2D(0,1,0,1);
        assertTrue("contains(0,0)",r.contains(0,0));
        assertTrue("contains((0,0))",r.contains(new PointD2D(0,0)));
        assertTrue("contains(1,1)",r.contains(1,1));
        assertTrue("contains((1,1))",r.contains(new PointD2D(1,1)));
        assertTrue("contains(0,1)",r.contains(0,1));
        assertTrue("contains((0,1))",r.contains(new PointD2D(0,1)));
        assertTrue("contains(.5,.5)",r.contains(.5,.5));
        assertTrue("contains((.5,.5))",r.contains(new PointD2D(.5,.5)));
        assertFalse("!contains(0,-1)",r.contains(0,-1));
        assertFalse("!contains((0,-1))",r.contains(new PointD2D(0,-1)));
        assertFalse("!contains(-1,0)",r.contains(-1,0));
        assertFalse("!contains((-1,0))",r.contains(new PointD2D(-1,0)));
        assertFalse("!contains(1,-1)",r.contains(1,-1));
        assertFalse("!contains((1,-1))",r.contains(new PointD2D(1,-1)));
        assertFalse("!contains(-1,1)",r.contains(-1,1));
        assertFalse("!contains((-1,1))",r.contains(new PointD2D(-1,1)));
    }

    public void testRad2() throws Exception {
        RectD2D r = new RectD2D(0,1,0,1);
        assertEquals("rad2",0.5,r.rad2());
    }

    public void testLength() throws Exception {
        RectD2D r = new RectD2D(0,1,0,1);
        assertEquals("length",4.0,r.length());
    }

    public void testArea() throws Exception {
        RectD2D r = new RectD2D(0,1,0,1);
        assertEquals("area",1.0,r.area());
    }

}