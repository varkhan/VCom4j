package net.varkhan.base.containers.map;

import java.io.NotSerializableException;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/9/11
 * @time 8:43 PM
 */
public class BlockOpenHashLong2ObjMapTest extends AbstractLong2ObjMapTest {

    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestAdd(rand, keys, vals, map, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestHas(rand, keys, vals, map, 0);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestGet(rand,keys, vals,map,0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestDel(rand, keys, vals, map, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestClear(rand, keys, vals, map, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestIterate(rand, keys, vals, map, 0);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestVisit(rand, keys, vals, map, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        try { featureTestSerialize(rand, keys, vals, map, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Long2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestEquals(rand, keys, vals, map, new BlockOpenHashLong2ObjMap<String>(), 0);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        String[] vals=genValueStrings(rand, 100000, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        BlockOpenHashLong2ObjMap<String> map = new BlockOpenHashLong2ObjMap<String>();
        featureTestClone(rand, keys, vals, map, 0);
    }

}
