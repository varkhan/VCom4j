package net.varkhan.core.geo.geometry.d2.rec;

import net.varkhan.core.geo.geometry.d2.Point2D;


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
        int w=winding(x, y, pts);
//        if(w!=0) System.err.println("Contains "+chr+" ("+x+","+y+") "+w+" "+new RectD2D(this)+" "+this);
        switch(chr) {
            case NONE:
                return (w&1)!=0;
            case DEX:
                return w>0;
            case LEV:
                return w<0;
        }
        return (w&1)!=0;
    }
}
