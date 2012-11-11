package net.varkhan.base.management.logging.compat.log4j;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 6:31 AM
 */
public interface LoggerRepository {
//    /**
//     * Returns whether this repository is disabled for a given
//     * level. The answer depends on the repository threshold and the
//     * <code>level</code> parameter. See also {@link #setThreshold}
//     * method.
//     */
//    boolean isDisabled(int level);
//
//    /**
//     * Set the repository-wide threshold. All logging requests below the
//     * threshold are immediately dropped. By default, the threshold is
//     * set to <code>Level.ALL</code> which has the lowest possible rank.
//     */
//    public void setThreshold(Level level);
//
//    /**
//     * Another form of {@link #setThreshold(Level)} accepting a string
//     * parameter instead of a <code>Level</code>.
//     */
//    public void setThreshold(String val);
//
//    /**
//     * Get the repository-wide threshold. See {@link #setThreshold(Level)} for an explanation.
//     */
//    public Level getThreshold();

    public Log getLogger(String name);

}
