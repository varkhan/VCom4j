package net.varkhan.data.diff;

import junit.framework.TestCase;
import net.varkhan.base.containers.Container;
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
                        new B<String>(null, 2, 2, null, 2, 3),
                        new B<String>(null, 3, 5, null, 4, 6)
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

    protected static class B<T> implements Diff.Block<T> {
        protected int          begL;
        protected int          begR;
        protected int          endL;
        protected int          endR;
        protected Container<T> blockL;
        protected Container<T> blockR;

        @SuppressWarnings("unchecked")
        protected B(List datL, int begL, int endL, List datR, int begR, int endR) {
            this.begL=begL;
            this.begR=begR;
            this.endL=endL;
            this.endR=endR;
//            blockL=datL.sublist(begL, endL);
//            blockR=datR.sublist(begR, endR);
        }

        @Override
        public int begL() { return begL; }

        @Override
        public int begR() { return begR; }

        @Override
        public int endL() { return endL; }

        @Override
        public int endR() { return endR; }

        @Override
        public Container<T> blockL() {
            return blockL;
        }

        @Override
        public Container<T> blockR() {
            return blockR;
        }

        @Override
        public String toString() {
            return "[ "+
                   begL+","+endL+
                   " x "+
                   begR+","+endR+
                  " ]";
        }

        @Override
        public boolean equals(Object o) {
            if(this==o) return true;
            if(!(o instanceof Diff.Block)) return false;
            Diff.Block b=(Diff.Block) o;
            if(begL!=b.begL()) return false;
            if(begR!=b.begR()) return false;
            if(endL!=b.endL()) return false;
            if(endR!=b.endR()) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result=begL;
            result=31*result+begR;
            result=31*result+endL;
            result=31*result+endR;
            return result;
        }
    }

}