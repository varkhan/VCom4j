package net.varkhan.base.containers;

import junit.framework.TestCase;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;


public class ReflectTest extends TestCase {


    public interface GenInterface1<A extends CharSequence> extends java.lang.Iterable<A> {
        public A getA();

        @Override
        default java.util.Iterator<A> iterator() {
            return new Iterator<A>() {
                private boolean used = false;
                @Override public boolean hasNext() { return !used; }

                @Override public A next() {
                    if(used) throw new NoSuchElementException();
                    used = true;
                    return getA();
                }
            };
        }

        @Override
        default void forEach(Consumer<? super A> action) { action.accept(getA()); }

        @Override
        default Spliterator<A> spliterator() { return Spliterators.spliterator(this.iterator(),1, Spliterator.SIZED); }
    }

    public interface GenInterface2<T,U extends Number> extends Function<T,U> {
        @Override
        U apply(T t);
    }

    public static abstract class GenAbstractX<AA extends CharSequence,TT,UU extends Number> implements GenInterface1<AA>, GenInterface2<TT,UU> {
        protected AA a;
        protected TT t;
        protected UU u;

        public AA getA() { return a; }

        @Override public UU apply(TT tt) { if(tt==t) return u; else return null; }
    }

    public static class GenConcreteY<TTT> extends GenAbstractX<String,TTT,Integer>/* implements GenInterface1<A>*/ {
        @Override
        public Integer apply(TTT ttt) { if(ttt==t) return 1; else return 0; }
    }

    public void testGetGenericParameters_DirectConcrete() {
        assertEquals("","[T, U]",
                Arrays.toString(Reflect.getGenericParameters( GenInterface2.class, GenInterface2.class)) );
        assertEquals("","[TT, UU]",
                Arrays.toString(Reflect.getGenericParameters( GenAbstractX.class, GenInterface2.class)) );
    }

    public void testGetGenericParameters_IndirectAbstract() {
        GenConcreteY<Boolean> y1 = new GenConcreteY<Boolean>();
        assertEquals("y1","[class java.lang.String]",
                Arrays.toString(Reflect.getGenericParameters( y1.getClass(), GenInterface1.class)) );
        assertEquals("y2","[TTT, class java.lang.Integer]",
                Arrays.toString(Reflect.getGenericParameters( y1.getClass(), GenInterface2.class)) );
    }

    public void testGetGenericParameters_IndirectAnonymous() {
        GenConcreteY<Boolean> y3 = new GenConcreteY<Boolean>(){};
        assertEquals("y3","[class java.lang.String]",
                Arrays.toString(Reflect.getGenericParameters( y3.getClass(), GenInterface1.class)) );
        assertEquals("y4","[class java.lang.Boolean, class java.lang.Integer]",
                Arrays.toString(Reflect.getGenericParameters( y3.getClass(), GenInterface2.class)) );
    }

    public void testGetGenericParameters_IndirectConcrete() {
        GenAbstractX<String,Boolean,Integer> x = new GenAbstractX<String,Boolean,Integer>() {};
        assertEquals("x","[class java.lang.Boolean, class java.lang.Integer]",
                Arrays.toString(Reflect.getGenericParameters( x.getClass(), GenInterface2.class)) );
    }

    public void testGetBaseClass() {
        assertNull("i1", Reflect.getBaseClass(GenInterface1.class.getTypeParameters()[0]));
        GenConcreteY<List<String>> y5 = new GenConcreteY<List<String>>(){};
        ParameterizedType t5 = (ParameterizedType) y5.getClass().getGenericSuperclass();
        assertEquals("y5", List.class, Reflect.getBaseClass(t5.getActualTypeArguments()[0]));
    }
}