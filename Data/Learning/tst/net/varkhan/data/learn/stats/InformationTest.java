package net.varkhan.data.learn.stats;

import junit.framework.TestCase;
import net.varkhan.base.containers.set.ArrayOpenHashCountingSet;
import net.varkhan.base.containers.set.CountingSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 3:40 PM
 */
public class InformationTest extends TestCase {

    public void testInvoke() throws Exception {
        Purity<Integer,Object> purity =new Information<Integer,Object>();
        System.out.println("Information "+purity.invoke(groups(new Integer[][]{}),null));
        System.out.println("Information "+purity.invoke(groups(new Integer[][]{new Integer[]{1,1,1,2,2,2}}),null));
        System.out.println("Information "+purity.invoke(groups(new Integer[]{1,1,1},new Integer[]{2,2,2}),null));
        System.out.println("Information "+purity.invoke(groups(new Integer[]{1,1,2},new Integer[]{1,2,2}),null));
        System.out.println("Information "+purity.invoke(groups(new Integer[]{1,2,3},new Integer[]{1,2,3}),null));
        System.out.println("Information "+purity.invoke(groups(new Integer[]{1,2,3},new Integer[]{4,5,6}),null));
    }

    public void testEntropy() throws Exception {
        System.out.println("entropy(...,...) "+Information.entropy(counts()));
        System.out.println("entropy(...,...) "+Information.entropy(counts(new Integer[] { 1, 1, 1 }, new Integer[] { 2, 2, 2 })));
        System.out.println("entropy(...,...) "+Information.entropy(counts(new Integer[] { 1, 1, 2 }, new Integer[] { 1, 2, 2 })));
        System.out.println("entropy(...,...) "+Information.entropy(counts(new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3 })));
        System.out.println("entropy(...,...) "+Information.entropy(counts(new Integer[] { 1, 2, 3 }, new Integer[] { 4, 5, 6 })));
    }


    protected final <A> Collection<Collection<A>> groups(A[]... vals) {
        Collection<Collection<A>> gr = new ArrayList<Collection<A>>();
        for(A[] av: vals) gr.add(Arrays.asList(av));
        return gr;
    }

    protected final <A> List<CountingSet<A>> counts(A[]... vals) {
        List<CountingSet<A>> sets = new ArrayList<CountingSet<A>>(vals.length);
        for(A[] va: vals) sets.add(count(va));
        return sets;
    }

    protected final <A> CountingSet<A> count(A... vals) {
        CountingSet<A> set = new ArrayOpenHashCountingSet<A>();
        for(A v: vals) set.add(v);
        return set;
    }

}
