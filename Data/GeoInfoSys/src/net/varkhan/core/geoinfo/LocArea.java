package net.varkhan.core.geoinfo;

import net.varkhan.core.geometry.plane.Shape2D;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/11/12
 * @time 4:07 PM
 */
public interface LocArea extends Shape2D {

    public String name();

    public LocType type();

    public LocRank rank();

}
