package net.varkhan.base.extensions.type;

import junit.framework.TestCase;
import net.varkhan.base.extensions.Kinded;

public class StructKindTest extends TestCase {

    public void testStruct() {
        StructKind<Object, Kinded.Named<?>> sk = new StructKind<Object, Kinded.Named<?>>("s",
                new Kinded.Named<Integer>("i", Kind.INT) {},
                new Kinded.Named<String>("t", Kind.nullable(Kind.STRING)) {}
                ) {  };
        assertEquals("toString","s<i:int,t:nullable<string>>", sk.toString());
        assertEquals("getFields().size",2,sk.fields().size());
        assertEquals("getFields().get(0)","i",sk.fields().get(0).name());
    }

}