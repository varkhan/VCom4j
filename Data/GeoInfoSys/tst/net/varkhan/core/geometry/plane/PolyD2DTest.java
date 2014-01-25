package net.varkhan.core.geometry.plane;

import junit.framework.TestCase;

import static net.varkhan.core.geometry.plane.PolyD2D.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/23/12
 * @time 6:01 PM
 */
public class PolyD2DTest extends TestCase {

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
        assertEquals("0,0 pol", 0.005, distmin2(-0.1, 0, 6,
                                                new double[] { 1, 0.2, -0.3, -0.1, -1, -0.1 },
                                                new double[] { 0, 1.3, 0.9, 0.2, -1, -0.1 }
                                               ),0.0001);
        assertEquals("0,0 pol", 0.0, distmin2(-1, -1, 6,
                                              new double[] { 1, 0.2, -0.3, -0.1, -1, -0.1 },
                                              new double[] { 0, 1.3, 0.9, 0.2, -1, -0.1 }
                                             ));
        assertEquals("-1,0 pol", 0.25, distmin2(0, -1, 6,
                                                new double[] { 1, 0.2, -0.3, -0.1, -1, -0.1 },
                                                new double[] { 0, 1.3, 0.9, 0.2, -1, -0.1 }
                                               ));
    }

    public void testContains() throws Exception {
        assertEquals("0,0 loz", -1, winding(0, 0, 4,
                                           new double[]{0, 1, 0, -1},
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
