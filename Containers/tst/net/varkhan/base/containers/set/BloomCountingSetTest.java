package net.varkhan.base.containers.set;


import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/28/13
 * @time 12:50 PM
 */
public class BloomCountingSetTest extends AbstractCountingSetTest {

    long baseseed=3553712899943009546L;

    public void testSize() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BloomCountingSet<String>(10,200000);
        featureTestSize(rand, keys, vals, ilst, false);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BloomCountingSet<String>(10,200000);
        featureTestClear(rand, keys, vals, ilst);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100/*000*/, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100/*000*/, 0, 10);
        CountingSet<String> ilst = new BloomCountingSet<String>(10,200000);
        featureTestHas(rand, keys, vals, ilst, false);
    }

    public void testCount() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BloomCountingSet<String>(10,200000);
        featureTestCount(rand, keys, vals, ilst, false);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BloomCountingSet<String>(10,200000);
        featureTestDel(rand, keys, vals, ilst, false);
    }

}
