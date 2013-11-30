package net.varkhan.base.functor.predicate;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 12:42 PM
 */
public class EqualsPredicateTest extends TestCase {

    public void testEquals() {
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
        EqualsPredicate<O,Object> p01 = new EqualsPredicate<O,Object>(o01);
        EqualsPredicate<O,Object> p02 = new EqualsPredicate<O,Object>(o02);
        EqualsPredicate<O,Object> p10 = new EqualsPredicate<O,Object>(o11);
        assertTrue("0==. 1",p01.invoke(o01,null));
        assertFalse("0!=0 1", p01.invoke(o02, null));
        assertFalse("0!=0 2", p02.invoke(o01, null));
        assertTrue("0==. 2", p02.invoke(o02, null));
        assertTrue("1==. ",p10.invoke(o11,null));
        assertTrue("1==1 2",p10.invoke(new O(1), null));
        assertFalse("1!=0 1", p10.invoke(o01, null));
        assertFalse("1!=0 2", p10.invoke(o02, null));
    }

}
