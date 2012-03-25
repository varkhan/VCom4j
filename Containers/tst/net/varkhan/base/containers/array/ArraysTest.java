package net.varkhan.base.containers.array;

import junit.framework.TestCase;

import java.sql.Array;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class ArraysTest extends TestCase {

    public void testIsMember() throws Exception {
        assertTrue("isMember(null,1,2,3,null)",Arrays.isMember(null,1,2,3,null));
        assertTrue("isMember(null,null,2,3,4)", Arrays.isMember(null, null, 2, 3, 4));
        assertFalse("isMember(null,1,2,3,4)", Arrays.isMember(null, 1, 2, 3, 4));
        assertTrue("isMember(1,1,2,3,null)", Arrays.isMember(1, 1, 2, 3, null));
        assertFalse("isMember(4,1,2,3,null)", Arrays.isMember(4, 1, 2, 3, null));
        assertTrue("isMember(4,1,2,3,4)", Arrays.isMember(4, 1, 2, 3, 4));
    }

    public void testEquals() throws Exception {
        assertTrue("equals([1,2,3,\"f\"],[1,2,3,\"f\"])",Arrays.equals(new Object[]{1, 2, 3, "f"}, new Object[]{1, 2, 3, "f"}));
        assertFalse("equals([1,2,3,\"f\"],[5,2,3,\"f\"])", Arrays.equals(new Object[] { 1, 2, 3, "f" }, new Object[] { 5, 2, 3, "f" }));
        assertFalse("equals([1,2,3,\"f\"],[1,2,3,null])", Arrays.equals(new Object[] { 1, 2, 3, "f" }, new Object[] { 5, 2, 3, null }));
        assertFalse("equals([1,2,3,\"f\"],[1,2,3,\"f\",10])", Arrays.equals(new Object[] { 1, 2, 3, "f" }, new Object[] { 5, 2, 3, "f", 10 }));
    }

    public void testIndexOf() throws Exception {

    }

    public void testSearchHeap() throws Exception {

    }

    public void testInsertHeap() throws Exception {

    }

    public void testAppend() throws Exception {

    }

    public void testPrepend() throws Exception {

    }

    public void testConcat() throws Exception {

    }

    public void testSubarray() throws Exception {
        assertTrue("subarray([1,2,3,4,5,6],2,3)", Arrays.equals(new Object[]{3}, Arrays.subarray(new Object[]{1,2,3,4,5,6}, 2,3)));
        assertTrue("subarray([1,2,3,4,5,6],0,6)", Arrays.equals(new Object[]{1,2,3,4,5,6}, Arrays.subarray(new Object[]{1,2,3,4,5,6}, 0,6)));
        assertTrue("subarray([1,2,3,4,5,6],1,1)", Arrays.equals(new Object[]{}, Arrays.subarray(new Object[]{1,2,3,4,5,6}, 1,1)));
        assertTrue("subarray([1,2,3,4,5,6],6,6)", Arrays.equals(new Object[]{}, Arrays.subarray(new Object[]{1,2,3,4,5,6}, 6,6)));
    }

    public void testContainer() throws Exception {

    }

    public void testMapping() throws Exception {

    }
}
