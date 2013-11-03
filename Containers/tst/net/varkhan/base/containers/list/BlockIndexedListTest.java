package net.varkhan.base.containers.list;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/2/13
 * @time 3:47 PM
 */
public class BlockIndexedListTest extends AbstractIndexedListTest {

    long baseseed=3553712899943009546L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestAdd(rand, vals, ilst);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestHas(rand, vals, ilst);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestGet(rand, vals, ilst);
    }

    public void testSet() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestSet(rand, vals, ilst);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestDel(rand, vals, ilst);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestClear(rand, vals, ilst);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestIndexes(rand, vals, ilst);
    }

    public void testIterateIndexes() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestIterateIndexes(rand, vals, ilst);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestIterator(rand, vals, ilst);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestVisit(rand, vals, ilst);
    }

    public void testVisitIndexed() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestVisitIndexed(rand, vals, ilst);
    }

    public void testIterateIndexArray() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestIterateIndexArray(rand, vals, ilst);
    }

    public void testIterateIndexIterator() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestIterateIndexIterator(rand, vals, ilst);
    }

    public void testIterateIndex() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestIterateIndex(rand, vals, ilst);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new BlockIndexedList<Integer>();
        featureTestSerialize(rand, vals, ilst);
    }
}
