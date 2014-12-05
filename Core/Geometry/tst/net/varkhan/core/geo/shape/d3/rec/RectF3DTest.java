package net.varkhan.core.geo.shape.d3.rec;

import junit.framework.TestCase;


public class RectF3DTest extends TestCase {

    public void testContains() throws Exception {
        RectF3D r = new RectF3D(0,1,0,1,0,1);
        assertTrue("contains(0,0,0)",r.contains(0,0,0));
        assertTrue("contains((0,0,0))",r.contains(new PointF3D(0,0,0)));
        assertTrue("contains(1,1,1)",r.contains(1,1,1));
        assertTrue("contains((1,1,1))",r.contains(new PointF3D(1,1,1)));
        assertTrue("contains(0,1,0)",r.contains(0,1,0));
        assertTrue("contains((0,1,0))",r.contains(new PointF3D(0,1,0)));
        assertTrue("contains(.5,.5,.5)",r.contains(.5,.5,.5));
        assertTrue("contains((.5,.5,.5))",r.contains(new PointF3D(.5,.5,.5)));
        assertFalse("!contains(0,-1,0)",r.contains(0,-1,0));
        assertFalse("!contains((0,-1,0))",r.contains(new PointF3D(0,-1,0)));
        assertFalse("!contains(-1,0,0)",r.contains(-1,0,0));
        assertFalse("!contains((-1,0,0))",r.contains(new PointF3D(-1,0,0)));
        assertFalse("!contains(1,-1,1)",r.contains(1,-1,1));
        assertFalse("!contains((1,-1,1))",r.contains(new PointF3D(1,-1,1)));
        assertFalse("!contains(-1,1,.5)",r.contains(-1,1,.5));
        assertFalse("!contains((-1,1,.5))",r.contains(new PointF3D(-1,1,.5)));
    }

    public void testRad2() throws Exception {
        RectF3D r = new RectF3D(0,1,0,1,0,1);
        assertEquals("rad2",3.0/4.0,r.rad2());
    }

    public void testArea() throws Exception {
        RectF3D r = new RectF3D(0,1,0,1,0,1);
        assertEquals("area",6.0,r.area());
    }

    public void testVolume() throws Exception {
        RectF3D r = new RectF3D(0,1,0,1,0,1);
        assertEquals("volume",1.0,r.volume());
    }

    public void testDMin() throws Exception {
        RectF3D r = new RectF3D(new PointF3D(0.0,0,0),new PointF3D(1.0,1,1));
        assertEquals("dmin(0,0,0)",0.0,r.dmin2(0,0,0));
        assertEquals("dmin(-1,0,0)",1.0,r.dmin2(-1,0,0));
        assertEquals("dmin(0,-1,0)",1.0,r.dmin2(0,-1,0));
        assertEquals("dmin(0,0,-1)",1.0,r.dmin2(0,0,-1));
        assertEquals("dmin(2,0,0)",1.0,r.dmin2(2,0,0));
        assertEquals("dmin(0,2,0)",1.0,r.dmin2(0,2,0));
        assertEquals("dmin(0,0,2)",1.0,r.dmin2(0,0,2));
        assertEquals("dmin(.5,.5,.5)",0.0,r.dmin2(.5,.5,.5));
        assertEquals("dmin(-1,.5,.5)",1.0,r.dmin2(-1,.5,.5));
        assertEquals("dmin(.5,-1,.5)",1.0,r.dmin2(.5,-1,.5));
        assertEquals("dmin(.5,.5,-1)",1.0,r.dmin2(.5,.5,-1));
        assertEquals("dmin(2,.5,.5)",1.0,r.dmin2(2,.5,.5));
        assertEquals("dmin(.5,2,.5)",1.0,r.dmin2(.5,2,.5));
        assertEquals("dmin(.5,.5,2)",1.0,r.dmin2(.5,.5,2));
    }

    public void testDMax() throws Exception {
        RectF3D r = new RectF3D(new PointF3D(0.0,0,0),new PointF3D(1.0,1,1));
        assertEquals("dmax(0,0,0)",3.0,r.dmax2(0,0,0));
        assertEquals("dmax(-1,0,0)",6.0,r.dmax2(-1,0,0));
        assertEquals("dmax(0,-1,0)",6.0,r.dmax2(0,-1,0));
        assertEquals("dmax(0,0,-1)",6.0,r.dmax2(0,0,-1));
        assertEquals("dmax(2,0,0)",6.0,r.dmax2(2,0,0));
        assertEquals("dmax(0,2,0)",6.0,r.dmax2(0,2,0));
        assertEquals("dmax(0,0,2)",6.0,r.dmax2(0,0,2));
        assertEquals("dmax(.5,.5,.5)",0.75,r.dmax2(.5,.5,.5));
        assertEquals("dmax(-1,.5,.5)",4.5,r.dmax2(-1,.5,.5));
        assertEquals("dmax(.5,-1,.5)",4.5,r.dmax2(.5,-1,.5));
        assertEquals("dmax(.5,.5,-1)",4.5,r.dmax2(.5,.5,-1));
        assertEquals("dmax(2,.5,.5)",4.5,r.dmax2(2,.5,.5));
        assertEquals("dmax(.5,2,.5)",4.5,r.dmax2(.5,2,.5));
        assertEquals("dmax(.5,.5,2)",4.5,r.dmax2(.5,.5,2));
    }

}