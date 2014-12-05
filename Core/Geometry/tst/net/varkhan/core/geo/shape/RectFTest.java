package net.varkhan.core.geo.shape;

import junit.framework.TestCase;


public class RectFTest extends TestCase {

    public void testContains() throws Exception {
        RectF r = new RectF(new PointF(0.0,0,0,0),new PointF(1.0,1,1,1));
        assertTrue("contains(0,0,0,0)",r.contains(0,0,0,0));
        assertTrue("contains((0,0,0,0))",r.contains(new PointF(0.0,0,0,0)));
        assertTrue("contains(1,1,1,1)",r.contains(1,1,1,1));
        assertTrue("contains((1,1,1,1))",r.contains(new PointF(1.0,1,1,1)));
        assertTrue("contains(0,1,0,1)",r.contains(0,1,0,1));
        assertTrue("contains((0,1,0,1))",r.contains(new PointF(0.0,1,0,1)));
        assertTrue("contains(.5,.5,.5,.5)",r.contains(.5,.5,.5,.5));
        assertTrue("contains((.5,.5,.5,.5))",r.contains(new PointF(.5,.5,.5,.5)));
        assertFalse("!contains(0,-1,0,1)",r.contains(0,-1,0,1));
        assertFalse("!contains((0,-1,0,1))",r.contains(new PointF(0.0,-1,0,1)));
        assertFalse("!contains(-1,0,0,0)",r.contains(-1,0,0,0));
        assertFalse("!contains((-1,0,0,0))",r.contains(new PointF(-1.0,0,0,0)));
        assertFalse("!contains(1,-1,1,1)",r.contains(1,-1,1,1));
        assertFalse("!contains((1,-1,1,1))",r.contains(new PointF(1.0,-1,1,1)));
        assertFalse("!contains(-1,1,.5,.5)",r.contains(-1,1,.5,.5));
        assertFalse("!contains((-1,1,.5,.5))",r.contains(new PointF(-1.0,1,.5,.5)));
    }

    public void testRad2() throws Exception {
        RectF r = new RectF(new PointF(0.0,0,0,0),new PointF(1.0,1,1,1));
        assertEquals("rad2",4.0/4.0,r.rad2());
    }

    public void testMsr() throws Exception {
        RectF r = new RectF(new PointF(0.0,0,0,0),new PointF(1.0,1,1,1));
        assertEquals("area",8.0,r.msr(3));
        assertEquals("volume",1.0,r.msr(4));
        assertEquals("volume",1.0,r.msr());
    }

    public void testDMin() throws Exception {
        RectF r = new RectF(new PointF(0.0,0,0,0),new PointF(1.0,1,1,1));
        assertEquals("dmin(0,0,0,0)",0.0,r.dmin2(0,0,0,0));
        assertEquals("dmin(-1,0,0,0)",1.0,r.dmin2(-1,0,0,0));
        assertEquals("dmin(0,-1,0,0)",1.0,r.dmin2(0,-1,0,0));
        assertEquals("dmin(0,0,-1,0)",1.0,r.dmin2(0,0,-1,0));
        assertEquals("dmin(0,0,0,-1)",1.0,r.dmin2(0,0,0,-1));
        assertEquals("dmin(2,0,0,0)",1.0,r.dmin2(2,0,0,0));
        assertEquals("dmin(0,2,0,0)",1.0,r.dmin2(0,2,0,0));
        assertEquals("dmin(0,0,2,0)",1.0,r.dmin2(0,0,2,0));
        assertEquals("dmin(0,0,0,2)",1.0,r.dmin2(0,0,0,2));
        assertEquals("dmin(.5,.5,.5,.5)",0.0,r.dmin2(.5,.5,.5,.5));
        assertEquals("dmin(-1,.5,.5,.5)",1.0,r.dmin2(-1,.5,.5,.5));
        assertEquals("dmin(.5,-1,.5,.5)",1.0,r.dmin2(.5,-1,.5,.5));
        assertEquals("dmin(.5,.5,-1,.5)",1.0,r.dmin2(.5,.5,-1,.5));
        assertEquals("dmin(.5,.5,.5,-1)",1.0,r.dmin2(.5,.5,.5,-1));
        assertEquals("dmin(2,.5,.5,.5)",1.0,r.dmin2(2,.5,.5,.5));
        assertEquals("dmin(.5,2,.5,.5)",1.0,r.dmin2(.5,2,.5,.5));
        assertEquals("dmin(.5,.5,2,.5)",1.0,r.dmin2(.5,.5,2,.5));
        assertEquals("dmin(.5,.5,.5,2)",1.0,r.dmin2(.5,.5,.5,2));
    }

    public void testDMax() throws Exception {
        RectF r = new RectF(new PointF(0.0,0,0,0),new PointF(1.0,1,1,1));
        assertEquals("dmax(0,0,0,0)",4.0,r.dmax2(0,0,0,0));
        assertEquals("dmax(-1,0,0,0)",7.0,r.dmax2(-1,0,0,0));
        assertEquals("dmax(0,-1,0,0)",7.0,r.dmax2(0,-1,0,0));
        assertEquals("dmax(0,0,-1,0)",7.0,r.dmax2(0,0,-1,0));
        assertEquals("dmax(0,0,0,-1)",7.0,r.dmax2(0,0,0,-1));
        assertEquals("dmax(2,0,0,0)",7.0,r.dmax2(2,0,0,0));
        assertEquals("dmax(0,2,0,0)",7.0,r.dmax2(0,2,0,0));
        assertEquals("dmax(0,0,2,0)",7.0,r.dmax2(0,0,2,0));
        assertEquals("dmax(0,0,0,2)",7.0,r.dmax2(0,0,0,2));
        assertEquals("dmax(.5,.5,.5,.5)",1.0,r.dmax2(.5,.5,.5,.5));
        assertEquals("dmax(-1,.5,.5,.5)",4.75,r.dmax2(-1,.5,.5,.5));
        assertEquals("dmax(.5,-1,.5,.5)",4.75,r.dmax2(.5,-1,.5,.5));
        assertEquals("dmax(.5,.5,-1,.5)",4.75,r.dmax2(.5,.5,-1,.5));
        assertEquals("dmax(.5,.5,.5,-1)",4.75,r.dmax2(.5,.5,.5,-1));
        assertEquals("dmax(2,.5,.5,.5)",4.75,r.dmax2(2,.5,.5,.5));
        assertEquals("dmax(.5,2,.5,.5)",4.75,r.dmax2(.5,2,.5,.5));
        assertEquals("dmax(.5,.5,2,.5)",4.75,r.dmax2(.5,.5,2,.5));
        assertEquals("dmax(.5,.5,.5,2)",4.75,r.dmax2(.5,.5,.5,2));
    }

}