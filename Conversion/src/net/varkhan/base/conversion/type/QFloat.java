package net.varkhan.base.conversion.type;

/**
 * <b>Quarter-precision float utilities</b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 6:50 PM
 */
public class QFloat {

    public static final float MIN_VALUE =   0;
    public static final float MAX_VALUE = 504;

    private static final float[] conv;
    static {
        conv = new float[0x100];
        conv[0x00] = MIN_VALUE;
        for(int i=1; i<0xFF; i++) {
            int e = i&0xE0;
            int m = i&0x1F;
            if(e!=0) {
                // Normal values need an implicit mantissa offset
                m |= 0x20;
                e = (e >>> 5)-1;
            }
            conv[i] = Math.scalb(m,e-3);
        }
        conv[0xFF] = MAX_VALUE;
    }

    public static float byte2floatBits(byte b) {
        return conv[b&0xFF];
    }

//    public static float byte2floatBits(byte b) {
//        if(b==0x00) return MIN_VALUE;
//        if(b==0xFF) return MAX_VALUE;
//        int e = b&0xE0;
//        int m = b&0x1F;
//        if(e!=0) {
//            // Normal values need an implicit mantissa offset
//            m |= 0x20;
//            e = (e >>> 5)-1;
//        }
//        return Math.scalb(m,e-3);
//    }
//
    public static byte float2byteBits(float f) {
        if(f<=MIN_VALUE) return (byte)0x00;
        if(f>=MAX_VALUE) return (byte)0xFF;
        int e = Math.getExponent(f)-2;
        if(e<0) e=0;
        if(e>7) e=7;
        // Math.scalb(f,3-e) is supposed to be within [0,64[, but we can end up with m==64 as a result of rounding
        int m = (int)(0.5+Math.scalb(f,3-e));
        if(m>=0x40) { e += 2; m >>=1; }
        else if(m>=0x20) { e ++; }
        return (byte) ((m&0x1F) | (e<<5));
    }

}
