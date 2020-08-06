package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 4:23 PM
 */
public class BinaryEncoderTest extends TestCase {
    public void testEncode() throws Exception {
        verifyEncode(BinaryEncoder.BINARY, new byte[] {0,1,2,3,4,-1},"00000000"+"00000001"+"00000010"+"00000011"+"00000100"+"11111111");
        verifyEncode(BinaryEncoder.BINARY, new byte[] {42,~42,-42},"00101010"+"11010101"+"11010110");
        verifyEncode(BinaryEncoder.OCTAL, new byte[] {0,1,2,3,4,42,-42,-1}, "000"+"001"+"002"+"003"+"004"+"046"+"257"+"313");
        verifyEncode(BinaryEncoder.HEX_LOWER, new byte[] {0,1,2,3,4,42,-42,-1}, "00"+"01"+"02"+"03"+"04"+"2a"+"d6"+"ff");
        verifyEncode(BinaryEncoder.HEX_UPPER, new byte[] {0,1,2,3,4,42,-42,-1}, "00"+"01"+"02"+"03"+"04"+"2A"+"D6"+"FF");
        verifyEncode(new BinaryEncoder<>("0:1:2:3:4:5:6:7:8:9"), new byte[] {0,1,2,3,4,42,-42,-1}, "000"+"001"+"002"+"003"+"004"+"042"+"214"+"255");
    }

    public void verifyEncode(BinaryEncoder<Object> enc, byte[] expected, String encode) {
        assertArrayEquals("encode(\""+expected+"\")",expected,enc.encode(encode,null));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals("encode(\""+expected+"\",OutputStream).length()",expected.length, enc.encode(encode, out, null));
        assertArrayEquals("encode(\""+expected+"\",OutputStream)",expected,out.toByteArray());
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals("encode(\""+expected+"\",ByteBuffer).length()",expected.length, enc.encode(encode, buf, null));
        assertArrayEquals("encode(\""+expected+"\",ByteBuffer).array()",expected, Arrays.copyOfRange(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position()));
    }
}
