/**
 *
 */
package net.varkhan.base.containers.set;

import java.io.NotSerializableException;
import java.util.Random;


/**
 * @author varkhan
 * @date May 21, 2009
 * @time 12:13:22 AM
 */
public class ArrayOpenHashIndexedSetTest extends AbstractIndexedSetTest {
    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestAdd(rand, vals, iset, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestHas(rand, vals, iset, 0);
    }

    public void testIdx() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestIdx(rand, vals, iset, 0);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestGet(rand, vals, iset, 0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestDel(rand, vals, iset, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestClear(rand, vals, iset, 0);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestIndexes(rand, vals, iset, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestIterate(rand, vals, iset, 0);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestVisit(rand, vals, iset, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        try { featureTestSerialize(rand, vals, iset, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        IndexedSet<String> iset2 = new ArrayOpenHashIndexedSet<String>();
        featureTestEquals(rand, vals, iset, iset2, 0);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        ArrayOpenHashIndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestClone(rand, vals, iset, 0);
    }

    public void testString() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        ArrayOpenHashIndexedSet<String> iset = new ArrayOpenHashIndexedSet<String>();
        featureTestString(rand, vals, iset, 0);
    }

}
