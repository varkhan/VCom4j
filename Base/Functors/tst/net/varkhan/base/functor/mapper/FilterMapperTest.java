package net.varkhan.base.functor.mapper;

import junit.framework.TestCase;
import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;
import net.varkhan.base.functor.expander.ArrayExpander;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 4:15 PM
 */
public class FilterMapperTest extends TestCase {

    public void testFilter() throws Exception {
        Predicate<String,Object> p = new Predicate<String,Object>() {
            @Override
            public boolean invoke(String arg, Object ctx) {
                return ctx==null||!arg.contains(ctx.toString());
            }
        };
        Mapper<String,String,Object> u = new Mapper<String,String,Object>() {
            public String invoke(String arg, Object ctx) { return arg.toUpperCase(); }
        };
        FilterMapper<String,String,Object> f = new FilterMapper<String,String,Object>(p, u);
        Mapper<Iterable<String>,String[],Object> a = new ArrayExpander<String,Object>();
        Mapper<Iterable<String>,String[],Object> m = new ComposedMapper<Iterable<String>,String[],Object>(f, a);
        Iterator<String> it0=m.invoke(new String[] { }, "bar").iterator();
        assertFalse("filter([]).hasNext()", it0.hasNext());
        Iterator<String> it1=m.invoke(new String[] { "foo" }, "bar").iterator();
        assertTrue("filter([]).hasNext() 0", it1.hasNext());
        assertEquals("filter([]).next() 0","FOO",it1.next());
        assertFalse("filter([]).hasNext() 1", it1.hasNext());
        Iterator<String> it2=m.invoke(new String[] { "foo", "bar" }, "bar").iterator();
        assertTrue("filter([]).hasNext() 0", it2.hasNext());
        assertEquals("filter([]).next() 0","FOO",it2.next());
        assertFalse("filter([]).hasNext() 1", it2.hasNext());
        Iterator<String> it3=m.invoke(new String[] { "bar", "foo" }, "bar").iterator();
        assertTrue("filter([]).hasNext() 0", it3.hasNext());
        assertEquals("filter([]).next() 0","FOO",it3.next());
        assertFalse("filter([]).hasNext() 1", it3.hasNext());
        Iterator<String> it4=m.invoke(new String[] { "bar", "foo", "baz" }, "bar").iterator();
        assertTrue("filter([]).hasNext() 0", it4.hasNext());
        assertEquals("filter([]).next() 0","FOO",it4.next());
        assertTrue("filter([]).hasNext() 1", it4.hasNext());
        assertEquals("filter([]).next() 1","BAZ",it4.next());
        assertFalse("filter([]).hasNext() 2", it4.hasNext());
        Iterator<String> it5=m.invoke(new String[] { "foo", "bar", "baz" }, "bar").iterator();
        assertTrue("filter([]).hasNext() 0", it5.hasNext());
        assertEquals("filter([]).next() 0","FOO",it5.next());
        assertTrue("filter([]).hasNext() 1", it5.hasNext());
        assertEquals("filter([]).next() 1","BAZ",it5.next());
        assertFalse("filter([]).hasNext() 2", it5.hasNext());
    }

}
