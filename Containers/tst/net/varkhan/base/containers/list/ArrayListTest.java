package net.varkhan.base.containers.list;

import junit.framework.TestCase;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/2/13
 * @time 3:47 PM
 */
public class ArrayListTest extends AbstractListTest {

    long baseseed=3553712899943009546L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestAdd(rand, vals, ilst);
    }

    public void testSet() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestSet(rand, vals, ilst);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestDel(rand, vals, ilst);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestClear(rand, vals, ilst);
    }

    public void testIterator() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestIterator(rand, vals, ilst);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestVisit(rand, vals, ilst);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        Integer[] vals=genIntegerList(rand, 1000000, 0.9);
        List<Integer> ilst = new ArrayList<Integer>();
        featureTestSerialize(rand, vals, ilst);
    }

}