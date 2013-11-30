package net.varkhan.base.functor.predicate;

import junit.framework.TestCase;
import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 12:57 PM
 */
public class SetPredicateTest extends TestCase {

    public void testSet() throws Exception {
        class O {
            int i;
            O(int i) { this.i=i; }
            public boolean equals(Object o) {
                if(this==o) return true;
                if(o==null||getClass()!=o.getClass()) return false;
                return !(i==0&&((O) o).i==0)&&i==((O) o).i;
            }
            public int hashCode() {
                return i;
            }
        }
        O o01=new O(0);
        O o02=new O(0);
        O o11=new O(1);
        Predicate<O,Object> p00 = new SetPredicate<O, Object>();
        Predicate<O,Object> p101 = new SetPredicate<O, Object>(o01);
        Predicate<O,Object> p102 = new SetPredicate<O, Object>(o02);
        Predicate<O,Object> p111 = new SetPredicate<O, Object>(o11);
        Predicate<O,Object> p20102 = new SetPredicate<O, Object>(o01,o02);
        assertFalse("[] 01",p00.invoke(o01,null));
        assertFalse("[] 02",p00.invoke(o02,null));
        assertFalse("[] 11",p00.invoke(o11,null));
        assertTrue("[01] 01", p101.invoke(o01, null));
        assertFalse("[01] 02", p101.invoke(o02, null));
        assertFalse("[01] 11",p101.invoke(o11,null));
        assertFalse("[02] 01",p102.invoke(o01,null));
        assertTrue("[02] 02", p102.invoke(o02, null));
        assertFalse("[02] 11", p102.invoke(o11, null));
        assertFalse("[11] 01",p111.invoke(o01,null));
        assertFalse("[11] 02", p111.invoke(o02, null));
        assertTrue("[11] 11", p111.invoke(o11, null));
        assertTrue("[11] 12", p111.invoke(new O(1), null));
        assertTrue("[01] 01", p20102.invoke(o01, null));
        assertTrue("[01] 02", p20102.invoke(o02, null));
        assertFalse("[01] 11",p20102.invoke(o11,null));
    }

}
