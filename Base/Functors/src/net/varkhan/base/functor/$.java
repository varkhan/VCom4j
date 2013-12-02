package net.varkhan.base.functor;

/**
 * <b>A class parametrized by a parametrized type</b>.
 * <p/>
 * This is a marker interface for constructs that are parametrized by types
 * that themselves have type parameters.
 * <p/>
 * A typical use would be a class that operate on several parametrized types,
 * and needs to specify non-trivial relationships between arbitrary type
 * parameters of its parameters. In the definition bellow:
 * <pre>
 *     class Translator<T, U, XT extends C<T>, XU extends C<U> {
 *         public XT translate(XU vals);
 *     }
 * </pre>
 * it is not immediately possible to generalize the types C, U and T to any value.
 * What we would like to write (which is not syntactically correct Java) is:
 * <pre>
 *     class C<T> { ... }
 *     class Translator<X extends C> {
 *         public <T,U> X<T> translate(X<U> vals) { ... }
 *     }
 * </pre>
 * Instead we can use the $ operator in the following way:
 * <pre>
 *     class C<X extends C<X,?>,T> implements $<X,T> { ... }
 *     class Translator<X extends C> {
 *         public <T,U> $<X,T> translate($<X,U> vals) { ... }
 *     }
 * </pre>
 *
 *
 * @author varkhan
 * @date 12/1/13
 * @time 3:24 PM
 */
public interface $<X extends $<X,?>,T> {


}
