package net.varkhan.base.extensions;

/**
 * <b>A named marker object that indicates the validity of a predicate within a certain category.</b>.
 * <p/>
 * Labels belongs to a certain category, defined by the top-most non-parametrized subclass of this interface
 * present in their inheritance hierarchy (i.e. the value of the L type parameter).
 * <p/>
 * Within their category, a notion of <i>equivalence</i> is implicitly defined by the {@link #isEquivalentTo(Label)}
 * method. This method should define an equivalence relation on the category, i.e. be a reflexive, symmetric and
 * transitive binary relation such that {@code (a.isEquivalentTo(a) == true)},
 * {@code (a.isEquivalentTo(b) == b.isEquivalentTo(a))} and {@code ( (a.isEquivalentTo(b) && b.isEquivalentTo(c)
 * && !a.isEquivalentTo(c) ) == false)}.
 * <p/>
 * Within their category, a notion of <i>strength</i>, or precedence, is implicitly defined by the
 * {@link #isOverriddenBy(Label)} method. This method should define a strict partial order on the category,
 * i.e. be an irreflexive, asymmetric and transitive binary relation such that {@code (a.isOverriddenBy(a) == false)},
 * {@code (a.isOverriddenBy(b) && b.isOverriddenBy(a) == false)} and {@code ( (a.isOverriddenBy(b) &&
 * b.isOverriddenBy(c) && !a.isOverriddenBy(c) )
 * == false)}.
 * <p/>
 * This definition takes the position of "privileging" stronger labels, by allowing the implementation of each
 * label to be in control of which other labels they allow themselves to be overridden by, such that subsequent
 * implementations of the category will not be able to override this relation.
 * <p/>
 *
 * @param <L> the category (super type, or hierarchy root) of the label
 *
 * @author varkhan
 * @date 4/8/18
 * @time 16:34 PM
 */
public interface Label<L extends Label<L>> {

    /**
     * Whether this label is overridden by (strictly weaker than) another label.
     * <p/>
     * This method should define an irreflexive, transitive and asymmetric binary relation.
     *
     * @param label an other label in the category
     * @return {@literal true} if the other label is strictly more stringent
     */
    public default boolean isOverriddenBy(L label) { return false; }

    /**
     * Whether this label is equivalent to (of the same strength as) another label.
     * <p/>
     * This method should define a reflexive and transitive binary relation.
     *
     * @param label an other label in the category
     * @return {@literal true} if the other label is equivalently stringent
     */
    public default boolean isEquivalentTo(L label) { return this.equals(label); }

    /**
     * A default implementation of {@link Label}
     */
    public abstract class Base<L extends Label<L>> extends Named.Base implements Label<L> {
        protected Base(String name) { super(name); }
        @Override public boolean isOverriddenBy(L label) { return false; }
        @Override public boolean isEquivalentTo(L label) { return this.equals(label); }
        @Override public boolean equals(Object that) { return this==that; }
        @Override public int hashCode() { return super.hashCode(); }
    }
}
