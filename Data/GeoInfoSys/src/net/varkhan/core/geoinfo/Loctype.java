package net.varkhan.core.geoinfo;

import net.varkhan.core.geocode.features.Typology;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/11/12
 * @time 4:12 PM
 */
public enum Loctype {
    A(Typology.Politic, "country","state","region"),
    H(Typology.Geology, "stream","lake"),
    L(Typology.Ecology, "parks","area"),
    P(Typology.Demography, "city","village"),
    R(Typology.Communication, "road","railroad"),
    S(Typology.Culture, "spot","building"),
    T(Typology.Geology, "mountain","hill","rock"),
    U(Typology.Geology, "undersea"),
    V(Typology.Ecology, "forest","heath"),
    ;

    private final Typology type;
    private final String[] desc;

    Loctype(Typology type, String... desc) {
        this.type=type;
        this.desc=desc;
    }

    public Typology type() {
        return type;
    }

    public String[] desc() {
        return desc;
    }

}
