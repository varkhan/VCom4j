package net.varkhan.base.functor.mapper;

import junit.framework.TestCase;
import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/24/13
 * @time 6:09 PM
 */
public class ComposedMapperTest extends TestCase {
    public void testInvoke() throws Exception {
        ComposedMapper<Integer,Long,Object> m = new ComposedMapper<Integer,Long,Object>(
                new Mapper<Integer,CharSequence,Object>() {
                    @Override
                    public Integer invoke(CharSequence arg, Object ctx) {
                        return arg.length();
                    }
                },
                new Mapper<String,Long,Object>() {
                    @Override
                    public String invoke(Long arg, Object ctx) {
                        return Long.toBinaryString(arg);
                    }
                }
        );
        assertEquals(1,(int)m.invoke(0L,null));
        assertEquals(1,(int)m.invoke(1L,null));
        assertEquals(2,(int)m.invoke(2L,null));
        assertEquals(2,(int)m.invoke(3L,null));
        assertEquals(3,(int)m.invoke(4L,null));
        assertEquals(3,(int)m.invoke(5L,null));
        assertEquals("1010",((Mapper<String,Long,Object>)m.right()).invoke(10L,null));
        assertEquals(3,(int)((Mapper<Integer,CharSequence,Object>)m.left()).invoke("123",null));
    }
}
