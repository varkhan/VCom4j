package net.varkhan.data.diff;

import junit.framework.TestCase;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.list.List;

import java.util.Comparator;
//import java.util.Iterator;


public class EugeneMyersDiffTest extends TestCase {

    public void testInvoke() throws Exception {
        List<String> ls1=Arrays.asList("a", "b", "c", "d", "e");
        List<String> ls2=Arrays.asList("a", "b", "x", "c", "y", "z");
        Diff<String,List<String>,Object> differ=new EugeneMyersDiff<String,List<String>,Object>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        if(o1==null&&o2==null) return 0;
                        if(o1==null) return +1;
                        if(o2==null) return -1;
                        else return o1.compareTo(o2);
                    }
                }
        );
        Iterable<Diff.Block<String>> diffs=differ.invoke(ls1, ls2, null);
        assertIteratorEquals(
                "diff("+ls1.toString()+"<>"+ls2.toString()+")",
                Arrays.asList(
                        new DiffBlock<String>(null, 2, 2, null, 2, 3),
                        new DiffBlock<String>(null, 3, 5, null, 4, 6)
                ).iterator(),
                diffs.iterator()
        );
    }

    protected <T> void assertIteratorEquals(String mes, Iterator<? extends T> exp, Iterator<? extends T> act) {
        int i=0;
        while(exp.hasNext() && act.hasNext()) {
            assertEquals(mes+" ["+(i++)+"]==",exp.next(),act.next());
        }
        assertEquals(mes+" ["+i+"]--", false, exp.hasNext());
        assertEquals(mes+" ["+i+"]++", false, act.hasNext());
    }


}