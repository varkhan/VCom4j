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
public class BloomSetTest extends AbstractSetTest {
    long baseseed=1234567890987654321L;

    public void testAdd() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BloomSet<String>();
        featureTestAdd(rand, vals, set, 0);
    }

    public void testHas() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BloomSet<String>();
        featureTestHas(rand, vals, set, 0);
    }

    public void testClear() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BloomSet<String>();
        featureTestClear(rand, vals, set, 0);
    }

    public void testSerialize() throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=genKeyStrings(rand, 10000, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        Set<String> set = new BloomSet<String>();
        try { featureTestSerialize(rand, vals, set, 0); } catch(NotSerializableException e) { /* ignore */ }
    }



}
