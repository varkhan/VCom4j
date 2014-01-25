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
public interface Location extends Shape2D {

    public String name();

    public Loctype type();

    public Locclass clas();

}
