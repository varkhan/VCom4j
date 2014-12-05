package net.varkhan.core.geo.shape.d2.rec;

import junit.framework.TestCase;


public class RectF2DTest extends TestCase {

    public void testContains() throws Exception {
        RectF2D r = new RectF2D(0,1,0,1);
        assertTrue("contains(0,0)",r.contains(0,0));
        assertTrue("contains((0,0))",r.contains(new PointF2D(0,0)));
        assertTrue("contains(1,1)",r.contains(1,1));
        assertTrue("contains((1,1))",r.contains(new PointF2D(1,1)));
        assertTrue("contains(0,1)",r.contains(0,1));
        assertTrue("contains((0,1))",r.contains(new PointF2D(0,1)));
        assertTrue("contains(.5,.5)", r.contains(.5, .5));
        assertTrue("contains((.5,.5))", r.contains(new PointF2D(.5, .5)));
        assertFalse("!contains(0,-1)", r.contains(0, -1));
        assertFalse("!contains((0,-1))", r.contains(new PointF2D(0, -1)));
        assertFalse("!contains(-1,0)", r.contains(-1, 0));
        assertFalse("!contains((-1,0))", r.contains(new PointF2D(-1, 0)));
        assertFalse("!contains(1,-1)", r.contains(1, -1));
        assertFalse("!contains((1,-1))", r.contains(new PointF2D(1, -1)));
        assertFalse("!contains(-1,1)", r.contains(-1, 1));
        assertFalse("!contains((-1,1))", r.contains(new PointF2D(-1, 1)));
    }

    public void testRad2() throws Exception {
        RectF2D r = new RectF2D(0,1,0,1);
        assertEquals("rad2",0.5,r.rad2());
    }

    public void testLength() throws Exception {
        RectF2D r = new RectF2D(0,1,0,1);
        assertEquals("length",4.0,r.length());
    }

    public void testArea() throws Exception {
        RectF2D r = new RectF2D(0,1,0,1);
        assertEquals("area",1.0,r.area());
    }

    public void testDMin() throws Exception {
        RectF2D r = new RectF2D(new PointF2D(0.0,0),new PointF2D(1.0,1));
        assertEquals("dmin(0,0)",0.0,r.dmin2(0,0));
        assertEquals("dmin(-1,0)",1.0,r.dmin2(-1,0));
        assertEquals("dmin(0,-1)",1.0,r.dmin2(0,-1));
        assertEquals("dmin(2,0)",1.0,r.dmin2(2,0));
        assertEquals("dmin(0,2)",1.0,r.dmin2(0,2));
        assertEquals("dmin(.5,.5)",0.0,r.dmin2(.5,.5));
        assertEquals("dmin(-1,.5)",1.0,r.dmin2(-1,.5));
        assertEquals("dmin(.5,-1)",1.0,r.dmin2(.5,-1));
        assertEquals("dmin(2,.5)",1.0,r.dmin2(2,.5));
        assertEquals("dmin(.5,2)",1.0,r.dmin2(.5,2));
    }

    public void testDMax() throws Exception {
        RectF2D r = new RectF2D(new PointF2D(0.0,0),new PointF2D(1.0,1));
        assertEquals("dmax(0,0)",2.0,r.dmax2(0,0));
        assertEquals("dmax(-1,0)",5.0,r.dmax2(-1,0));
        assertEquals("dmax(0,-1)",5.0,r.dmax2(0,-1));
        assertEquals("dmax(2,0)",5.0,r.dmax2(2,0));
        assertEquals("dmax(0,2)",5.0,r.dmax2(0,2));
        assertEquals("dmax(.5,.5)",0.5,r.dmax2(.5,.5));
        assertEquals("dmax(-1,.5)",4.25,r.dmax2(-1,.5));
        assertEquals("dmax(.5,-1)",4.25,r.dmax2(.5,-1));
        assertEquals("dmax(2,.5)",4.25,r.dmax2(2,.5));
        assertEquals("dmax(.5,2)",4.25,r.dmax2(.5,2));
    }

}