package net.varkhan.base.extensions;

/**
 * <b>An interface to denote "unique" objects possessing a canonical name.</b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/8/11
 * @time 11:54 PM
 */
public interface Named {

    /**
     * The unique object name.
     *
     * @return the object name
     */
    public String name();

    /**
     * A default implementation of {@link Named}
     */
    public abstract class Base implements Named {
        protected final String name;
        protected Base(String name) {
            this.name = name;
        }
        @Override public String name() {
            return name;
        }
        @Override public String toString() { return name(); }
    }
}
