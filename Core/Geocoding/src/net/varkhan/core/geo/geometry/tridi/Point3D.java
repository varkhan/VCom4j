package net.varkhan.core.geo.geometry.tridi;

import net.varkhan.core.geo.geometry.Point;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:34 PM
 */
public interface Point3D extends Shape3D, Point {

    public double xctr();
    public double yctr();
    public double zctr();

}
