/**
 *
 */
package net.varkhan.base.containers.map;

import java.io.NotSerializableException;
import java.util.Random;


/**
 * @author varkhan
 * @date Nov 5, 2009
 * @time 6:09:05 PM
 */
public class BlockOpenHashIndexedMapTest extends AbstractIndexedMapTest {
    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestAdd(rand, keys, vals, imap, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestHas(rand, keys, vals, imap, 0);
    }

    public void testIdx() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestIdx(rand, keys, vals, imap, 0);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestGet(rand, keys, vals, imap, 0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestDel(rand, keys, vals, imap, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestClear(rand, keys, vals, imap, 0);
    }

    public void testIndexes() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestIndexes(rand, keys, vals, imap, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestIterate(rand, keys, vals, imap, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        try { featureTestSerialize(rand, keys, vals, imap, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        IndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestEquals(rand, keys, vals, imap, new BlockOpenHashIndexedMap<String,String>(), 0);
    }

    public void tesClone() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        BlockOpenHashIndexedMap<String,String> imap = new BlockOpenHashIndexedMap<String,String>();
        featureTestClone(rand, keys, vals, imap, 0);
    }

}
