package net.varkhan.base.extensions;

import java.util.*;

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
     * This method should define a reflexive, symmetric and transitive binary relation.
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

    /**
     * Creates an immutable set with the provided labels.
     *
     * @param labels the labels to form a set of
     * @param <L> the label category
     * @return an unmodifiable set containing the specified labels
     */
    public static <L extends Label<L>> Set<L> setOf(L... labels) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(labels)));
    }

    /**
     * Test whether a label overrides any element of a boundary set.
     *
     * @param label the label to test
     * @param boundary the overridden boundary
     * @param <L> the label category
     * @return {@literal true} iff any label in the boundary {@link #isOverriddenBy(Label)} the tested label
     */
    public static <L extends Label<L>> boolean isOverriding(L label, Set<L> boundary) {
        for(L b: boundary) if(b.isOverriddenBy(label)) return true;
        return false;
    }

    /**
     * Test whether a label is overridden by any element of a boundary set.
     *
     * @param label the label to test
     * @param boundary the overriding boundary
     * @param <L> the label category
     * @return {@literal true} iff the tested label {@link #isOverriddenBy(Label)} any label in the boundary
     */
    public static <L extends Label<L>> boolean isOverridden(L label, Set<L> boundary) {
        for(L b: boundary) if(label.isOverriddenBy(b)) return true;
        return false;
    }

    /**
     * Exclude all the labels of a set that override any element of a boundary set.
     *
     * @param labels the set of labels to filter
     * @param boundary the overridden boundary
     * @param <L> the label category
     * @return the subset of labels that do not override any element of the boundary
     */
    public static <L extends Label<L>> Set<L> excludeOverriding(Set<L> labels, Set<L> boundary) {
        Set<L> included = new LinkedHashSet<>();
        for(L l: labels) {
            if(!isOverriding(l,boundary)) included.add(l);
        }
        return included;
    }

    /**
     * Exclude all the labels of a set that are overridden by any element of a boundary set.
     *
     * @param labels the set of labels to filter
     * @param boundary the overriding boundary
     * @param <L> the label category
     * @return the subset of labels that are not overridden by any element of the boundary
     */
    public static <L extends Label<L>> Set<L> excludeOverridden(Set<L> labels, Set<L> boundary) {
        Set<L> included = new LinkedHashSet<>();
        for(L l: labels) {
            if(!isOverridden(l,boundary)) included.add(l);
        }
        return included;
    }

    /**
     * Extract the overridden bound of a set of labels.
     *
     * @param labels the set of labels to extract the bound of
     * @param <L> the label category
     * @return the subset of labels that do not override any element of the set
     */
    public static <L extends Label<L>> Set<L> overriddenBound(Set<L> labels) {
        return excludeOverriding(labels,labels);
    }

    /**
     * Extract the overriding bound of a set of labels.
     *
     * @param labels the set of labels to extract the bound of
     * @param <L> the label category
     * @return the subset of labels that are not overridden by any element of the set
     */
    public static <L extends Label<L>> Set<L> overridingBound(Set<L> labels) {
        return excludeOverridden(labels,labels);
    }

}
