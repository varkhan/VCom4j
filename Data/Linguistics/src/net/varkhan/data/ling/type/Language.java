package net.varkhan.data.ling.type;

import java.util.HashMap;
import java.util.Map;


/**
 * <b>ISO Language Codes</b>.
 * <p/>
 * The ISO 639-1, ISO 639-2 and ISO 639-3 language codes table.
 * Note that this table only lists major languages (or families)
 * present in the ISO 639-1 specification.
 *
 * @author varkhan
 * @date 11/17/13
 * @time 3:35 PM
 */
@SuppressWarnings("UnusedDeclaration")
public enum Language {

    AB("AB", "ABK", "ABK", "Abkhaz"),
    AA("AA", "AAR", "AAR", "Afar"),
    AF("AF", "AFR", "AFR", "Afrikaans"),
    AK("AK", "AKA", "AKA", "Akan"),
    SQ("SQ", "SQI", "ALB", "Albanian"),
    AM("AM", "AMH", "AMH", "Amharic"),
    AR("AR", "ARA", "ARA", "Arabic"),
    AN("AN", "ARG", "ARG", "Aragonese"),
    HY("HY", "HYE", "ARM", "Armenian"),
    AS("AS", "ASM", "ASM", "Assamese"),
    AV("AV", "AVA", "AVA", "Avaric"),
    AE("AE", "AVE", "AVE", "Avestan"),
    AY("AY", "AYM", "AYM", "Aymara"),
    AZ("AZ", "AZE", "AZE", "Azerbaijani"),
    BM("BM", "BAM", "BAM", "Bambara"),
    BA("BA", "BAK", "BAK", "Bashkir"),
    EU("EU", "EUS", "BAQ", "Basque"),
    BE("BE", "BEL", "BEL", "Belarusian"),
    BN("BN", "BEN", "BEN", "Bengali"),
    BH("BH", "BIH", "BIH", "Bihari"),
    BI("BI", "BIS", "BIS", "Bislama"),
    BS("BS", "BOS", "BOS", "Bosnian"),
    BR("BR", "BRE", "BRE", "Breton"),
    BG("BG", "BUL", "BUL", "Bulgarian"),
    MY("MY", "MYA", "BUR", "Burmese"),
    CA("CA", "CAT", "CAT", "Catalan"),
    CH("CH", "CHA", "CHA", "Chamorro"),
    CE("CE", "CHE", "CHE", "Chechen"),
    NY("NY", "NYA", "NYA", "Chichewa"),
    ZH("ZH", "ZHO", "CHI", "Chinese"),
    CV("CV", "CHV", "CHV", "Chuvash"),
    KW("KW", "COR", "COR", "Cornish"),
    CO("CO", "COS", "COS", "Corsican"),
    CR("CR", "CRE", "CRE", "Cree"),
    HR("HR", "HRV", "HRV", "Croatian"),
    CS("CS", "CES", "CZE", "Czech"),
    DA("DA", "DAN", "DAN", "Danish"),
    DV("DV", "DIV", "DIV", "Divehi"),
    NL("NL", "NLD", "DUT", "Dutch"),
    DZ("DZ", "DZO", "DZO", "Dzongkha"),
    EN("EN", "ENG", "ENG", "English"),
    EO("EO", "EPO", "EPO", "Esperanto"),
    ET("ET", "EST", "EST", "Estonian"),
    EE("EE", "EWE", "EWE", "Ewe"),
    FO("FO", "FAO", "FAO", "Faroese"),
    FJ("FJ", "FIJ", "FIJ", "Fijian"),
    FI("FI", "FIN", "FIN", "Finnish"),
    FR("FR", "FRA", "FRE", "French"),
    FF("FF", "FUL", "FUL", "Fula"),
    GL("GL", "GLG", "GLG", "Galician"),
    KA("KA", "KAT", "GEO", "Georgian"),
    DE("DE", "DEU", "GER", "German"),
    EL("EL", "ELL", "GRE", "Greek"),
    GN("GN", "GRN", "GRN", "Guarani"),
    GU("GU", "GUJ", "GUJ", "Gujarati"),
    HT("HT", "HAT", "HAT", "Haitian"),
    HA("HA", "HAU", "HAU", "Hausa"),
    HE("HE", "HEB", "HEB", "Hebrew"),
    HZ("HZ", "HER", "HER", "Herero"),
    HI("HI", "HIN", "HIN", "Hindi"),
    HO("HO", "HMO", "HMO", "HiriMotu"),
    HU("HU", "HUN", "HUN", "Hungarian"),
    IA("IA", "INA", "INA", "Interlingua"),
    ID("ID", "IND", "IND", "Indonesian"),
    IE("IE", "ILE", "ILE", "Interlingue"),
    GA("GA", "GLE", "GLE", "Irish"),
    IG("IG", "IBO", "IBO", "Igbo"),
    IK("IK", "IPK", "IPK", "Inupiaq"),
    IO("IO", "IDO", "IDO", "Ido"),
    IS("IS", "ISL", "ICE", "Icelandic"),
    IT("IT", "ITA", "ITA", "Italian"),
    IU("IU", "IKU", "IKU", "Inuktitut"),
    JA("JA", "JPN", "JPN", "Japanese"),
    JV("JV", "JAV", "JAV", "Javanese"),
    KL("KL", "KAL", "KAL", "Kalaallisut"),
    KN("KN", "KAN", "KAN", "Kannada"),
    KR("KR", "KAU", "KAU", "Kanuri"),
    KS("KS", "KAS", "KAS", "Kashmiri"),
    KK("KK", "KAZ", "KAZ", "Kazakh"),
    KM("KM", "KHM", "KHM", "Khmer"),
    KI("KI", "KIK", "KIK", "Kikuyu"),
    RW("RW", "KIN", "KIN", "Kinyarwanda"),
    KY("KY", "KIR", "KIR", "Kyrgyz"),
    KV("KV", "KOM", "KOM", "Komi"),
    KG("KG", "KON", "KON", "Kongo"),
    KO("KO", "KOR", "KOR", "Korean"),
    KU("KU", "KUR", "KUR", "Kurdish"),
    KJ("KJ", "KUA", "KUA", "Kwanyama"),
    LA("LA", "LAT", "LAT", "Latin"),
    LB("LB", "LTZ", "LTZ", "Luzembourgish"),
    LG("LG", "LUG", "LUG", "Ganda"),
    LI("LI", "LIM", "LIM", "Limburgish"),
    LN("LN", "LIN", "LIN", "Lingala"),
    LO("LO", "LAO", "LAO", "Lao"),
    LT("LT", "LIT", "LIT", "Lithuanian"),
    LU("LU", "LUB", "LUB", "Luba Katanga"),
    LV("LV", "LAV", "LAV", "Latvian"),
    GV("GV", "GLV", "GLV", "Manx"),
    MK("MK", "MKD", "MAC", "Macedonian"),
    MG("MG", "MLG", "MLG", "Malagasy"),
    MS("MS", "MSA", "MAY", "Malay"),
    ML("ML", "MAL", "MAL", "Malayalam"),
    MT("MT", "MLT", "MLT", "Maltese"),
    MI("MI", "MRI", "MAO", "Maori"),
    MR("MR", "MAR", "MAR", "Marathi"),
    MH("MH", "MAH", "MAH", "Marshallese"),
    MN("MN", "MON", "MON", "Mongolian"),
    NA("NA", "NAU", "NAU", "Nauru"),
    NV("NV", "NAV", "NAV", "Navajo"),
    NB("NB", "NOB", "NOB", "Norwegian Bokmal"),
    ND("ND", "NDE", "NDE", "North Ndebele"),
    NE("NE", "NEP", "NEP", "Nepali"),
    NG("NG", "NDO", "NDO", "Ndonga"),
    NN("NN", "NNO", "NNO", "Norwegian Nynorsk"),
    NO("NO", "NOR", "NOR", "Norwegian"),
    II("II", "III", "III", "Nuosu"),
    NR("NR", "NBL", "NBL", "South Ndebele"),
    OC("OC", "OCI", "OCI", "Occitan"),
    OJ("OJ", "OJI", "OJI", "Ojibwe"),
    CU("CU", "CHU", "CHU", "Old Church Slavonic"),
    OM("OM", "ORM", "ORM", "Oromo"),
    OR("OR", "ORI", "ORI", "Oriya"),
    OS("OS", "OSS", "OSS", "Ossetian"),
    PA("PA", "PAN", "PAN", "Panjabi"),
    PI("PI", "PLI", "PLI", "Pali"),
    FA("FA", "FAS", "PER", "Persian"),
    PL("PL", "POL", "POL", "Polish"),
    PS("PS", "PUS", "PUS", "Pashto"),
    PT("PT", "POR", "POR", "Portuguese"),
    QU("QU", "QUE", "QUE", "Quechua"),
    RM("RM", "ROH", "ROH", "Romansh"),
    RU("RU", "RUS", "RUS", "Russian"),
    SA("SA", "SAN", "SAN", "Sanskrit"),
    SC("SC", "SRD", "SRD", "Sardinian"),
    SD("SD", "SND", "SND", "Sindhi"),
    SE("SE", "SME", "SME", "Northern Sami"),
    SM("SM", "SMO", "SMO", "Samoan"),
    SG("SG", "SAG", "SAG", "Sango"),
    SR("SR", "SRP", "SRP", "Serbian"),
    GD("GD", "GLA", "GLA", "Scottish Gaelic"),
    SN("SN", "SNA", "SNA", "Shona"),
    SI("SI", "SIN", "SIN", "Sinhala"),
    SK("SK", "SLK", "SLO", "Slovak"),
    SL("SL", "SLV", "SLV", "Slovene"),
    SO("SO", "SOM", "SOM", "Somali"),
    ST("ST", "SOT", "SOT", "Southern Sotho"),
    ES("ES", "SPA", "SPA", "Spanish"),
    SU("SU", "SUN", "SUN", "Sundanese"),
    SW("SW", "SWA", "SWA", "Swahili"),
    SS("SS", "SSW", "SSW", "Swati"),
    SV("SV", "SWE", "SWE", "Swedish"),
    TA("TA", "TAM", "TAM", "Tamil"),
    TE("TE", "TEL", "TEL", "Telugu"),
    TG("TG", "TGK", "TGK", "Tajik"),
    TH("TH", "THA", "THA", "Thai"),
    TI("TI", "TIR", "TIR", "Tigrinya"),
    BO("BO", "BOD", "TIB", "Tibetan Standard"),
    TK("TK", "TUK", "TUK", "Turkmen"),
    TL("TL", "TGL", "TGL", "Tagalog"),
    TN("TN", "TSN", "TSN", "Tswana"),
    TO("TO", "TON", "TON", "Tonga"),
    TR("TR", "TUR", "TUR", "Turkish"),
    TS("TS", "TSO", "TSO", "Tsonga"),
    TT("TT", "TAT", "TAT", "Tatar"),
    TW("TW", "TWI", "TWI", "Twi"),
    TY("TY", "TAH", "TAH", "Tahitian"),
    UG("UG", "UIG", "UIG", "Uighur"),
    UK("UK", "UKR", "UKR", "Ukrainian"),
    UR("UR", "URD", "URD", "Urdu"),
    UZ("UZ", "UZB", "UZB", "Uzbek"),
    VE("VE", "VEN", "VEN", "Venda"),
    VI("VI", "VIE", "VIE", "Vietnamese"),
    VO("VO", "VOL", "VOL", "Volapuk"),
    WA("WA", "WLN", "WLN", "Walloon"),
    CY("CY", "CYM", "WEL", "Welsh"),
    WO("WO", "WOL", "WOL", "Wolof"),
    FY("FY", "FRY", "FRY", "Western Frisian"),
    XH("XH", "XHO", "XHO", "Xhosa"),
    YI("YI", "YID", "YID", "Yiddish"),
    YO("YO", "YOR", "YOR", "Yoruba"),
    ZA("ZA", "ZHA", "ZHA", "Zhuang"),
    ZU("ZU", "ZUL", "ZUL", "Zulu"),
    ;


    /** ISO 639‑1 code */
    private final String iso1;
    /** ISO 639‑2 code */
    private final String iso2;
    /** ISO 639‑3 code */
    private final String iso3;
    /** English name */
    private final String desc;

    Language(String iso1, String iso2, String iso3, String desc) {
        this.iso1=iso1;
        this.iso2=iso2;
        this.iso3=iso3;
        this.desc=desc;
    }

    /**
     * The language's 2-letter ISO code.
     *
     * @return the ISO 639‑1 code for this language
     */
    public String iso1() {return iso1;}

    /**
     * The language's family 3-letter ISO code.
     *
     * @return the ISO 639‑2 code for this language (macro-language or family)
     */
    public String iso2() {return iso2;}

    /**
     * The language's dialect 3-letter ISO code.
     *
     * @return the ISO 639‑3 code for this language (minor-language or dialect)
     */
    public String iso3() {return iso3;}

    /**
     * The language's English name.
     *
     * @return a human-readable name for this language
     */
    public String desc() {return desc;}


    private static final Map<String,Language> i1v=new HashMap<String,Language>();
    static { for(Language lc : values()) i1v.put(lc.iso1.toUpperCase(), lc); }
    public static Language forISO1(String lc) { return i1v.get(lc.toUpperCase()); }

    private static final Map<String,Language> i2v=new HashMap<String,Language>();
    static { for(Language lc : values()) i2v.put(lc.iso2.toUpperCase(), lc); }
    public static Language forISO2(String lc) { return i2v.get(lc.toUpperCase()); }

    private static final Map<String,Language> i3v=new HashMap<String,Language>();
    static { for(Language lc : values()) i3v.put(lc.iso3.toUpperCase(), lc); }
    public static Language forISO3(String lc) { return i3v.get(lc.toUpperCase()); }

    private static final Map<String,Language> nmv=new HashMap<String,Language>();
    static { for(Language lc : values()) nmv.put(lc.desc.toUpperCase(), lc); }
    public static Language forName(String lc) { return nmv.get(lc.toUpperCase()); }

    /**
     * Resolve a language by one of its ISO codes,
     * in order of ISO 639‑1, ISO 639‑2 and ISO 639‑3 code
     *
     * @param code the language code
     * @return the language identified by this code, or {@literal null} if not found
     */
    public static Language forCode(String code) {
        code=code.toUpperCase();
        int pos=code.indexOf('_');
        if(pos>0) code=code.substring(0, pos);
        if(i1v.containsKey(code)) return i1v.get(code);
        if(i2v.containsKey(code)) return i2v.get(code);
        if(i3v.containsKey(code)) return i3v.get(code);
        return null;
    }

}
