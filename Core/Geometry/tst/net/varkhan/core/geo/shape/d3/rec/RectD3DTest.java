package net.varkhan.core.geo.shape.d3.rec;

import junit.framework.TestCase;


public class RectD3DTest extends TestCase {

    public void testContains() throws Exception {
        RectD3D r = new RectD3D(0,1,0,1,0,1);
        assertTrue("contains(0,0,0)",r.contains(0,0,0));
        assertTrue("contains((0,0,0))",r.contains(new PointD3D(0,0,0)));
        assertTrue("contains(1,1,1)",r.contains(1,1,1));
        assertTrue("contains((1,1,1))",r.contains(new PointD3D(1,1,1)));
        assertTrue("contains(0,1,0)",r.contains(0,1,0));
        assertTrue("contains((0,1,0))",r.contains(new PointD3D(0,1,0)));
        assertTrue("contains(.5,.5,.5)",r.contains(.5,.5,.5));
        assertTrue("contains((.5,.5,.5))",r.contains(new PointD3D(.5,.5,.5)));
        assertFalse("!contains(0,-1,0)",r.contains(0,-1,0));
        assertFalse("!contains((0,-1,0))",r.contains(new PointD3D(0,-1,0)));
        assertFalse("!contains(-1,0,0)",r.contains(-1,0,0));
        assertFalse("!contains((-1,0,0))",r.contains(new PointD3D(-1,0,0)));
        assertFalse("!contains(1,-1,1)",r.contains(1,-1,1));
        assertFalse("!contains((1,-1,1))",r.contains(new PointD3D(1,-1,1)));
        assertFalse("!contains(-1,1,.5)",r.contains(-1,1,.5));
        assertFalse("!contains((-1,1,.5))",r.contains(new PointD3D(-1,1,.5)));
    }

    public void testRad2() throws Exception {
        RectD3D r = new RectD3D(0,1,0,1,0,1);
        assertEquals("rad2",3.0/4.0,r.rad2());
    }

    public void testArea() throws Exception {
        RectD3D r = new RectD3D(0,1,0,1,0,1);
        assertEquals("area",6.0,r.area());
    }

    public void testVolume() throws Exception {
        RectD3D r = new RectD3D(0,1,0,1,0,1);
        assertEquals("volume",1.0,r.volume());
    }
}