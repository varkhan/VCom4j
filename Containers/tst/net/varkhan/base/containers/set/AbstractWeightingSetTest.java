package net.varkhan.base.containers.set;

import junit.framework.TestCase;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.Visitable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/28/13
 * @time 11:49 AM
 */
public abstract class AbstractWeightingSetTest extends TestCase {
    protected static final char[] keychars="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-".toCharArray();

    protected String[] genKeyStrings(Random rand, int num, int minl, int maxl, char[] characters) {
        String[] keys = new String[num];
        for(int i=0; i<num; i++) {
            StringBuilder buf=new StringBuilder();
            int len=minl+rand.nextInt(maxl-minl);
            for(int j=0;j<len;j++) buf.append(characters[rand.nextInt(characters.length)]);
            keys[i]=buf.toString();
        }
        return keys;
    }

    protected double[] genValWeights(Random rand, int num, int minv, int maxv) {
        double[] vals = new double[num];
        for(int i=0; i<num; i++) {
            vals[i] = minv+rand.nextInt(maxv-minv);
        }
        return vals;
    }

    public <K> void featureTestSize(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        double weight = 0;
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
                weight += vals[i];
            }
        }
        assertEquals("size()",cmap.size(),cset.size());
        assertEquals("weight()",weight,cset.weight());
    }

    public <K> void featureTestClear(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
            }
        }
        assertTrue("size()", cmap.size()>0);
        assertTrue("weight()", cset.weight()>0);
        cset.clear();
        assertEquals("size()", 0, cset.size());
        assertEquals("weight()", 0.0, cset.weight());
    }

    public <K> void featureTestWeight(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            assertEquals("weight(.)",cmap.containsKey(k)?cmap.get(k):0,cset.weight(k));
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
                assertEquals("weight(.)",v,cset.weight(k));
            }
        }
    }

    public <K> void featureTestHas(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            assertEquals("has(.)", cmap.containsKey(k), cset.has(k));
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
                assertEquals("has(.)",cmap.containsKey(k),cset.has(k));
            }
        }
    }

    public <K> void featureTestDel(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        double weight = 0;
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
                weight += vals[i];
            }
        }
        while(cmap.size()>0) {
            K k = keys[rand.nextInt(keys.length)];
            if(!cmap.containsKey(k)) continue;
            assertEquals("weight(.)",cmap.get(k).doubleValue(),cset.weight(k));
            cset.del(k);
            weight-=cmap.get(k).doubleValue();
            cmap.remove(k);
            assertEquals("size()", cmap.size(), cset.size());
        }
        assertEquals("size()",0,cset.size());
        assertEquals("weight()",0.0,cset.weight());
    }

    public <K> void featureTestIterator(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
            }
        }
        int num = 0;
        for(Iterator<? extends K> iterator=cset.iterator();iterator.hasNext();) {
            K k=iterator.next();
            assertTrue("next()",cmap.containsKey(k));
            num ++;
        }
        assertEquals("", cmap.size(), num);
        assertEquals("size()", cmap.size(), cset.size());
    }

    public <K> void featureTestVisit(Random rand, K[] keys, double[] vals, WeightingSet<K> cset) throws Exception {
        Map<K, Double> cmap = new HashMap<K, Double>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(vals[i]!=0) {
                double v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+vals[i]));
                else cmap.put(k, v=vals[i]);
                cset.add(k,vals[i]);
            }
        }
        int num = 0;
        for(Iterator<? extends K> iterator=cset.iterator();iterator.hasNext();) {
            K k=iterator.next();
            assertTrue("next()",cmap.containsKey(k));
            num ++;
        }
        assertEquals("",cmap.size(),cset.visit(new Visitable.Visitor<K,Map<K,Double>>() {
            @Override
            public long invoke(K obj, Map<K,Double> map) {
                assertTrue(map.containsKey(obj));
                return 1;
            }
        }, cmap));
    }

}
