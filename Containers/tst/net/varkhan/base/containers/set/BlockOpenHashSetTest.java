/**
 *
 */
package net.varkhan.base.containers.set;

import java.io.NotSerializableException;
import java.util.Random;


/**
 * @author varkhan
 * @date Feb 10, 2010
 * @time 6:24:10 AM
 */
public class BlockOpenHashSetTest extends AbstractSetTest {
    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        featureTestAdd(rand, vals, set, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        featureTestHas(rand, vals, set, 0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        featureTestDel(rand, vals, set, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        featureTestClear(rand, vals, set, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        featureTestIterate(rand, vals, set, 0);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        featureTestVisit(rand, vals, set, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        try { featureTestSerialize(rand, vals, set, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BlockOpenHashSet<String>();
        Set<String> set2 = new BlockOpenHashSet<String>();
        featureTestEquals(rand, vals, set, set2, 0);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        BlockOpenHashSet<String> set = new BlockOpenHashSet<String>();
        featureTestClone(rand, vals, set, 0);
    }

}
