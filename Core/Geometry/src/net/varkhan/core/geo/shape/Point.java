package net.varkhan.core.geo.shape;

/**
 * <b>A single point</b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 1:28 PM
 */
public interface Point extends Shape {

    public int dim();

    public double cctr(int d);
    public double[] cctr();

}
