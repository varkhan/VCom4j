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
public class ArrayIndexedListTest extends AbstractIndexedListTest {

    long baseseed=3553712899943009546L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestAdd(rand, vals, ilst);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestHas(rand, vals, ilst);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestGet(rand, vals, ilst);
    }

    public void testSet() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestSet(rand, vals, ilst);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestDel(rand, vals, ilst);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestClear(rand, vals, ilst);
    }

    public void testFree() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestFree(rand, vals, ilst);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestIndexes(rand, vals, ilst);
    }

    public void testIterateIndexes() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestIterateIndexes(rand, vals, ilst);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestIterator(rand, vals, ilst);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestVisit(rand, vals, ilst);
    }

    public void testVisitIndexed() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestVisitIndexed(rand, vals, ilst);
    }

    public void testIterateIndexArray() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestIterateIndexArray(rand, vals, ilst);
    }

    public void testIterateIndexIterator() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestIterateIndexIterator(rand, vals, ilst);
    }

    public void testIterateIndex() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestIterateIndex(rand, vals, ilst);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestSerialize(rand, vals, ilst);
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        IndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        IndexedList<Integer> ilst2 = new ArrayIndexedList<Integer>();
        featureTestEquals(rand, vals, ilst, ilst2);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        ArrayIndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestClone(rand, vals, ilst);
    }

    public void testString() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        ArrayIndexedList<Integer> ilst = new ArrayIndexedList<Integer>();
        featureTestString(rand, vals, ilst);
    }

}
