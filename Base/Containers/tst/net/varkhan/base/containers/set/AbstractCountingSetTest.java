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
public abstract class AbstractCountingSetTest extends TestCase {
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

    protected long[] genValCounts(Random rand, int num, int minv, int maxv) {
        long[] vals = new long[num];
        for(int i=0; i<num; i++) {
            vals[i] = minv+rand.nextInt(maxv-minv);
        }
        return vals;
    }

    public <K> void featureTestSize(Random rand, K[] keys, long[] vals, CountingSet<K> cset, boolean exact) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        long count = 0;
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            for(int c=0;c<vals[i];c++) {
                if(cmap.containsKey(k)) cmap.put(k, cmap.get(k)+1L);
                else cmap.put(k, 1L);
                cset.add(k);
                count++;
            }
        }
        if(exact) {
            assertEquals("size()",cmap.size(),cset.size());
            assertEquals("count()",count,cset.count());
        }
        else {
            assertTrue("Size non-zero "+cset.size(), cset.size()>0);
            assertTrue("Size smaller than max "+cset.size()+" <> "+cmap.size(), cset.size()<=cmap.size());
            assertTrue("Size larger than half "+cset.size()+" <> "+cmap.size(), cset.size()>cmap.size()/2);
            assertTrue("Count non-zero "+cset.count(), cset.count()>0);
            assertTrue("Count smaller than max "+cset.count()+" <> "+count, cset.count()<=count);
        }
    }

    public <K> void featureTestClear(Random rand, K[] keys, long[] vals, CountingSet<K> cset) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            for(int c=0;c<vals[i];c++) {
                if(cmap.containsKey(k)) cmap.put(k, cmap.get(k)+1L);
                else cmap.put(k, 1L);
                cset.add(k);
            }
        }
        assertTrue("size()", cmap.size()>0);
        assertTrue("count()", cset.count()>0);
        cset.clear();
        assertEquals("size()", 0, cset.size());
        assertEquals("count()", 0, cset.count());
    }

    public <K> void featureTestCount(Random rand, K[] keys, long[] vals, CountingSet<K> cset, boolean exact) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        long count = 0;
        long e = 0;
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(exact) assertEquals("count(.)",(long)(cmap.containsKey(k)?cmap.get(k):0L),(long)cset.count(k));
            else {
                long x = cmap.containsKey(k)?cmap.get(k):0 - cset.count(k);
                e += x > 0 ? x : -x;
            }
            count += vals[i];
            for(int c=0;c<vals[i];c++) {
                long v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+1L));
                else cmap.put(k, v=1L);
                cset.add(k);
                if(exact) assertEquals("count(.)",v,cset.count(k));
            }
            if(!exact) {
                long x = cmap.containsKey(k)?cmap.get(k):0 - cset.count(k);
                e += x > 0 ? x : -x;
            }
        }
        if(!exact) {
            assertTrue("Error rate reasonable " + e + " <> " + count*cmap.size() , e <= 0.1 * count*cmap.size());
        }
    }

    public <K> void featureTestHas(Random rand, K[] keys, long[] vals, CountingSet<K> cset, boolean exact) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        long z=0;
        long y=0;
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            if(exact) assertEquals("has(.)", cmap.containsKey(k), cset.has(k));
            else {
                if(cset.has(k) && !cmap.containsKey(k)) z++;
                if(!cset.has(k) && cmap.containsKey(k)) y++;
            }
            for(int c=0;c<vals[i];c++) {
                long v;
                if(cmap.containsKey(k)) cmap.put(k, v=(cmap.get(k)+1L));
                else cmap.put(k, v=1L);
                cset.add(k);
                assertEquals("has(.)",cmap.containsKey(k),cset.has(k));
            }
        }
        if(!exact) {
            assertTrue("False pos reasonable " + z + " <> " + cmap.size(), z < 0.1 * cmap.size());
            assertTrue("False neg reasonable " + y + " <> " + cmap.size(), y < 0.1 * cmap.size());
        }
    }


    public <K> void featureTestDel(Random rand, K[] keys, long[] vals, CountingSet<K> cset, boolean exact) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        long count = 0;
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            for(int c=0;c<vals[i];c++) {
                if(cmap.containsKey(k)) cmap.put(k, cmap.get(k)+1L);
                else cmap.put(k, 1L);
                count++;
                cset.add(k);
            }
        }
        while(cmap.size()>0) {
            K k = keys[rand.nextInt(keys.length)];
            if(!cmap.containsKey(k)) continue;
            if(exact) assertEquals("count(.)",cmap.get(k).longValue(),cset.count(k));
            cset.del(k);
            count--;
            if(cmap.get(k)>1) cmap.put(k, cmap.get(k)-1L);
            else cmap.remove(k);
            if(exact) assertEquals("size()", cmap.size(), cset.size());
        }
        if(exact) {
            assertEquals("size()",0,cset.size());
        }
        else {
            assertTrue("Size small",cset.size()<0.1*vals.length);
        }
        assertEquals("count()",0,cset.count());
    }

    public <K> void featureTestIterator(Random rand, K[] keys, long[] vals, CountingSet<K> cset) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            for(int c=0;c<vals[i];c++) {
                if(cmap.containsKey(k)) cmap.put(k, cmap.get(k)+1L);
                else cmap.put(k, 1L);
                cset.add(k);
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

    public <K> void featureTestVisit(Random rand, K[] keys, long[] vals, CountingSet<K> cset) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            for(int c=0;c<vals[i];c++) {
                if(cmap.containsKey(k)) cmap.put(k, cmap.get(k)+1L);
                else cmap.put(k, 1L);
                cset.add(k);
            }
        }
        assertEquals("visit()",cmap.size(),cset.visit(new Visitable.Visitor<K,Map<K,Long>>() {
            @Override
            public long invoke(K obj, Map<K,Long> map) {
                assertTrue(map.containsKey(obj));
                return 1;
            }
        }, cmap));
    }

    public <K> void featureTestString(Random rand, K[] keys, long[] vals, CountingSet<K> cset) throws Exception {
        Map<K, Long> cmap = new HashMap<K, Long>(keys.length);
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            for(int c=0;c<vals[i];c++) {
                if(cmap.containsKey(k)) cmap.put(k, cmap.get(k)+1L);
                else cmap.put(k, 1L);
                cset.add(k);
            }
        }
        String s = cset.toString();
        assertTrue("toString() : { ",s.startsWith("{ "));
        assertTrue("toString() : { ",s.endsWith(" }"));
        for(int i=0;i<keys.length;i++) {
            String k = keys[i]+":"+cset.count(keys[i]);
            if(cset.count(keys[i])==0) assertFalse("toString() : "+k,s.contains(k+", ")||s.contains(k+" }"));
            else assertTrue("toString() : "+k,s.contains(k+", ")||s.contains(k+" }"));
        }
        System.out.println("toString OK");
    }

}
