package net.varkhan.core.geo.shape;


/**
 * <b>A rectangular (hypercube) shape</b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 4:35 PM
 */
public interface Rect extends Shape {

    public double[] cmin();
    public double[] cmax();

}
