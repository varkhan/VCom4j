package net.varkhan.base.extensions;

import net.varkhan.base.extensions.type.Kind;

/**
 * <b>A type-checked object of a specific {@link net.varkhan.base.extensions.type.Kind}</b>.
 * <br/>
 *
 * @author varkhan
 * @date 18/4/19
 * @time 5:57 PM
 */
public interface Kinded<T> {

    /**
     * The object's {@link Kind}
     *
     * @return the kind information that values for this object are checked against
     */
    public Kind<T> kind();

    public static abstract class Base<T> implements Kinded<T> {
        protected final Kind<T> kind;
        protected Base(Kind<T> kind) { this.kind = kind; }
        @Override public Kind<T> kind() { return kind; }
    }

    public static abstract class Named<T> extends net.varkhan.base.extensions.Named.Base implements Kinded<T> {
        protected final Kind<T> kind;
        protected Named(String name, Kind<T> kind) { super(name); this.kind = kind; }
        @Override public Kind<T> kind() { return kind; }
    }

}
