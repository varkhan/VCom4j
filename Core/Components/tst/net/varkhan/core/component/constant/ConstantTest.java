package net.varkhan.core.component.constant;

import junit.framework.TestCase;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;


public class ConstantTest extends TestCase {

    public static abstract class Ct0 extends Constant<Ct0,Ct0,Ct0,Ct10> {
        public Ct0(String name) {
            super(name);
        }
        public abstract int num();
    }

    public static class Ct10 extends Constant<Ct0,Ct0,Ct10,Ct2x> {
        public Ct10(Ct0 sup, String name) {
            super(sup, name);
        }
    }

    public static class Ct2x extends Constant<Ct0,Ct10,Ct2x,Ct31> {
        protected Ct2x(Ct10 sup, String name) {
            super(sup, name);
        }
    }

    public static class Ct21 extends Ct2x {
        protected Ct21(Ct10 sup, String name) {
            super(sup, name);
        }
    }

    public static class Ct22 extends Ct2x {
        protected Ct22(Ct10 sup, String name) {
            super(sup, name);
        }
    }

    public static class Ct31 extends Constant<Ct0,Ct2x,Ct31,Constant.None<Ct0,Ct31,?>> {
        protected Ct31(Ct21 sup, String name) {
            super(sup, name);
        }
    }

    public static class Ct32 extends Constant<Ct0,Ct2x,Ct32,Constant.None<Ct0,Ct32,?>> {
        protected Ct32(Ct22 sup, String name) {
            super(sup, name);
        }
    }

    public void testCreation() throws Exception {
        Ct0 root = new Ct0("root") {
            @Override
            public int num() {
                return 8007;
            }
        };
        Ct10 c101 = new Ct10(root, "C1:0_1");
        Ct10 c102 = new Ct10(root, "C1:0_2");
        Ct10 c103 = new Ct10(root, "C1:0_3");
        Ct2x c211 = new Ct21(c101, "C2:1_1");
        Ct2x c212 = new Ct21(c101, "C2:1_2");
        Ct2x c213 = new Ct21(c101, "C2:1_3");
        Ct2x c221 = new Ct22(c102, "C2:2_1");
        assertEquals("", root, root.dom());
        assertEquals("", root, c101.dom());
        assertEquals("", root, c102.dom());
        assertEquals("", root, c103.dom());
        assertEquals("", root, c101.sup());
        assertEquals("", root, c102.sup());
        assertEquals("", root, c103.sup());
        assertEquals("", root, c211.dom());
        assertEquals("", root, c212.dom());
        assertEquals("", root, c213.dom());
        assertEquals("", root, c221.dom());
        assertArrayEquals("", new Object[] { c211, c212, c213 }, c101.sub());
        assertArrayEquals("", new Object[] { c221 }, c102.sub());
        assertArrayEquals("", new Object[] { }, c103.sub());
        System.err.println(tree(root));
    }


    private static <D extends Constant<D,D,D,?>> String tree(D c) throws IOException {
        return tree(new StringBuilder(),"",c).toString();
    }

    private static <D extends Constant<D,D,D,?>, E extends Constant<D,?,?,?>,A extends Appendable> A tree(A buf, String pfx, E c) throws IOException {
        buf.append(pfx).append(c.path()).append(" <").append(Long.toString(c.id())).append("> ").append(" \"").append(c.toString()).append("\"");
        if(c.sub().length>0) {
            buf.append(" [\n");
            for(Constant<D,?,?,?> cc: c.sub()) {
                tree(buf,pfx+"\t",cc);
            }
            buf.append(pfx).append("]");
        }
        buf.append("\n");
        return buf;
    }

//    public static class Domain<E extends Domain<E,C>, C extends Constant<E,E,C,?>> extends Constant<E,E,E,C> {
//
//        private Domain() {
//            super("_");
//        }
//
//        @SuppressWarnings("unchecked")
//        public Domain(String name) {
//            super((E) root,name);
//        }
//
//        public class Administration<E extends Administration<E,C>, C extends Constant<Domain<?,E>,E,C,?>>
//
//    }
//
//    private static final Domain<?,?> root = new Domain();
//    public static final Domain<?,?> Administration = new Domain("Administration");
//    static {
//        new Domain("Geography");
//        new Domain("Infrastructure");
//        new Domain("Buildings");
//    }
//
//    public static Domain<?,?> v(String name) { return (Domain<?, ?>) root.valueOf(name); }

}