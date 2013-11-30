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
public class BlockOpenHashCountingSetTest extends AbstractCountingSetTest {

    long baseseed=3553712899943009546L;

    public void testSize() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestSize(rand, keys, vals, ilst, true);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestClear(rand, keys, vals, ilst);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestHas(rand, keys, vals, ilst, true);
    }

    public void testCount() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestCount(rand, keys, vals, ilst, true);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestDel(rand, keys, vals, ilst, true);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestVisit(rand, keys, vals, ilst);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        long[] vals=genValCounts(rand, 100000, 0, 10);
        CountingSet<String> ilst = new BlockOpenHashCountingSet<String>();
        featureTestIterator(rand, keys, vals, ilst);
    }

}
