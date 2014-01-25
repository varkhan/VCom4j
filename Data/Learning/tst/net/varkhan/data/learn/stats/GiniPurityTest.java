package net.varkhan.data.learn.stats;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 3:40 PM
 */
public class GiniPurityTest extends TestCase {

    public void testInvoke() throws Exception {
        Purity<Integer,Object> purity =new GiniPurity<Integer,Object>();
        System.out.println("GiniPurity "+purity.invoke(groups(new Integer[][]{}),null));
        System.out.println("GiniPurity "+purity.invoke(groups(new Integer[][] { new Integer[] { 1, 1, 1, 2, 2, 2 } }), null));
        System.out.println("GiniPurity "+purity.invoke(groups(new Integer[] { 1, 1, 1 }, new Integer[] { 2, 2, 2 }), null));
        System.out.println("GiniPurity "+purity.invoke(groups(new Integer[] { 1, 1, 2 }, new Integer[] { 1, 2, 2 }), null));
        System.out.println("GiniPurity "+purity.invoke(groups(new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3 }), null));
        System.out.println("GiniPurity "+purity.invoke(groups(new Integer[] { 1, 2, 3 }, new Integer[] { 4, 5, 6 }), null));
    }


    protected final <A> Collection<Collection<A>> groups(A[]... vals) {
        Collection<Collection<A>> gr = new ArrayList<Collection<A>>();
        for(A[] av: vals) gr.add(Arrays.asList(av));
        return gr;
    }

}
