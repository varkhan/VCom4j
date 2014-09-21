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
 * @time 6:20 PM
 */
public enum Region implements Entity {
    WESTERN_EUROPE("WEU", "Western Europe"),
    EASTERN_EUROPE("EEU", "Eastern Europe"),
    SOUTHERN_EUROPE("SEU", "Southern Europe"),
    SOUTH_WEST_EUROPE("SWEU", "South West Europe"),
    NORTHERN_EUROPE("NEU", "Northern Europe"),
    SOUTH_EAST_EUROPE("SEEU", "South East Europe"),
    CENTRAL_EUROPE("CEU", "Central Europe"),

    NORTH_AMERICA("NAM", "North America"),
    WEST_INDIES("WIN", "West Indies"),
    CENTRAL_AMERICA("CAM", "Central America"),
    SOUTH_AMERICA("SAM", "South America"),

    CENTRAL_ASIA("CAS", "Central Asia"),
    EAST_ASIA("EAS", "East Asia"),
    NORTHERN_ASIA("NAS", "Northern Asia"),
    SOUTH_WEST_ASIA("SWAS", "South West Asia"),
    SOUTH_ASIA("SAS", "South Asia"),
    SOUTH_EAST_ASIA("SEA", "South East Asia"),

    NORTHERN_AFRICA("NAF", "Northern Africa"),
    CENTRAL_AFRICA("CAF", "Central Africa"),
    SOUTHERN_AFRICA("SAF", "Southern Africa"),
    EASTERN_AFRICA("EAF", "Eastern Africa"),
    WESTERN_AFRICA("WAF", "Western Africa"),

    NORTH_PACIFIC_OCEAN("NPO", "North Pacific Ocean"),
    PACIFIC_OCEAN("PO", "Pacific Ocean"),
    INDIAN_OCEAN("IO", "Indian Ocean"),
    SOUTHERN_INDIAN_OCEAN("SIO", "Southern Indian Ocean"),
    SOUTH_ATLANTIC_OCEAN("SAO", "South Atlantic Ocean"),
    ANTARCTICA("AN", "Antarctica"),
    ;

    private final String code;
    private final String desc;

    Region(String code, String desc) {
        this.code=code;
        this.desc = desc;
    }

    public Typology typology() { return Typology.Politic; }
    public Division division() { return Division.Region; }
    public String code() { return code; }
    public String desc() { return desc; }

    private static final Map<String,Region> c2v = new HashMap<String,Region>();
    static { for(Region v: Region.values()) c2v.put(v.code().toUpperCase(),v); }
    public static Region forCode(String code) { return c2v.get(code.toUpperCase()); }

    private static final Map<String,Region> n2v = new HashMap<String,Region>();
    static { for(Region v: Region.values()) n2v.put(v.desc().toUpperCase(),v); }
    public static Region forName(String name) { return n2v.get(name.toUpperCase()); }


}
