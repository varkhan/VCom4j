package net.varkhan.core.geo.geometry.d2.rec;

import junit.framework.TestCase;
import net.varkhan.core.geo.geometry.d2.Point2D;

import static net.varkhan.core.geo.geometry.d2.rec.PolyD2D.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/23/12
 * @time 6:01 PM
 */
public class PolyD2DTest extends TestCase {

    public void testDmin2() throws Exception {
        PolyD2D poly1 = new PolyD2D(
                new PointD2D ( -108.65110778808594 , 76.81360054016113 ),
                new PointD2D ( -108.55803680419922 , 76.40860176086426 ),
                new PointD2D ( -108.07749938964844 , 76.28055000305176 ),
                new PointD2D ( -108.3961181640625  , 76.04609870910645 ),
                new PointD2D ( -107.63249206542969 , 75.99109077453613 ),
                new PointD2D ( -108.02027893066406 , 75.78082466125488 ),
                new PointD2D ( -106.89666557312012 , 75.72026252746582 ),
                new PointD2D ( -106.336669921875   , 76.0547046661377  ),
                new PointD2D ( -105.39195251464844 , 75.63888740539551 ),
                new PointD2D ( -106.01112365722656 , 75.05081367492676 ),
                new PointD2D ( -108.8324966430664  , 75.06999397277832 ),
                new PointD2D ( -112.75306701660156 , 74.4013843536377  ),
                new PointD2D ( -114.44776916503906 , 74.6746997833252  ),
                new PointD2D ( -110.91278076171875 , 75.23387336730957 ),
                new PointD2D ( -113.9175033569336  , 75.05359077453613 ),
                new PointD2D ( -113.34056091308594 , 75.41331672668457 ),
                new PointD2D ( -114.06500244140625 , 75.46609687805176 ),
                new PointD2D ( -115.05082702636719 , 74.96110725402832 ),
                new PointD2D ( -117.68388366699219 , 75.25305366516113 ),
                new PointD2D ( -114.9997329711914  , 75.6908130645752  ),
                new PointD2D ( -117.25110626220703 , 75.59749031066895 ),
                new PointD2D ( -114.81749725341797 , 75.88081550598145 ),
                new PointD2D ( -116.73416137695312 , 75.92248725891113 ),
                new PointD2D ( -116.29611206054688 , 76.18858528137207 ),
                new PointD2D ( -114.66251373291016 , 76.1605396270752  ),
                new PointD2D ( -115.92500305175781 , 76.28665351867676 ),
                new PointD2D ( -114.89972686767578 , 76.51693916320801 ),
                new PointD2D ( -112.45388793945312 , 76.17637825012207 ),
                new PointD2D ( -111.727783203125   , 75.9216480255127  ),
                new PointD2D ( -112.2255630493164  , 75.81109809875488 ),
                new PointD2D ( -111.45195007324219 , 75.83665657043457 ),
                new PointD2D ( -111.24722290039062 , 75.51805305480957 ),
                new PointD2D ( -108.8994369506836  , 75.47638130187988 ),
                new PointD2D ( -108.82695007324219 , 75.68664741516113 ),
                new PointD2D ( -110.05555725097656 , 75.89055061340332 ),
                new PointD2D ( -109.31360626220703 , 76.1091480255127  ),
                new PointD2D ( -110.39306640625    , 76.39193916320801 ),
                new PointD2D ( -108.65110778808594 , 76.81360054016113 )
        );
        double dmin2 = Double.MAX_VALUE;
        double x =-121.911392, y=37.340684;
        for(Point2D pt: poly1) {
            double d = pt.dmin2(x,y);
            if(dmin2>d) dmin2=d;
        }
        RectD2D bbox=new RectD2D(poly1);
        double bbmd2 = bbox.dmin2(x, y);
        assertFalse("! in bbox "+bbox, bbox.contains(x, y));
        assertTrue("dmin2>"+bbmd2, bbmd2<=poly1.dmin2(x, y));
        assertEquals("dmin2",dmin2,poly1.dmin2(x,y),0.1);
        PolyD2D poly2 = new PolyD2D(
                new PointD2D ( 18.20610800000003,  56.91194200000011  ),
                new PointD2D ( 18.11916400000007,  57.531105000000096 ),
                new PointD2D ( 19.004719000000136, 57.908607          ),
                new PointD2D ( 18.20610800000003,  56.91194200000011  )
        );
        dmin2 = Double.MAX_VALUE;
        for(Point2D pt: poly2) {
            double d = pt.dmin2(x,y);
            if(dmin2>d) dmin2=d;
        }
        bbox=new RectD2D(poly2);
        bbmd2 = bbox.dmin2(x, y);
        assertFalse("! in bbox "+bbox, bbox.contains(x, y));
        assertTrue("dmin2 "+poly2.dmin2(x, y)+">="+bbmd2, bbmd2<=poly2.dmin2(x, y));
        assertEquals("dmin2",dmin2,poly2.dmin2(x,y),0.1);
    }


    public void testAngle() throws Exception {
        assertEquals("angle((2,2 4,2 4,4))",Math.PI/4,angle(2,2,4,2,4,4),1e-15);
        assertEquals("angle((1,1 5,4 2,8))",Math.PI/4,angle(1,1,5,4,2,8),1e-15);
    }

    public void testArea() throws Exception {
        assertEquals("area((2,2 4,2 4,4))",2.0,area(2,2,4,2,4,4));
        assertEquals("area((2,2 4,2 4,5))",3.0,area(2,2,4,2,4,5));
        assertEquals("area((1,1 5,4 2,8))",12.5,area(1,1,5,4,2,8));
    }

    public void testDist() throws Exception {
//        assertEquals("dist((2,2 4,2 4,4))",2.0,dist(2,2,4,2,4,4));
//        assertEquals("dist((2,2 4,2 4,5))",2.0,dist(2,2,4,2,4,5));
//        assertEquals("dist((1,1 5,4 2,8))",5.0,dist(1,1,5,4,2,8));
//        assertEquals("dist((1,1 3.5,6 2,8))",Math.sqrt(5*5+2.5*2.5),dist(1,1,3.5,6,2,8));
//        assertEquals("dist((1,1 6.5,2 2,8))",5.0,dist(1,1,6.5,2,2,8));
//        assertEquals("dist((1,1 8,0 6.5,2))",Math.sqrt(5*5+2.5*2.5),dist(1,1,8,0,6.5,2));
//        assertEquals("dist((1,1 6.5,2 8,0))",-Math.sqrt(5*5+2.5*2.5),dist(1,1,6.5,2,8,0));
        assertEquals("0,0 loz", 0.5, distmin2(0, 0, 4,
                                              new double[] { 0, 1, 0, -1 },
                                              new double[] { 1, 0, -1, 0 }
                                             ));
        assertEquals("1,1 loz", 0.5, distmin2(1, 1, 4,
                                              new double[] { 1, 0, -1, 0 },
                                              new double[] { 0, 1, 0, -1 }
                                             ));
        assertEquals("0,0 rec", 1.0, distmin2(0, 0, 4,
                                              new double[] { 1, -1, -1, 1 },
                                              new double[] { 1, 1, -1, -1 }
                                             ));
        assertEquals("-2,0 rec", 1.0, distmin2(-2, 0, 4,
                                               new double[] { 1, -1, -1, 1 },
                                               new double[] { 1, 1, -1, -1 }
                                              ));
        assertEquals("-0.2,0 pol", 0.02, distmin2(-0.2, 0, 7,
                                                new double[] { 1, 0.2, -0.3, -0.1, -1,  0, -0.1 },
                                                new double[] { 0, 1.3,  0.9,  0.2,  0, -1, -0.1 }
                                               ),0.0001);
        assertEquals("-0.5,-0.5 pol", 0.0, distmin2(-0.5, -0.5, 7,
                                              new double[] { 1, 0.2, -0.3, -0.1, -1,  0, -0.1 },
                                              new double[] { 0, 1.3,  0.9,  0.2,  0, -1, -0.1 }
                                             ));
        assertEquals("-1,0 pol", 0.5, distmin2(-1, -1, 7,
                                                new double[] { 1, 0.2, -0.3, -0.1, -1,  0, -0.1 },
                                                new double[] { 0, 1.3,  0.9,  0.2,  0, -1, -0.1 }
                                               ));
    }

    public void testWinding() throws Exception {
        assertEquals("0,0 loz", -1, winding(0, 0, 4,
                                           new double[]{0, 1, 0, -1},
                                           new double[]{1, 0, -1, 0 }
                                          ));
        assertEquals("0,0 loz x", 1, winding(0, 0, 4,
                                           new double[]{0, -1, 0, 1},
                                           new double[]{1, 0, -1, 0 }
                                          ));
        assertEquals("1,1 loz", 0, winding(1, 1, 4,
                                           new double[]{1, 0, -1, 0},
                                           new double[]{0, 1, 0, -1}
                                          ));
        assertEquals("0,0 rec", 1, winding(0, 0, 4,
                                           new double[]{1, -1, -1, 1},
                                           new double[]{1, 1, -1, -1}
                                          ));
        assertEquals("-2,0 rec", 0, winding(-2, 0, 4,
                                            new double[]{1, -1, -1, 1},
                                            new double[]{1, 1, -1, -1}
                                           ));
        assertEquals("0,0 pol", 1, winding(0, 0, 6,
                                           new double[]{1, 0.2, -0.3, -0.1, -1, -0.1},
                                           new double[]{0, 1.3,  0.9,  0.2, -1, -0.1}
                                          ));
        assertEquals("0,0 pol", 1, winding(-0.990, -0.988, 6,
                                           new double[]{1, 0.2, -0.3, -0.1, -1, -0.1},
                                           new double[]{0, 1.3,  0.9,  0.2, -1, -0.1}
                                          ));
        assertEquals("0,0 pol", 0, winding(-1, -1, 6,
                                           new double[]{1, 0.2, -0.3, -0.1, -1, -0.1},
                                           new double[]{0, 1.3,  0.9,  0.2, -1, -0.1}
                                          ));
        assertEquals("-1,0 pol", 0, winding(-1, 0, 6,
                                            new double[]{1, 0.2, -0.3, -0.1, -1, -0.1},
                                            new double[]{0, 1.3,  0.9,  0.2, -1, -0.1}
                                           ));
    }

}
