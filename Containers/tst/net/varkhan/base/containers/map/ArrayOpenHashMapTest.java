/**
 *
 */
package net.varkhan.base.containers.map;

import java.io.NotSerializableException;
import java.util.Random;


/**
 * @author varkhan
 * @date Feb 10, 2010
 * @time 6:24:10 AM
 */
public class ArrayOpenHashMapTest extends AbstractMapTest {
    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestAdd(rand, keys, vals, map, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestHas(rand, keys, vals, map, 0);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestGet(rand,keys, vals,map, 0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestDel(rand, keys, vals, map, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestClear(rand, keys, vals, map, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestIterate(rand, keys, vals, map, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        try { featureTestSerialize(rand, keys, vals, map, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Map<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestEquals(rand, keys, vals, map, new ArrayOpenHashMap<String,String>(), 0);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, 100000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        ArrayOpenHashMap<String,String> map = new ArrayOpenHashMap<String,String>();
        featureTestClone(rand, keys, vals, map, 0);
    }

}
