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
public class BlockOpenHashLong2FloatMapTest extends AbstractLong2FloatMapTest {

    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestAdd(rand, keys, vals, map, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestHas(rand, keys, vals, map, 0);
    }

    public void testGet() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestGet(rand,keys, vals,map,0);
    }

    public void testDel() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestDel(rand, keys, vals, map, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestClear(rand, keys, vals, map, 0);
    }

    public void testIterate() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestIterate(rand, keys, vals, map, 0);
    }

    public void testVisit() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestVisit(rand, keys, vals, map, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        try { featureTestSerialize(rand, keys, vals, map, 0); } catch(NotSerializableException e) { /* ignore */ }
    }

    public void testEquals() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        Long2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestEquals(rand, keys, vals, map, new BlockOpenHashLong2FloatMap(), 0);
    }

    public void testClone() throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=genKeyLongs(rand, 100000);
        float[] vals=genValueFloats(rand, 100000, .1, 1f, 10000f);
        BlockOpenHashLong2FloatMap map = new BlockOpenHashLong2FloatMap();
        featureTestClone(rand, keys, vals, map, 0);
    }

}
