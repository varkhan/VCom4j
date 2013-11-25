package net.varkhan.base.containers.list;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/3/13
 * @time 12:54 PM
 */
public class BlockIndexedIntListTest extends AbstractIndexedIntListTest {

    long baseseed=3553712899943009546L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestAdd(rand, vals, ilst, defVal);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestHas(rand, vals, ilst, defVal);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestGet(rand, vals, ilst, defVal);
    }

    public void testSet() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestSet(rand, vals, ilst, defVal);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestDel(rand, vals, ilst, defVal);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestClear(rand, vals, ilst, defVal);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestIndexes(rand, vals, ilst, defVal);
    }

    public void testIterateIndexes() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndexes(rand, vals, ilst, defVal);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestIterator(rand, vals, ilst, defVal);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestVisit(rand, vals, ilst, defVal);
    }

    public void testVisitIndexed() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestVisitIndexed(rand, vals, ilst, defVal);
    }

    public void testIterateIndexArray() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndexArray(rand, vals, ilst, defVal);
    }

    public void testIterateIndexIterator() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndexIterator(rand, vals, ilst, defVal);
    }

    public void testIterateIndex() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndex(rand, vals, ilst, defVal);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        int defVal = rand.nextInt();
        int[] vals=genIntegerList(rand, 1000000, 0.9, defVal);
        IndexedIntList ilst = new BlockIndexedIntList();
        ilst.setDefaultValue(defVal);
        featureTestSerialize(rand, vals, ilst, defVal);
    }

}