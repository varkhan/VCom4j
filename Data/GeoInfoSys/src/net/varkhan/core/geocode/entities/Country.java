package net.varkhan.core.geocode.entities;

import net.varkhan.core.geocode.features.Division;
import net.varkhan.core.geocode.features.Entity;
import net.varkhan.core.geocode.features.Typology;

import java.util.HashMap;
import java.util.Map;


/**
 * <b>ISO Country codes</b>.
 * <p/>
 * The ISO 3166-1 alpha-2, ISO 3166-1 alpha-3 and ISO 3166-1 numeric country code table.
 *
 * @author varkhan
 * @date 9/3/12
 * @time 6:19 PM
 */
@SuppressWarnings({ "UnusedDeclaration" })
public enum Country implements Entity {

    AF("AF","AFG","004", Zone.AS, Region.SOUTH_ASIA,"Afghanistan","AF","AF"),
    AL("AL","ALB","008", Zone.EU, Region.SOUTH_EAST_EUROPE,"Albania","AL","AL"),
    DZ("DZ","DZA","012", Zone.AF, Region.NORTHERN_AFRICA,"Algeria","AG","DZ"),
    AS("AS","ASM","016", Zone.OC, Region.PACIFIC_OCEAN,"American Samoa","AQ","AS"),
    AD("AD","AND","020", Zone.EU, Region.SOUTH_WEST_EUROPE,"Andorra","AN","AD"),
    AO("AO","AGO","024", Zone.AF, Region.SOUTHERN_AFRICA,"Angola","AO","AO"),
    AI("AI","AIA","660", Zone.NA, Region.WEST_INDIES,"Anguilla","AV","AI"),
    AG("AG","ATG","028", Zone.NA, Region.WEST_INDIES,"Antigua and Barbuda","AC","AG"),
    AR("AR","ARG","032", Zone.SA, Region.SOUTH_AMERICA,"Argentina","AR","AR"),
    AM("AM","ARM","051", Zone.AS, Region.SOUTH_WEST_ASIA,"Armenia","AM","AM"),
    AW("AW","ABW","533", Zone.NA, Region.WEST_INDIES,"Aruba","AA","AW"),
    AU("AU","AUS","036", Zone.AU, Region.PACIFIC_OCEAN,"Australia","AS","AU"),
    AT("AT","AUT","040", Zone.EU, Region.CENTRAL_EUROPE,"Austria","AU","AT"),
    AZ("AZ","AZE","031", Zone.AS, Region.SOUTH_WEST_ASIA,"Azerbaijan","AJ","AZ"),
    BS("BS","BHS","044", Zone.NA, Region.WEST_INDIES,"Bahamas, The","BF","BS"),
    BH("BH","BHR","048", Zone.AS, Region.SOUTH_WEST_ASIA,"Bahrain","BA","BH"),
    BD("BD","BGD","050", Zone.AS, Region.SOUTH_ASIA,"Bangladesh","BG","BD"),
    BB("BB","BRB","052", Zone.NA, Region.WEST_INDIES,"Barbados","BB","BB"),
    BY("BY","BLR","112", Zone.EU, Region.EASTERN_EUROPE,"Belarus","BO","BY"),
    BE("BE","BEL","056", Zone.EU, Region.WESTERN_EUROPE,"Belgium","BE","BE"),
    BZ("BZ","BLZ","084", Zone.NA, Region.CENTRAL_AMERICA,"Belize","BH","BZ"),
    BJ("BJ","BEN","204", Zone.AF, Region.WESTERN_AFRICA,"Benin","BN","BJ"),
    BM("BM","BMU","060", Zone.NA, Region.WEST_INDIES,"Bermuda","BD","BM"),
    BT("BT","BTN","064", Zone.AS, Region.SOUTH_ASIA,"Bhutan","BT","BT"),
    BO("BO","BOL","068", Zone.SA, Region.SOUTH_AMERICA,"Bolivia","BL","BO"),
    BA("BA","BIH","070", Zone.EU, Region.SOUTH_EAST_EUROPE,"Bosnia and Herzegovina","BK","BA"),
    BW("BW","BWA","072", Zone.AF, Region.SOUTHERN_AFRICA,"Botswana","BC","BW"),
    BR("BR","BRA","076", Zone.SA, Region.SOUTH_AMERICA,"Brazil","BR","BR"),
    VG("VG","VGB","092", Zone.NA, Region.WEST_INDIES,"British Virgin Islands","VI","VG"),
    BN("BN","BRN","096", Zone.AS, Region.SOUTH_EAST_ASIA,"Brunei","BX","BN"),
    BG("BG","BGR","100", Zone.EU, Region.SOUTH_EAST_EUROPE,"Bulgaria","BU","BG"),
    BF("BF","BFA","854", Zone.AF, Region.WESTERN_AFRICA,"Burkina Faso","UV","BF"),
    BI("BI","BDI","108", Zone.AF, Region.CENTRAL_AFRICA,"Burundi","BY","BI"),
    KH("KH","KHM","116", Zone.AS, Region.SOUTH_EAST_ASIA,"Cambodia","CB","KH"),
    CM("CM","CMR","120", Zone.AF, Region.WESTERN_AFRICA,"Cameroon","CM","CM"),
    CA("CA","CAN","124", Zone.NA, Region.NORTH_AMERICA,"Canada","CA","CA"),
    CV("CV","CPV","132", Zone.AF, Region.WESTERN_AFRICA,"Cape Verde","CV","CV"),
    KY("KY","CYM","136", Zone.NA, Region.WEST_INDIES,"Cayman Islands","CJ","KY"),
    CF("CF","CAF","140", Zone.AF, Region.CENTRAL_AFRICA,"Central African Republic","CT","CF"),
    TD("TD","TCD","148", Zone.AF, Region.CENTRAL_AFRICA,"Chad","CD","TD"),
    CL("CL","CHL","152", Zone.SA, Region.SOUTH_AMERICA,"Chile","CI","CL"),
    CN("CN","CHN","156", Zone.AS, Region.EAST_ASIA,"China","CH","CN"),
    CX("CX","CXR","162", Zone.AS, Region.SOUTH_EAST_ASIA,"Christmas Island","KT","CX"),
    CC("CC","CCK","166", Zone.AS, Region.SOUTH_EAST_ASIA,"Cocos (Keeling) Islands","CK","CC"),
    CO("CO","COL","170", Zone.SA, Region.SOUTH_AMERICA,"Colombia","CO","CO"),
    KM("KM","COM","174", Zone.AF, Region.INDIAN_OCEAN,"Comoros","CN","KM"),
    CG("CG","COG","178", Zone.AF, Region.CENTRAL_AFRICA,"Congo, Republic of the","CF","CG"),
    CK("CK","COK","184", Zone.OC, Region.PACIFIC_OCEAN,"Cook Islands","CW","CK"),
    CR("CR","CRI","188", Zone.NA, Region.CENTRAL_AMERICA,"Costa Rica","CS","CR"),
    CI("CI","CIV","384", Zone.AF, Region.WESTERN_AFRICA,"Cote d'Ivoire","IV","CI"),
    HR("HR","HRV","191", Zone.EU, Region.SOUTH_EAST_EUROPE,"Croatia","HR","HR"),
    CU("CU","CUB","192", Zone.NA, Region.WEST_INDIES,"Cuba","CU","CU"),
    CY("CY","CYP","196", Zone.AS, Region.SOUTH_WEST_ASIA,"Cyprus","CY","CY"),
    CZ("CZ","CZE","203", Zone.EU, Region.CENTRAL_EUROPE,"Czech Republic","EZ","CZ"),
    DK("DK","DNK","208", Zone.EU, Region.NORTHERN_EUROPE,"Denmark","DA","DK"),
    DJ("DJ","DJI","262", Zone.AF, Region.EASTERN_AFRICA,"Djibouti","DJ","DJ"),
    DM("DM","DMA","212", Zone.NA, Region.WEST_INDIES,"Dominica","DO","DM"),
    DO("DO","DOM","214", Zone.NA, Region.WEST_INDIES,"Dominican Republic","DR","DO"),
    EC("EC","ECU","218", Zone.SA, Region.SOUTH_AMERICA,"Ecuador","EC","EC"),
    EG("EG","EGY","818", Zone.AF, Region.NORTHERN_AFRICA,"Egypt","EG","EG"),
    SV("SV","SLV","222", Zone.NA, Region.CENTRAL_AMERICA,"El Salvador","ES","SV"),
    GQ("GQ","GNQ","226", Zone.AF, Region.WESTERN_AFRICA,"Equatorial Guinea","EK","GQ"),
    ER("ER","ERI","232", Zone.AF, Region.EASTERN_AFRICA,"Eritrea","ER","ER"),
    EE("EE","EST","233", Zone.EU, Region.EASTERN_EUROPE,"Estonia","EN","EE"),
    ET("ET","ETH","231", Zone.AF, Region.EASTERN_AFRICA,"Ethiopia","ET","ET"),
    FK("FK","FLK","238", Zone.SA, Region.SOUTH_AMERICA,"Falkland Islands (Islas Malvinas)","FA","FK"),
    FO("FO","FRO","234", Zone.EU, Region.NORTHERN_EUROPE,"Faroe Islands","FO","FO"),
    FJ("FJ","FJI","242", Zone.OC, Region.PACIFIC_OCEAN,"Fiji","FJ","FJ"),
    FI("FI","FIN","246", Zone.EU, Region.NORTHERN_EUROPE,"Finland","FI","FI"),
    FR("FR","FRA","250", Zone.EU, Region.WESTERN_EUROPE,"France","FR","FR"),
    GF("GF","GUF","254", Zone.SA, Region.SOUTH_AMERICA,"French Guiana","FG","GF"),
    PF("PF","PYF","258", Zone.OC, Region.PACIFIC_OCEAN,"French Polynesia","FP","PF"),
    GA("GA","GAB","266", Zone.AF, Region.WESTERN_AFRICA,"Gabon","GB","GA"),
    GM("GM","GMB","270", Zone.AF, Region.WESTERN_AFRICA,"Gambia, The","GA","GM"),
    GE("GE","GEO","268", Zone.AS, Region.SOUTH_WEST_ASIA,"Georgia","GG","GE"),
    DE("DE","DEU","276", Zone.EU, Region.WESTERN_EUROPE,"Germany","GM","DE"),
    GH("GH","GHA","288", Zone.AF, Region.WESTERN_AFRICA,"Ghana","GH","GH"),
    GI("GI","GIB","292", Zone.EU, Region.SOUTH_WEST_EUROPE,"Gibraltar","GI","GI"),
    GR("GR","GRC","300", Zone.EU, Region.SOUTH_EAST_EUROPE,"Greece","GR","GR"),
    GL("GL","GRL","304", Zone.NA, Region.NORTH_AMERICA,"Greenland","GL","GL"),
    GD("GD","GRD","308", Zone.NA, Region.WEST_INDIES,"Grenada","GJ","GD"),
    GP("GP","GLP","312", Zone.NA, Region.WEST_INDIES,"Guadeloupe","GP","GP"),
    GU("GU","GUM","316", Zone.OC, Region.PACIFIC_OCEAN,"Guam","GQ","GU"),
    GT("GT","GTM","320", Zone.NA, Region.CENTRAL_AMERICA,"Guatemala","GT","GT"),
    // Europe Western Europe Guernsey Saint Peter Port -- -- -- --
    GN("GN","GIN","324", Zone.AF, Region.WESTERN_AFRICA,"Guinea","GV","GN"),
    GW("GW","GNB","624", Zone.AF, Region.WESTERN_AFRICA,"Guinea-Bissau","PU","GW"),
    GY("GY","GUY","328", Zone.SA, Region.SOUTH_AMERICA,"Guyana","GY","GY"),
    HT("HT","HTI","332", Zone.NA, Region.WEST_INDIES,"Haiti","HA","HT"),
    VA("VA","VAT","336", Zone.EU, Region.SOUTHERN_EUROPE,"Holy See (Vatican City)","VT","VA"),
    HN("HN","HND","340", Zone.NA, Region.CENTRAL_AMERICA,"Honduras","HO","HN"),
    HU("HU","HUN","348", Zone.EU, Region.CENTRAL_EUROPE,"Hungary","HU","HU"),
    IS("IS","ISL","352", Zone.EU, Region.NORTHERN_EUROPE,"Iceland","IC","IS"),
    IN("IN","IND","356", Zone.AS, Region.SOUTH_ASIA,"India","IN","IN"),
    ID("ID","IDN","360", Zone.AS, Region.SOUTH_EAST_ASIA,"Indonesia","ID","ID"),
    IR("IR","IRN","364", Zone.AS, Region.SOUTH_WEST_ASIA,"Iran","IR","IR"),
    IQ("IQ","IRQ","368", Zone.AS, Region.SOUTH_WEST_ASIA,"Iraq","IZ","IQ"),
    IE("IE","IRL","372", Zone.EU, Region.WESTERN_EUROPE,"Ireland","EI","IE"),
    IL("IL","ISR","376", Zone.AS, Region.SOUTH_WEST_ASIA,"Israel","IS","IL"),
    IT("IT","ITA","380", Zone.EU, Region.SOUTHERN_EUROPE,"Italy","IT","IT"),
    JM("JM","JAM","388", Zone.NA, Region.WEST_INDIES,"Jamaica","JM","JM"),
    // Europe Northern Europe Jan Mayen - -- -- -- --
    JP("JP","JPN","392", Zone.AS, Region.EAST_ASIA,"Japan","JA","JP"),
    // Europe Western Europe Jersey Saint Helier -- -- -- --
    JO("JO","JOR","400", Zone.AS, Region.SOUTH_WEST_ASIA,"Jordan","JO","JO"),
    KZ("KZ","KAZ","398", Zone.AS, Region.CENTRAL_ASIA,"Kazakhstan","KZ","KZ"),
    KE("KE","KEN","404", Zone.AF, Region.EASTERN_AFRICA,"Kenya","KE","KE"),
    KI("KI","KIR","296", Zone.OC, Region.PACIFIC_OCEAN,"Kiribati","KR","KI"),
    KP("KP","PRK","408", Zone.AS, Region.EAST_ASIA,"North Korea","KN","KP"),
    KR("KR","KOR","410", Zone.AS, Region.EAST_ASIA,"South Korea","KS","KR"),
    KW("KW","KWT","414", Zone.AS, Region.SOUTH_WEST_ASIA,"Kuwait","KU","KW"),
    KG("KG","KGZ","417", Zone.AS, Region.CENTRAL_ASIA,"Kyrgyzstan","KG","KG"),
    LA("LA","LAO","418", Zone.AS, Region.SOUTH_EAST_ASIA,"Laos","LA","LA"),
    LV("LV","LVA","428", Zone.EU, Region.EASTERN_EUROPE,"Latvia","LG","LV"),
    LB("LB","LBN","422", Zone.AS, Region.SOUTH_WEST_ASIA,"Lebanon","LE","LB"),
    LS("LS","LSO","426", Zone.AF, Region.SOUTHERN_AFRICA,"Lesotho","LT","LS"),
    LR("LR","LBR","430", Zone.AF, Region.WESTERN_AFRICA,"Liberia","LI","LR"),
    LY("LY","LBY","434", Zone.AF, Region.NORTHERN_AFRICA,"Libya","LY","LY"),
    LI("LI","LIE","438", Zone.EU, Region.CENTRAL_EUROPE,"Liechtenstein","LS","LI"),
    LT("LT","LTU","440", Zone.EU, Region.EASTERN_EUROPE,"Lithuania","LH","LT"),
    LU("LU","LUX","442", Zone.EU, Region.WESTERN_EUROPE,"Luxembourg","LU","LU"),
    MK("MK","MKD","807", Zone.EU, Region.SOUTH_EAST_EUROPE,"Macedonia","MK","MK"),
    MG("MG","MDG","450", Zone.AF, Region.INDIAN_OCEAN,"Madagascar","MA","MG"),
    MW("MW","MWI","454", Zone.AF, Region.SOUTHERN_AFRICA,"Malawi","MI","MW"),
    MY("MY","MYS","458", Zone.AS, Region.SOUTH_EAST_ASIA,"Malaysia","MY","MY"),
    MV("MV","MDV","462", Zone.AS, Region.SOUTH_ASIA,"Maldives","MV","MV"),
    ML("ML","MLI","466", Zone.AF, Region.WESTERN_AFRICA,"Mali","ML","ML"),
    MT("MT","MLT","470", Zone.EU, Region.SOUTHERN_EUROPE,"Malta","MT","MT"),
    // Europe Western Europe Man, Isle of Douglas -- -- -- --
    MH("MH","MHL","584", Zone.OC, Region.PACIFIC_OCEAN,"Marshall Islands","RM","MH"),
    MQ("MQ","MTQ","474", Zone.NA, Region.WEST_INDIES,"Martinique","MB","MQ"),
    MR("MR","MRT","478", Zone.AF, Region.WESTERN_AFRICA,"Mauritania","MR","MR"),
    MU("MU","MUS","480", Zone.AF, Region.INDIAN_OCEAN,"Mauritius","MP","MU"),
    YT("YT","MYT","175", Zone.AF, Region.INDIAN_OCEAN,"Mayotte","MF","YT"),
    MX("MX","MEX","484", Zone.NA, Region.CENTRAL_AMERICA,"Mexico","MX","MX"),
    FM("FM","FSM","583", Zone.OC, Region.PACIFIC_OCEAN,"Micronesia, Federated States of","XX","FM"),
    MD("MD","MDA","498", Zone.EU, Region.EASTERN_EUROPE,"Moldova","MD","MD"),
    MC("MC","MCO","492", Zone.EU, Region.WESTERN_EUROPE,"Monaco","MN","MC"),
    MN("MN","MNG","496", Zone.AS, Region.NORTHERN_ASIA,"Mongolia","MG","MN"),
    MS("MS","MSR","500", Zone.NA, Region.WEST_INDIES,"Montserrat","MH","MS"),
    MA("MA","MAR","504", Zone.AF, Region.NORTHERN_AFRICA,"Morocco","MO","MA"),
    MZ("MZ","MOZ","508", Zone.AF, Region.SOUTHERN_AFRICA,"Mozambique","MZ","MZ"),
    MM("MM","MMR","104", Zone.AS, Region.SOUTH_EAST_ASIA,"Myanmar","BM","MM"),
    NA("NA","NAM","516", Zone.AF, Region.SOUTHERN_AFRICA,"Namibia","WA","NA"),
    NR("NR","NRU","520", Zone.OC, Region.PACIFIC_OCEAN,"Nauru","NR","NR"),
    NP("NP","NPL","524", Zone.AS, Region.SOUTH_ASIA,"Nepal","NP","NP"),
    NL("NL","NLD","528", Zone.EU, Region.WESTERN_EUROPE,"Netherlands","NL","NL"),
    AN("AN","ANT","530", Zone.NA, Region.WEST_INDIES,"Netherlands Antilles","NT","AN"),
    NC("NC","NCL","540", Zone.AU, Region.PACIFIC_OCEAN,"New Caledonia","NC","NC"),
    NZ("NZ","NZL","554", Zone.AU, Region.PACIFIC_OCEAN,"New Zealand","NZ","NZ"),
    NI("NI","NIC","558", Zone.NA, Region.CENTRAL_AMERICA,"Nicaragua","NU","NI"),
    NE("NE","NER","562", Zone.AF, Region.WESTERN_AFRICA,"Niger","NG","NE"),
    NG("NG","NGA","566", Zone.AF, Region.WESTERN_AFRICA,"Nigeria","NI","NG"),
    NU("NU","NIU","570", Zone.OC, Region.PACIFIC_OCEAN,"Niue","NE","NU"),
    NF("NF","NFK","574", Zone.OC, Region.PACIFIC_OCEAN,"Norfolk Island","NF","NF"),
    MP("MP","MNP","580", Zone.OC, Region.PACIFIC_OCEAN,"Northern Mariana Islands","CQ","MP"),
    NO("NO","NOR","578", Zone.EU, Region.NORTHERN_EUROPE,"Norway","NO","NO"),
    OM("OM","OMN","512", Zone.AS, Region.SOUTH_WEST_ASIA,"Oman","MU","OM"),
    PK("PK","PAK","586", Zone.AS, Region.SOUTH_ASIA,"Pakistan","PK","PK"),
    PW("PW","PLW","585", Zone.OC, Region.PACIFIC_OCEAN,"Palau","PS","PW"),
    // Asia South West Asia Palestine -- -- -- --
    PA("PA","PAN","591", Zone.NA, Region.CENTRAL_AMERICA,"Panama","PM","PA"),
    PG("PG","PNG","598", Zone.OC, Region.PACIFIC_OCEAN,"Papua New Guinea","PP","PG"),
    PY("PY","PRY","600", Zone.SA, Region.SOUTH_AMERICA,"Paraguay","PA","PY"),
    PE("PE","PER","604", Zone.SA, Region.SOUTH_AMERICA,"Peru","PE","PE"),
    PH("PH","PHL","608", Zone.AS, Region.SOUTH_EAST_ASIA,"Philippines","RP","PH"),
    PN("PN","PCN","612", Zone.OC, Region.PACIFIC_OCEAN,"Pitcairn Islands","PC","PN"),
    PL("PL","POL","616", Zone.EU, Region.EASTERN_EUROPE,"Poland","PL","PL"),
    PT("PT","PRT","620", Zone.EU, Region.SOUTH_WEST_EUROPE,"Portugal","PO","PT"),
    PR("PR","PRI","630", Zone.NA, Region.WEST_INDIES,"Puerto Rico","RQ","PR"),
    QA("QA","QAT","634", Zone.AS, Region.SOUTH_WEST_ASIA,"Qatar","QA","QA"),
    RE("RE","REU","638", Zone.AF, Region.INDIAN_OCEAN,"Reunion","RE","RE"),
    RO("RO","ROM","642", Zone.EU, Region.SOUTH_EAST_EUROPE,"Romania","RO","RO"),
    RU("RU","RUS","643", Zone.AS, Region.NORTHERN_ASIA,"Russia","RS","RU"),
    RW("RW","RWA","646", Zone.AF, Region.CENTRAL_AFRICA,"Rwanda","RW","RW"),
    KN("KN","KNA","659", Zone.NA, Region.WEST_INDIES,"Saint Kitts and Nevis","SC","KN"),
    LC("LC","LCA","662", Zone.NA, Region.WEST_INDIES,"Saint Lucia","ST","LC"),
    PM("PM","SPM","666", Zone.NA, Region.NORTH_AMERICA,"Saint Pierre and Miquelon","SB","PM"),
    VC("VC","VCT","670", Zone.NA, Region.WEST_INDIES,"Saint Vincent and the Grenadines","VC","VC"),
    SM("SM","SMR","674", Zone.EU, Region.SOUTHERN_EUROPE,"San Marino","SM","SM"),
    ST("ST","STP","678", Zone.AF, Region.WESTERN_AFRICA,"Sao Tome and Principe","TP","ST"),
    SA("SA","SAU","682", Zone.AS, Region.SOUTH_WEST_ASIA,"Saudi Arabia","SA","SA"),
    SN("SN","SEN","686", Zone.AF, Region.WESTERN_AFRICA,"Senegal","SG","SN"),
    // Europe South East Europe Serbia and Montenegro Belgrade / Podgorica SR -- -- --
    SC("SC","SYC","690", Zone.AF, Region.INDIAN_OCEAN,"Seychelles","SE","SC"),
    SL("SL","SLE","694", Zone.AF, Region.WESTERN_AFRICA,"Sierra Leone","SL","SL"),
    SG("SG","SGP","702", Zone.AS, Region.SOUTH_EAST_ASIA,"Singapore","SN","SG"),
    SK("SK","SVK","703", Zone.EU, Region.CENTRAL_EUROPE,"Slovakia","LO","SK"),
    SI("SI","SVN","705", Zone.EU, Region.SOUTH_EAST_EUROPE,"Slovenia","SI","SI"),
    SB("SB","SLB","090", Zone.OC, Region.PACIFIC_OCEAN,"Solomon Islands","BP","SB"),
    SO("SO","SOM","706", Zone.AF, Region.EASTERN_AFRICA,"Somalia","SO","SO"),
    ZA("ZA","ZAF","710", Zone.AF, Region.SOUTHERN_AFRICA,"South Africa","SF","ZA"),
    ES("ES","ESP","724", Zone.EU, Region.SOUTH_WEST_EUROPE,"Spain","SP","ES"),
    LK("LK","LKA","144", Zone.AS, Region.SOUTH_ASIA,"Sri Lanka","CE","LK"),
    SD("SD","SDN","736", Zone.AF, Region.NORTHERN_AFRICA,"Sudan","SU","SD"),
    SR("SR","SUR","740", Zone.SA, Region.SOUTH_AMERICA,"Suriname","NS","SR"),
    SJ("SJ","SJM","744", Zone.EU, Region.NORTHERN_EUROPE,"Svalbard","SV","SJ"),
    SZ("SZ","SWZ","748", Zone.AF, Region.SOUTHERN_AFRICA,"Swaziland","WZ","SZ"),
    SE("SE","SWE","752", Zone.EU, Region.NORTHERN_EUROPE,"Sweden","SW","SE"),
    CH("CH","CHE","756", Zone.EU, Region.CENTRAL_EUROPE,"Switzerland","SZ","CH"),
    SY("SY","SYR","760", Zone.AS, Region.SOUTH_WEST_ASIA,"Syria","SY","SY"),
    TW("TW","TWN","158", Zone.AS, Region.EAST_ASIA,"Taiwan","TW","TW"),
    TJ("TJ","TJK","762", Zone.AS, Region.CENTRAL_ASIA,"Tajikistan","TI","TJ"),
    TZ("TZ","TZA","834", Zone.AF, Region.EASTERN_AFRICA,"Tanzania","TZ","TZ"),
    TH("TH","THA","764", Zone.AS, Region.SOUTH_EAST_ASIA,"Thailand","TH","TH"),
    TG("TG","TGO","768", Zone.AF, Region.WESTERN_AFRICA,"Togo","TO","TG"),
    TK("TK","TKL","772", Zone.OC, Region.PACIFIC_OCEAN,"Tokelau","TL","TK"),
    TO("TO","TON","776", Zone.OC, Region.PACIFIC_OCEAN,"Tonga","TN","TO"),
    TT("TT","TTO","780", Zone.NA, Region.WEST_INDIES,"Trinidad and Tobago","TD","TT"),
    TN("TN","TUN","788", Zone.AF, Region.NORTHERN_AFRICA,"Tunisia","TS","TN"),
    TR("TR","TUR","792", Zone.AS, Region.SOUTH_WEST_ASIA,"Turkey","TU","TR"),
    TM("TM","TKM","795", Zone.AS, Region.CENTRAL_ASIA,"Turkmenistan","TX","TM"),
    TC("TC","TCA","796", Zone.NA, Region.WEST_INDIES,"Turks and Caicos Islands","TK","TC"),
    TV("TV","TUV","798", Zone.OC, Region.PACIFIC_OCEAN,"Tuvalu","TV","TV"),
    UG("UG","UGA","800", Zone.AF, Region.EASTERN_AFRICA,"Uganda","UG","UG"),
    UA("UA","UKR","804", Zone.EU, Region.EASTERN_EUROPE,"Ukraine","UP","UA"),
    AE("AE","ARE","784", Zone.AS, Region.SOUTH_WEST_ASIA,"United Arab Emirates","TC","AE"),
    GB("GB","GBR","826", Zone.EU, Region.WESTERN_EUROPE,"United Kingdom","UK","UK"),
    US("US","USA","840", Zone.NA, Region.NORTH_AMERICA,"United States","US","US"),
    UY("UY","URY","858", Zone.SA, Region.SOUTH_AMERICA,"Uruguay","UY","UY"),
    UZ("UZ","UZB","860", Zone.AS, Region.CENTRAL_ASIA,"Uzbekistan","UZ","UZ"),
    VU("VU","VUT","548", Zone.OC, Region.PACIFIC_OCEAN,"Vanuatu","NH","VU"),
    VE("VE","VEN","862", Zone.SA, Region.SOUTH_AMERICA,"Venezuela","VE","UE"),
    VN("VN","VNM","704", Zone.AS, Region.SOUTH_EAST_ASIA,"Vietnam","VM","VN"),
    VI("VI","VIR","850", Zone.NA, Region.WEST_INDIES,"Virgin Islands","VQ","VI"),
    WF("WF","WLF","876", Zone.OC, Region.PACIFIC_OCEAN,"Wallis and Futuna","WF","WF"),
    EH("EH","ESH","732", Zone.AF, Region.NORTHERN_AFRICA,"Western Sahara","WI","EH"),
    WS("WS","WSM","882", Zone.OC, Region.PACIFIC_OCEAN,"Western Samoa","WS","WS"),
    YE("YE","YEM","887", Zone.AS, Region.SOUTH_WEST_ASIA,"Yemen","YM","YE"),
    ZR("ZR","ZAR","180", Zone.AF, Region.CENTRAL_AFRICA,"Zaire (Dem Rep of Congo)","CG","ZR"),
    ZM("ZM","ZWB","894", Zone.AF, Region.SOUTHERN_AFRICA,"Zambia","ZA","ZM"),
    ZW("ZW","ZWE","716", Zone.AF, Region.SOUTHERN_AFRICA,"Zimbabwe","ZI","ZW"),




    HK("HK","HKG","344", Zone.AS, Region.EAST_ASIA,"Hong Kong","HK","HK"),
    MO("MO","MAC","446", Zone.AS, Region.EAST_ASIA,"Macau","MC","MO"),
    AQ("AQ","ATA","010", Zone.AN, Region.ANTARCTICA, "Antartica","AY","AQ"),
    BV("BV","BVT","074", Zone.AO, Region.SOUTH_ATLANTIC_OCEAN,"Bouvet","BV","BV"),
    IO("IO","IOT","086", Zone.AS, Region.SOUTH_ASIA,"British Indian Ocean","IO","IO"),
    TP("TP","TMP","626", Zone.AS, Region.SOUTH_EAST_ASIA,"East Timor","XX","TP"),
// FX("FX","FXX","249", Zone.EU, Region.WESTERN_EUROPE,"France, Metropolitan","XX","FX"),
    // Indian Ocean Southern Indian Ocean French Southern and Antarctic Lands FS TF ATF 260 --
    HM("HM","HMD","334", Zone.IO, Region.SOUTHERN_INDIAN_OCEAN,"Heard Island and McDonald","HM","HM"),
    // Europe South East Europe Montenegro XX MW XX XX XX XX
    SH("SH","SHN","654", Zone.AO, Region.SOUTH_ATLANTIC_OCEAN,"Saint Helena","SH","SH"),
    GS("GS","SGS","239", Zone.AO, Region.SOUTH_ATLANTIC_OCEAN,"South Georgia and the South Sandwich","SX","GS"),
    UM("UM","UMI","581", Zone.OC, Region.NORTH_PACIFIC_OCEAN,"United States Minor Outlying","XX","UM"),
// Europe South East Europe Yugoslavia* -- YU YUG 891 YU 5

    ;


    private final String iso2;
    private final String iso3;
    private final String isoC;
    private final Zone zone;
    private final Region regn;
    private final String desc;
    private final String fips;
    private final String ican;


    Country(String iso2, String iso3, String isoC, Zone zone, Region regn, String desc, String fips, String ican) {
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.isoC = isoC;
        this.zone=zone;
        this.regn = regn;
        this.desc = desc;
        this.fips = fips;
        this.ican = ican;
    }

    public Typology typology() { return Typology.Politic; }
    public Division division() { return Division.Country; }

    /**
     * The country's zone.
     *
     * @return the zone this country is in
     */
    public Zone zone() { return zone; }

    /**
     * The country's region.
     *
     * @return the region this country is in
     */
    public Region region() { return regn; }

    /**
     * The country's 2-letter ISO code.
     *
     * @return the ISO 3166-1 alpha-2 code for this country
     */
    public String iso2() { return iso2; }

    /**
     * The country's 3-letter ISO code.
     *
     * @return the ISO 3166-1 alpha-2 code for this country
     */
    public String iso3() { return iso3; }

    /**
     * The country's numeric ISO code.
     *
     * @return the ISO 3166-1 numeric code for this country
     */
    public String isoC() { return isoC; }

    /**
     * The country's English name.
     *
     * @return a human-readable name for this country
     */
    public String desc() { return desc; }

    /**
     * The country's FIPS code.
     *
     * @return the FIPS code for this country
     */
    public String fips() { return fips; }

    /**
     * The country's 2-letter domain name extension.
     *
     * @return the ICAN suffix for this country
     */
    public String ican() { return ican; }

    private static final Map<String,Country> i2v= new HashMap<String, Country>();
    static { for(Country c: values()) i2v.put(c.iso2.toUpperCase(),c); }
    public static Country forISO2(String cc) { return i2v.get(cc.toUpperCase()); }

    private static final Map<String,Country> i3v= new HashMap<String, Country>();
    static { for(Country c: values()) i3v.put(c.iso3.toUpperCase(),c); }
    public static Country forISO3(String cc) { return i3v.get(cc.toUpperCase()); }

    private static final Map<String,Country> icv= new HashMap<String, Country>();
    static { for(Country c: values()) icv.put(c.isoC.toUpperCase(),c); }
    public static Country forISOC(String cc) { return icv.get(cc.toUpperCase()); }

    private static final Map<String,Country> nmv= new HashMap<String, Country>();
    static { for(Country c: values()) nmv.put(c.desc.toUpperCase(),c); }
    public static Country forName(String cc) { return nmv.get(cc.toUpperCase()); }

    /**
     * Resolve a country by one of its ISO codes, in order of ISO 3166‑1 2-letter,
     * 3-letter and numeric codes, or by its name if not resolved as a code.
     *
     * @param code the country code
     * @return the country identified by this code, or {@literal null} if not found
     */
    public static Country forCode(String code) {
        code=code.toUpperCase();
        int pos=code.indexOf('_');
        if(pos>0) code=code.substring(0, pos);
        if(i2v.containsKey(code)) return i2v.get(code);
        if(i3v.containsKey(code)) return i3v.get(code);
        if(icv.containsKey(code)) return icv.get(code);
        return null;
    }

}
