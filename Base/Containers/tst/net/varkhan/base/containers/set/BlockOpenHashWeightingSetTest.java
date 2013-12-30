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
public class BlockOpenHashWeightingSetTest extends AbstractWeightingSetTest {

    long baseseed=3553712899943009546L;

    public void testSize() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestSize(rand, keys, vals, ilst);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestClear(rand, keys, vals, ilst);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestHas(rand, keys, vals, ilst);
    }

    public void testWeight() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestWeight(rand, keys, vals, ilst);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestDel(rand, keys, vals, ilst);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestVisit(rand, keys, vals, ilst);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 100000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 100000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestIterator(rand, keys, vals, ilst);
    }

    public void testIString() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=genKeyStrings(rand, 10000, 10, 50, keychars);
        double[] vals=genValWeights(rand, 10000, 0, 10);
        WeightingSet<String> ilst = new BlockOpenHashWeightingSet<String>();
        featureTestString(rand, keys, vals, ilst);
    }

}
