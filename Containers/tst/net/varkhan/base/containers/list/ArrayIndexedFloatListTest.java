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
public class ArrayIndexedFloatListTest extends AbstractIndexedFloatListTest {

    long baseseed=3553712899943009546L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestAdd(rand, vals, ilst, defVal);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestHas(rand, vals, ilst, defVal);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestGet(rand, vals, ilst, defVal);
    }

    public void testSet() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestSet(rand, vals, ilst, defVal);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestDel(rand, vals, ilst, defVal);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestClear(rand, vals, ilst, defVal);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestIndexes(rand, vals, ilst, defVal);
    }

    public void testIterateIndexes() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndexes(rand, vals, ilst, defVal);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestIterator(rand, vals, ilst, defVal);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestVisit(rand, vals, ilst, defVal);
    }

    public void testVisitIndexed() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestVisitIndexed(rand, vals, ilst, defVal);
    }

    public void testIterateIndexArray() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndexArray(rand, vals, ilst, defVal);
    }

    public void testIterateIndexIterator() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndexIterator(rand, vals, ilst, defVal);
    }

    public void testIterateIndex() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestIterateIndex(rand, vals, ilst, defVal);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        ilst.setDefaultValue(defVal);
        featureTestSerialize(rand, vals, ilst, defVal);
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        IndexedFloatList ilst = new ArrayIndexedFloatList();
        IndexedFloatList ilst2 = new ArrayIndexedFloatList();
        featureTestEquals(rand, vals, ilst, ilst2, defVal);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        float defVal = 0;//rand.nextFloat();
        float[] vals=genFloatList(rand, 1000000, 0.9, defVal);
        ArrayIndexedFloatList ilst = new ArrayIndexedFloatList();
        featureTestClone(rand, vals, ilst, defVal);
    }

}
