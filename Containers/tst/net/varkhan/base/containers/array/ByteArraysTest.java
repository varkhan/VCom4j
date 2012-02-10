/**
 *
 */
package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * @author varkhan
 * @date Feb 26, 2010
 * @time 7:12:39 PM
 */
public class ByteArraysTest extends TestCase {

    public void testVariadic() {
        long[] vals={
                0L,
                1L,
                2L,
                127L,
                128L,
                129L,
                128L*128L-1,
                128L*128L,
                128L*128L+1,
                -1L,
                1234567890123456789L,
        };
        for(int i=0;i<vals.length;i++) {
            long val=vals[i];
            int len=ByteArrays.lenVariadic(val);
            byte[] a=new byte[20];
            ByteArrays.setVariadic(a, 0, val);
            System.out.println(val+" "+len+" "+StringArrays.toString(a));
            assertEquals("setVariadic().len", 0, a[len]);
            assertTrue("setVariadic()[len]", a[len-1]>=0);
            assertTrue("setVariadic()[0]", len==1||a[0]<0);
            long var=ByteArrays.getVariadic(a, 0);
            assertEquals("getVariadic()", val, var);
        }
    }


}
