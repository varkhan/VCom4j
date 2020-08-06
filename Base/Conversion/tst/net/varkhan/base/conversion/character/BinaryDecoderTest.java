package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 4:23 PM
 */
public class BinaryDecoderTest extends TestCase {
    public void testDecode() throws Exception {
        verifyDecode(BinaryDecoder.BINARY,
                "00000000"+"00000001"+"00000010"+"00000011"+"00000100"+"11111111",
                new byte[] {0,1,2,3,4, -1});
        verifyDecode(BinaryDecoder.BINARY,
                "00101010"+"11010101"+"11010110",
                new byte[] {42,~42,-42});
        verifyDecode(BinaryDecoder.OCTAL, "000"+"001"+"002"+"003"+"004"+"046"+"257"+"313",  new byte[] {0,1,2,3,4,42,-42,-1});
        verifyDecode(BinaryDecoder.HEX_LOWER, "00"+"01"+"02"+"03"+"04"+"2a"+"d6"+"ff",  new byte[] {0,1,2,3,4,42,-42,-1});
        verifyDecode(BinaryDecoder.HEX_UPPER, "00"+"01"+"02"+"03"+"04"+"2A"+"D6"+"FF",  new byte[] {0,1,2,3,4,42,-42,-1});
        verifyDecode(new BinaryDecoder<>("0:1:2:3:4:5:6:7:8:9"), "000"+"001"+"002"+"003"+"004"+"042"+"214"+"255", new byte[] {0,1,2,3,4,42,-42,-1});
    }

    public void verifyDecode(BinaryDecoder<Object> dec, String expected, byte[] buf) {
        assertEquals(expected,dec.decode(buf,0,buf.length,null));
        assertEquals(expected,dec.decode(new ByteArrayInputStream(buf),null));
        assertEquals(expected,dec.decode(ByteBuffer.wrap(buf),null));
    }

}
