package net.varkhan.base.conversion.type;

import junit.framework.TestCase;

import static net.varkhan.base.conversion.type.QFloat.byte2floatBits;
import static net.varkhan.base.conversion.type.QFloat.float2byteBits;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 6:52 PM
 */
public class QFloatTest extends TestCase {

    public void testByte2qfloatBits() throws Exception {
        for(int i=0; i<256; i++) {
            System.out.println(String.format("%05.4f",byte2floatBits((byte)i))+" "+String.format("%8.8s",Integer.toBinaryString(i)).replace(' ','0'));
            assertEquals(String.format("%8.8s",Integer.toBinaryString(i)).replace(' ','0'),i,0xFF&float2byteBits(byte2floatBits((byte)i)));
        }
    }

    public void testQfloat2byteBits() throws Exception {
        for(float d=0;d<1;d+=0.01){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b))+" "+(d-byte2floatBits(b))+" "+d);
            assertEquals(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0'),d,byte2floatBits(b),d);
        }
        for(float d=1;d<10;d+=0.1){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b))+" "+(d-byte2floatBits(b))+" "+(d+1)/32);
            assertEquals(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0'),d,byte2floatBits(b),(d+1)/32);
        }
        for(float d=10;d<50;d+=0.5){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b))+" "+(d-byte2floatBits(b))+" "+(d+1)/64);
            assertEquals(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0'),d,byte2floatBits(b),d/64);
        }
        for(float d=50;d<100;d+=1){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b))+" "+(d-byte2floatBits(b))+" "+(d+1)/64);
            assertEquals(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0'),d,byte2floatBits(b),d/64);
        }
        for(float d=100;d<490;d+=1){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b))+" "+(d-byte2floatBits(b))+" "+(d+1)/64);
            assertEquals(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0'),d,byte2floatBits(b),d/64);
        }
        for(float d=490;d<=QFloat.MAX_VALUE;d+=0.5){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b))+" "+(d-byte2floatBits(b))+" "+(d+1)/64);
            assertEquals(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0'),d,byte2floatBits(b),d/64);
        }

    }

    public void XtestPrint() {
        for(float d=0;d<1;d+=0.01){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b)));
        }
        for(float d=1;d<10;d+=0.1){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b)));
        }
        for(float d=10;d<50;d+=0.5){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b)));
        }
        for(float d=50;d<100;d+=1){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b)));
        }
        for(float d=100;d<490;d+=1){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b)));
        }
        for(float d=490;d<=520;d+=0.5){
            final byte b = float2byteBits(d);
            System.out.println(String.format("%05.4f",d)+" "+String.format("%8.8s",Integer.toBinaryString(b&0xFF)).replace(' ','0')+" "+ java.lang.Float.toString(byte2floatBits(b)));
        }

    }

}
