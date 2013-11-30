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
public class ABlockOpenHashIndexedSetTest extends AbstractIndexedSetTest {
    long baseseed=1234567890987654321L;


    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestAdd(rand, vals, iset, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestHas(rand, vals, iset, 0);
    }

    public void testIdx() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestIdx(rand, vals, iset, 0);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestGet(rand, vals, iset, 0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestDel(rand, vals, iset, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestClear(rand, vals, iset, 0);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestIndexes(rand, vals, iset, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestIterate(rand, vals, iset, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        try { featureTestSerialize(rand, vals, iset, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        IndexedSet<String> iset2 = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestEquals(rand, vals, iset, iset2, 0);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        AbstractBlockOpenHashIndexedSet<String> iset = new AbstractBlockOpenHashIndexedSet<String>() {
            public static final long serialVersionUID=1L;
        };
        featureTestClone(rand, vals, iset, 0);
    }

}
