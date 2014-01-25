package net.varkhan.core.geocode.features;

/**
 * <b>A geographical entity</b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/3/12
 * @time 7:22 PM
 */
public interface Entity {
    /**
     * The geo entity's type.
     *
     * @return the typology of the entity
     */
    public Typology typology();

    /**
     * The geo entity's division level.
     *
     * @return the division of the entity
     */
    public Division division();
}
