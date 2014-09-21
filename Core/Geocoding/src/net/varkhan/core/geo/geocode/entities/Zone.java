package net.varkhan.core.geo.geocode.entities;

import net.varkhan.core.geo.geocode.features.Division;
import net.varkhan.core.geo.geocode.features.Entity;
import net.varkhan.core.geo.geocode.features.Typology;

import java.util.HashMap;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 6:05 PM
 */
public enum Zone implements Entity {
    AF("AF","Africa"),
    AS("AS","Asia"),
    AU("AU","Australia"),
    EU("EU","Europe"),
    NA("NA","North America"),
    SA("SA","South America"),
    AN("AN","Antarctica"),
    AO("AO","Atlantic Ocean"),
    BO("BO","Arctic Ocean"),
    IO("IO","Indian Ocean"),
    OC("OC","Oceania"),
    ;
    private final String code;
    private final String desc;

    Zone(String code, String desc) {
        this.code=code;
        this.desc=desc;
    }

    public Typology typology() { return Typology.Geology; }
    public Division division() { return Division.Zone; }
    public String code() { return code; }
    public String desc() { return desc; }

    private static final Map<String,Zone> c2v = new HashMap<String,Zone>();
    static { for(Zone v: Zone.values()) c2v.put(v.code().toUpperCase(),v); }
    public static Zone forCode(String code) { return c2v.get(code.toUpperCase()); }

    private static final Map<String,Zone> n2v = new HashMap<String,Zone>();
    static { for(Zone v: Zone.values()) n2v.put(v.desc().toUpperCase(),v); }
    public static Zone forName(String name) { return n2v.get(name.toUpperCase()); }

}
