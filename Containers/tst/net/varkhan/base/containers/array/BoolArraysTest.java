package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class BoolArraysTest extends TestCase {

    public static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+BoolArrays.join(",",expected)+"];\n   actual: ["+BoolArrays.join(",",actual)+"]");
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", BoolArrays.equals(new boolean[] { }, new boolean[] { }));
        assertTrue("equals([true, true, false],[true, true, false])", BoolArrays.equals(new boolean[] { true, true, false }, new boolean[] { true, true, false }));
        assertFalse("equals([true, true, false],[true, false, false])", BoolArrays.equals(new boolean[] { true, true, false }, new boolean[] { true, false, false }));
        assertFalse("equals([true, true, false],[true, true, false, true ])", BoolArrays.equals(new boolean[] { true, true, false }, new boolean[] { true, true, false, true }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(true, true, true, false)", 0, BoolArrays.indexOf(true, true, true, false));
        assertEquals("indexOf(false, true, true, false)", 2, BoolArrays.indexOf(false, true, true, false));
        assertEquals("indexOf(false, true, true, true)", -1, BoolArrays.indexOf(false, true, true, true));
    }

    public void testIndexOfArray() throws Exception {
        assertEquals("indexOf([], 0, [true, true, false])", 0, BoolArrays.indexOf(new boolean[]{}, 0, new boolean[]{true, true, false}));
        assertEquals("indexOf([true], 0, [true, true, false])", 0, BoolArrays.indexOf(new boolean[]{true}, 0, new boolean[]{true, true, false}));
        assertEquals("indexOf([false, true], 0, [true, false, true])", 1, BoolArrays.indexOf(new boolean[]{false, true}, 0, new boolean[]{true, false, true}));
        assertEquals("indexOf([false, true], 2, [true, false, true])", -1, BoolArrays.indexOf(new boolean[]{false, true}, 2, new boolean[]{true, false, true}));
        assertEquals("indexOf([false, true], 0, [true, true, false])", -1, BoolArrays.indexOf(new boolean[]{false, true}, 0, new boolean[]{true, true, false}));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new boolean[]{true, true, false, false, true, true, false},
                          BoolArrays.append(new boolean[]{true, true, false}, false, true, true, false));
        assertArrayEquals("",
                          new boolean[]{true, true, false, false, true, true, false},
                          BoolArrays.append(new boolean[]{}, true, true, false, false, true, true, false));
        assertArrayEquals("",
                          new boolean[]{},
                          BoolArrays.append(new boolean[]{}));
        assertArrayEquals("",
                          new boolean[]{true, true, false},
                          BoolArrays.append(new boolean[]{true, true, false}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new boolean[]{false, true, true, false, true, true, false},
                          BoolArrays.prepend(new boolean[] { true, true, false }, false, true, true, false));
        assertArrayEquals("",
                          new boolean[]{true, true, false, false, true, true, false},
                          BoolArrays.prepend(new boolean[] { }, true, true, false, false, true, true, false));
        assertArrayEquals("",
                          new boolean[]{},
                          BoolArrays.prepend(new boolean[]{}));
        assertArrayEquals("",
                          new boolean[]{true, true, false},
                          BoolArrays.prepend(new boolean[]{true, true, false}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new boolean[]{false, true, true, false, true, true, false},
                          BoolArrays.concat(new boolean[]{false, true, true}, new boolean[]{false, true, true, false}));
        assertArrayEquals("",
                          new boolean[]{false, true, true, false, true, true, false},
                          BoolArrays.concat(new boolean[]{}, new boolean[]{false, true, true, false, true, true, false}));
        assertArrayEquals("",
                          new boolean[]{},
                          BoolArrays.concat(new boolean[]{}));
        assertArrayEquals("",
                          new boolean[]{false, true, true, false, true, true, false},
                          BoolArrays.concat(new boolean[]{false, true, true}, new boolean[]{false, true}, new boolean[]{true, false}));
        assertArrayEquals("",
                          new boolean[]{false, true, true, false, true, true, false},
                          BoolArrays.concat(new boolean[]{false, true, true}, new boolean[]{}, new boolean[]{false, true}, new boolean[]{true, false}));
        assertArrayEquals("",
                          new boolean[]{false, true, true, false, true, true, false},
                          BoolArrays.concat(new boolean[]{}, new boolean[]{false, true, true}, new boolean[]{false, true}, new boolean[]{true, false}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new boolean[] { }, BoolArrays.subarray(new boolean[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new boolean[] { }, BoolArrays.subarray(new boolean[] { false, true, true, false, true, true }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new boolean[] { }, BoolArrays.subarray(new boolean[] { false, true, true, false, true, true }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new boolean[] { true, true, false }, BoolArrays.subarray(new boolean[] { false, true, true, false, true, true }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new boolean[] { true }, BoolArrays.subarray(new boolean[] { false, true, true, false, true, true }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new boolean[] { false, true, true, false, true, true }, BoolArrays.subarray(new boolean[] { false, true, true, false, true, true }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new boolean[] { }, BoolArrays.subarray(new boolean[] { false, true, true, false, true, true}, 1, 1));
        try {
            BoolArrays.subarray(new boolean[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            BoolArrays.subarray(new boolean[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            BoolArrays.subarray(new boolean[] { false, true, true, false, true, true }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public void testStrgBool() throws Exception {
        assertEquals("[0|]",BoolArrays.toString(new boolean[]{}));
        assertEquals("[1|0]",BoolArrays.toString(new boolean[]{false}));
        assertEquals("[1|1]",BoolArrays.toString(new boolean[]{true}));
        assertEquals("[5|0,1,1,0,1]",BoolArrays.toString(new boolean[]{false,true,true,false,true}));
        assertEquals("[5|0,1,1,0,1]",BoolArrays.toString(new StringBuilder(),new boolean[]{false,true,true,false,true}).toString());
        assertEquals("[5|0,1,1,0,1]",BoolArrays.toString(new StringBuffer(),new boolean[]{false,true,true,false,true}).toString());
    }

    public void testJoinBool() throws Exception {
        assertEquals("",BoolArrays.join(":",new boolean[]{}));
        assertEquals("0",BoolArrays.join(":",new boolean[]{false}));
        assertEquals("1",BoolArrays.join(":",new boolean[]{true}));
        assertEquals("0:1:1:0:1",BoolArrays.join(":",new boolean[]{false,true,true,false,true}));
        assertEquals("0:1:1:0:1",BoolArrays.join(new StringBuilder(),":",new boolean[]{false,true,true,false,true}).toString());
        assertEquals("0:1:1:0:1",BoolArrays.join(new StringBuffer(),":",new boolean[]{false,true,true,false,true}).toString());
        assertEquals("01101",BoolArrays.join(null,new boolean[]{false,true,true,false,true}));
        assertEquals("01101",BoolArrays.join(new StringBuilder(),null,new boolean[]{false,true,true,false,true}).toString());
        assertEquals("01101",BoolArrays.join(new StringBuffer(),null,new boolean[]{false,true,true,false,true}).toString());
    }

}
