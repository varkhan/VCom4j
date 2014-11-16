package net.varkhan.core.geo.geometry.plane;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/16/14
 * @time 3:34 PM
 */
public class PolyP2Dc extends PolyP2D {
    protected final Chirality chr;

    public PolyP2Dc(Chirality chr, double[] xpts, double[] ypts) {
        super(xpts, ypts);
        this.chr=chr;
    }

    public PolyP2Dc(Chirality chr, float[] xpts, float[] ypts) {
        super(xpts, ypts);
        this.chr=chr;
    }

    public <P extends Point2D> PolyP2Dc(Chirality chr, P... pts) {
        super(pts);
        this.chr=chr;
    }

    @Override
    public boolean contains(double x, double y) {
        switch(chr) {
            case NONE:
                return (winding(x, y, pts)&1)!=0;
            case DEX:
                return winding(x, y, pts)>0;
            case LEV:
                return winding(x, y, pts)<0;
        }
        return (winding(x, y, pts)&1)!=0;
    }
}
