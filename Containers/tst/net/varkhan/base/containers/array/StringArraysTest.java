package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/1/13
 * @time 7:57 PM
 */
public class StringArraysTest extends TestCase {

    public void testStrgBool() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new boolean[]{}));
        assertEquals("[1|0]",StringArrays.toString(new boolean[]{false}));
        assertEquals("[1|1]",StringArrays.toString(new boolean[]{true}));
        assertEquals("[5|0,1,1,0,1]",StringArrays.toString(new boolean[]{false,true,true,false,true}));
        assertEquals("[5|0,1,1,0,1]",StringArrays.toString(new StringBuilder(),new boolean[]{false,true,true,false,true}).toString());
        assertEquals("[5|0,1,1,0,1]",StringArrays.toString(new StringBuffer(),new boolean[]{false,true,true,false,true}).toString());
    }

    public void testJoinBool() throws Exception {
        assertEquals("",StringArrays.join(":",new boolean[]{}));
        assertEquals("0",StringArrays.join(":",new boolean[]{false}));
        assertEquals("1",StringArrays.join(":",new boolean[]{true}));
        assertEquals("0:1:1:0:1",StringArrays.join(":",new boolean[]{false,true,true,false,true}));
        assertEquals("0:1:1:0:1",StringArrays.join(new StringBuilder(),":",new boolean[]{false,true,true,false,true}).toString());
        assertEquals("0:1:1:0:1",StringArrays.join(new StringBuffer(),":",new boolean[]{false,true,true,false,true}).toString());
    }


    public void testStrgByte() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new byte[]{}));
        assertEquals("[1|00]",StringArrays.toString(new byte[]{0}));
        assertEquals("[1|01]",StringArrays.toString(new byte[]{1}));
        assertEquals("[1|F7]",StringArrays.toString(new byte[]{(byte)0xF7}));
        assertEquals("[5|00,01,02,F3,04]",StringArrays.toString(new byte[]{0,1,2,(byte)0xF3,4}));
        assertEquals("[5|00,01,02,F3,04]",StringArrays.toString(new StringBuilder(),new byte[]{0,1,2,(byte)0xF3,4}).toString());
        assertEquals("[5|00,01,02,F3,04]",StringArrays.toString(new StringBuffer(),new byte[]{0,1,2,(byte)0xF3,4}).toString());
    }

    public void testJoinByte() throws Exception {
        assertEquals("",StringArrays.join(":",new byte[]{}));
        assertEquals("00",StringArrays.join(":",new byte[]{0}));
        assertEquals("01",StringArrays.join(":",new byte[]{1}));
        assertEquals("F7",StringArrays.join(":",new byte[]{(byte)0xF7}));
        assertEquals("00:01:02:F3:04",StringArrays.join(":",new byte[]{0,1,2,(byte)0xF3,4}));
        assertEquals("00:01:02:F3:04",StringArrays.join(new StringBuilder(),":",new byte[]{0,1,2,(byte)0xF3,4}).toString());
        assertEquals("00:01:02:F3:04",StringArrays.join(new StringBuffer(),":",new byte[]{0,1,2,(byte)0xF3,4}).toString());
    }


    public void testStrgShort() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new short[]{}));
        assertEquals("[1|0]",StringArrays.toString(new short[]{0}));
        assertEquals("[1|1]",StringArrays.toString(new short[]{1}));
        assertEquals("[1|247]",StringArrays.toString(new short[]{(short)0xF7}));
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new short[]{0,1,2,(short)0xF3,4}));
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new StringBuilder(),new short[]{0,1,2,(short)0xF3,4}).toString());
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new StringBuffer(),new short[]{0,1,2,(short)0xF3,4}).toString());
    }

    public void testJoinShort() throws Exception {
        assertEquals("",StringArrays.join(":",new short[]{}));
        assertEquals("0",StringArrays.join(":",new short[]{0}));
        assertEquals("1",StringArrays.join(":",new short[]{1}));
        assertEquals("247",StringArrays.join(":",new short[]{(short)0xF7}));
        assertEquals("0:1:2:243:4",StringArrays.join(":",new short[]{0,1,2,(short)0xF3,4}));
        assertEquals("0:1:2:243:4",StringArrays.join(new StringBuilder(),":",new short[]{0,1,2,(short)0xF3,4}).toString());
        assertEquals("0:1:2:243:4",StringArrays.join(new StringBuffer(),":",new short[]{0,1,2,(short)0xF3,4}).toString());
    }


    public void testStrgInt() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new int[]{}));
        assertEquals("[1|0]",StringArrays.toString(new int[]{0}));
        assertEquals("[1|1]",StringArrays.toString(new int[]{1}));
        assertEquals("[1|247]",StringArrays.toString(new int[]{0xF7}));
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new int[]{0,1,2,0xF3,4}));
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new StringBuilder(),new int[]{0,1,2,0xF3,4}).toString());
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new StringBuffer(),new int[]{0,1,2,0xF3,4}).toString());
    }

    public void testJoinInt() throws Exception {
        assertEquals("",StringArrays.join(":",new int[]{}));
        assertEquals("0",StringArrays.join(":",new int[]{0}));
        assertEquals("1",StringArrays.join(":",new int[]{1}));
        assertEquals("247",StringArrays.join(":",new int[]{0xF7}));
        assertEquals("0:1:2:243:4",StringArrays.join(":",new int[]{0,1,2,0xF3,4}));
        assertEquals("0:1:2:243:4",StringArrays.join(new StringBuilder(),":",new int[]{0,1,2,0xF3,4}).toString());
        assertEquals("0:1:2:243:4",StringArrays.join(new StringBuffer(),":",new int[]{0,1,2,0xF3,4}).toString());
    }


    public void testStrgLong() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new long[]{}));
        assertEquals("[1|0]",StringArrays.toString(new long[]{0}));
        assertEquals("[1|1]",StringArrays.toString(new long[]{1}));
        assertEquals("[1|247]",StringArrays.toString(new long[]{0xF7}));
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new long[]{0,1,2,0xF3,4}));
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new StringBuilder(),new long[]{0,1,2,0xF3,4}).toString());
        assertEquals("[5|0,1,2,243,4]",StringArrays.toString(new StringBuffer(),new long[]{0,1,2,0xF3,4}).toString());
    }

    public void testJoinLong() throws Exception {
        assertEquals("",StringArrays.join(":",new long[]{}));
        assertEquals("0",StringArrays.join(":",new long[]{0}));
        assertEquals("1",StringArrays.join(":",new long[]{1}));
        assertEquals("247",StringArrays.join(":",new long[]{0xF7}));
        assertEquals("0:1:2:243:4",StringArrays.join(":",new long[]{0,1,2,0xF3,4}));
        assertEquals("0:1:2:243:4",StringArrays.join(new StringBuilder(),":",new long[]{0,1,2,0xF3,4}).toString());
        assertEquals("0:1:2:243:4",StringArrays.join(new StringBuffer(),":",new long[]{0,1,2,0xF3,4}).toString());
    }


    public void testStrgFloat() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new float[]{}));
        assertEquals("[1|0.000000]",StringArrays.toString(new float[]{0}));
        assertEquals("[1|1.000000]",StringArrays.toString(new float[]{1}));
        assertEquals("[1|247.000000]",StringArrays.toString(new float[]{0xF7}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",StringArrays.toString(new float[]{0,1,2.2f,0xF3,4}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",StringArrays.toString(new StringBuilder(),new float[]{0,1,2.2f,0xF3,4}).toString());
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",StringArrays.toString(new StringBuffer(),new float[]{0,1,2.2f,0xF3,4}).toString());
    }

    public void testJoinFloat() throws Exception {
        assertEquals("",StringArrays.join(":",new float[]{}));
        assertEquals("0.000000",StringArrays.join(":",new float[]{0}));
        assertEquals("1.000000",StringArrays.join(":",new float[]{1}));
        assertEquals("247.000000",StringArrays.join(":",new float[]{0xF7}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",StringArrays.join(":",new float[]{0,1,2.2f,0xF3,4}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",StringArrays.join(new StringBuilder(),":",new float[]{0,1,2.2f,0xF3,4}).toString());
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",StringArrays.join(new StringBuffer(),":",new float[]{0,1,2.2f,0xF3,4}).toString());
    }


    public void testStrgDouble() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new double[]{}));
        assertEquals("[1|0.000000]",StringArrays.toString(new double[]{0}));
        assertEquals("[1|1.000000]",StringArrays.toString(new double[]{1}));
        assertEquals("[1|247.000000]",StringArrays.toString(new double[]{0xF7}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",StringArrays.toString(new double[]{0,1,2.2,0xF3,4}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",StringArrays.toString(new StringBuilder(),new double[]{0,1,2.2,0xF3,4}).toString());
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",StringArrays.toString(new StringBuffer(),new double[]{0,1,2.2,0xF3,4}).toString());
    }

    public void testJoinDouble() throws Exception {
        assertEquals("",StringArrays.join(":",new double[]{}));
        assertEquals("0.000000",StringArrays.join(":",new double[]{0}));
        assertEquals("1.000000",StringArrays.join(":",new double[]{1}));
        assertEquals("247.000000",StringArrays.join(":",new double[]{0xF7}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",StringArrays.join(":",new double[]{0,1,2.2,0xF3,4}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",StringArrays.join(new StringBuilder(),":",new double[]{0,1,2.2,0xF3,4}).toString());
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",StringArrays.join(new StringBuffer(),":",new double[]{0,1,2.2,0xF3,4}).toString());
    }


    public void testStrg() throws Exception {
        assertEquals("[0|]",StringArrays.toString(new Object[]{}));
        assertEquals("[1|0]",StringArrays.toString(0));
        assertEquals("[1|1]",StringArrays.toString(1));
        assertEquals("[1|]",StringArrays.toString((Object)null));
        assertEquals("[1|247]",StringArrays.toString(0xF7));
        assertEquals("[1|foo]",StringArrays.toString("foo"));
        assertEquals("[5|0,true,2.2,,foo]",StringArrays.toString(0,true,2.2,null,"foo"));
        assertEquals("[5|0,true,2.2,,foo]",StringArrays.toString(new StringBuilder(),0,true,2.2,null,"foo").toString());
        assertEquals("[5|0,true,2.2,,foo]",StringArrays.toString(new StringBuffer(),0,true,2.2,null,"foo").toString());

    }

    public void testJoin() throws Exception {
        assertEquals("",StringArrays.join(":"));
        assertEquals("0",StringArrays.join(":",0));
        assertEquals("1",StringArrays.join(":",1));
        assertEquals("",StringArrays.join(":",(Object)null));
        assertEquals("247",StringArrays.join(":",0xF7));
        assertEquals("foo",StringArrays.join(":","foo"));
        assertEquals("0:true:2.2::foo",StringArrays.join(":",0,true,2.2,null,"foo"));
        assertEquals("0:true:2.2::foo",StringArrays.join(new StringBuilder(),":",0,true,2.2,null,"foo").toString());
        assertEquals("0:true:2.2::foo",StringArrays.join(new StringBuffer(),":",0,true,2.2,null,"foo").toString());
    }


}
