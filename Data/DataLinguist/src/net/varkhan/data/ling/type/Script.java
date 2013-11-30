package net.varkhan.data.ling.type;

/**
 * <b>Scripts identification</b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/17/13
 * @time 4:20 PM
 */
public interface Script {

    /**
     * <b>A script type</b>.
     * <p/>
     *
     * @author varkhan
     * @date 11/17/13
     * @time 3:36 PM
     */
    public static enum Type {
    }

    /**
     * <b>A Unicode block</b>.
     * <p/>
     *
     * @author varkhan
     * @date 11/17/13
     * @time 3:36 PM
     */
    public static enum Block {
        UC_00000_00127(    0,   127, "Basic Latin"),
        UC_00128_00255(  128,   255, "Latin 1 Supplement"),
        UC_00256_00383(  256,   383, "Latin Extended A"),
        UC_00384_00591(  384,   591, "Latin Extended B"),
        UC_00592_00687(  592,   687, "IPA Extensions"),
        UC_00688_00767(  688,   767, "Spacing Modifier Letters"),
        UC_00768_00879(  768,   879, "Combining Diacritical Marks"),
        UC_00880_01023(  880,  1023, "Greek"),
        UC_01024_01279( 1024,  1279, "Cyrillic"),
        UC_01328_01423( 1328,  1423, "Armenian"),
        UC_01424_01535( 1424,  1535, "Hebrew"),
        UC_01536_01791( 1536,  1791, "Arabic"),
        UC_01792_01871( 1792,  1871, "Syriac"),
        UC_01920_01983( 1920,  1983, "Thaana"),
        UC_02304_02431( 2304,  2431, "Devanagari"),
        UC_02432_02559( 2432,  2559, "Bengali"),
        UC_02560_02687( 2560,  2687, "Gurmukhi"),
        UC_02688_02815( 2688,  2815, "Gujarati"),
        UC_02816_02943( 2816,  2943, "Oriya"),
        UC_02944_03071( 2944,  3071, "Tamil"),
        UC_03072_03199( 3072,  3199, "Telugu"),
        UC_03200_03327( 3200,  3327, "Kannanda"),
        UC_03328_03455( 3328,  3455, "Malayalam"),
        UC_03456_03583( 3456,  3583, "Synhala"),
        UC_03584_03771( 3584,  3771, "Thai"),
        UC_03712_03839( 3712,  3839, "Lao"),
        UC_03840_04095( 3840,  4095, "Tibetan"),
        UC_04096_04255( 4096,  4255, "Myanmar"),
        UC_04256_04351( 4256,  4351, "Georgian"),
        UC_04352_04607( 4352,  4607, "Hangul Jamo"),
        UC_04608_04991( 4608,  4991, "Ethiopic"),
        UC_05024_05119( 5024,  5119, "Cherokee"),
        UC_05120_05759( 5120,  5759, "Unified Canadian aboriginal Syllabics"),
        UC_05760_05791( 5760,  5791, "Ogham"),
        UC_05792_05887( 5792,  5887, "Runic"),
        UC_06016_06143( 6016,  6143, "Khmer"),
        UC_06144_06319( 6144,  6319, "Mongolian"),
        UC_07680_07935( 7680,  7935, "Latin Extended Additional"),
        UC_07936_08191( 7936,  8191, "Greek Extended"),
        UC_08192_08303( 8192,  8303, "General Punctuation"),
        UC_08304_08351( 8304,  8351, "Superscripts and Subscripts"),
        UC_08352_08399( 8352,  8399, "Currency Symbols"),
        UC_08400_08447( 8400,  8447, "Combinig Marks for Symbols"),
        UC_08448_08527( 8448,  8527, "Letterlike Symbols"),
        UC_08528_08591( 8528,  8591, "Number Forms"),
        UC_08592_08703( 8592,  8703, "Arrows"),
        UC_08704_08959( 8704,  8959, "Mathematical Operators"),
        UC_08960_09215( 8960,  9215, "Miscellaneous Technical"),
        UC_09216_09279( 9216,  9279, "Contorl Pictures"),
        UC_09280_09311( 9280,  9311, "Optical Character Recognition"),
        UC_09312_09471( 9312,  9471, "Enclosed Alphanumerics"),
        UC_09472_09599( 9472,  9599, "Box Drawing"),
        UC_09600_09631( 9600,  9631, "Block Elements"),
        UC_09632_09727( 9632,  9727, "Geometric Shapes"),
        UC_09728_09983( 9728,  9983, "Miscellaneous Symbols"),
        UC_09984_10175( 9984, 10175, "Dingbats"),
        UC_10240_10495(10240, 10495, "Braille Patterns"),
        UC_11904_12031(11904, 12031, "CJK Radicals Supplement"),
        UC_12032_12255(12032, 12255, "Kangxi Radicals"),
        UC_12272_12287(12272, 12287, "Ideographic Description Characters"),
        UC_12288_12351(12288, 12351, "CJK Symbols and Punctuation"),
        UC_12352_12447(12352, 12447, "Hiragana"),
        UC_12448_12543(12448, 12543, "Katakana"),
        UC_12544_12591(12544, 12591, "Bopomofo"),
        UC_12592_12687(12592, 12687, "Hangul Compatibility Jamo"),
        UC_12688_12703(12688, 12703, "Kanbun"),
        UC_12704_12735(12704, 12735, "Bopomofo Extended"),
        UC_12800_13055(12800, 13055, "Enclosed CJK Letters and Months"),
        UC_13056_13311(13056, 13311, "CJK Compatiability"),
        UC_13312_19893(13312, 19893, "CJK Unified Ideographs Extension A"),
        UC_19968_40959(19968, 40959, "CJK Unified Ideogrphs"),
        UC_40960_42127(40960, 42127, "Yi Syllables"),
        UC_42128_42191(42128, 42191, "Yi Radicals"),
        UC_44032_55203(44032, 55203, "Hangul Syllables"),
        UC_55296_56191(55296, 56191, "High Surrogates"),
        UC_56192_56319(56192, 56319, "High Private Use Surrogates"),
        UC_56320_57343(56320, 57343, "Low Surrogates"),
        UC_57344_63743(57344, 63743, "Private Use"),
        UC_63744_64255(63744, 64255, "CJK Compatibility Ideographs"),
        UC_64256_64335(64256, 64335, "Alphabetic Presentation Forms"),
        UC_64336_65023(64336, 65023, "Arabic Presentation Forms A"),
        UC_65056_65071(65056, 65071, "Combining Half Marks"),
        UC_65072_65103(65072, 65103, "CJK Compatibility Forms"),
        UC_65104_65135(65104, 65135, "Small Form Variants"),
        UC_65136_65278(65136, 65278, "Arabic Presentation Forms B"),
        UC_65279_65279(65279, 65279, "Specials 1"),
        UC_65280_65519(65280, 65519, "Halfwidth and fullwidth Forms"),
        UC_65520_65533(65520, 65533, "Specials 2"),;

        private final String name;
        private final int    beg;
        private final int    end;

        Block(int beg, int end, String name) {
            this.name=name;
            //this.hex1 = hex1;
            //this.hex2 = hex2;
            this.beg=beg;
            this.end=end;
        }
    }

}
