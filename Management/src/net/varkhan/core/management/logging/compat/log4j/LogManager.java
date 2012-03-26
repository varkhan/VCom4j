package net.varkhan.core.management.logging.compat.log4j;


import net.varkhan.core.management.logging.LogAppender;
import net.varkhan.core.management.logging.LogWriter;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 5:08 AM
 */
public class LogManager {

    public static final String DEFAULT_CONTEXT = "log4j";

    private static final DefaultRepository repo = new DefaultRepository(DEFAULT_CONTEXT);
    static {
        repo.addWriter(new LogAppender<Throwable>(System.err));
    }

    public static LoggerRepository getLoggerRepository() {
        return repo;
    }

    /**
     * Retrieve the appropriate {@link Log} instance.
     */
    public static Log getLogger(final String name) {
        // Delegate the actual manufacturing of the logger to the logger repository.
        return getLoggerRepository().getLogger(name);
    }

    /**
     * Retrieve the appropriate {@link Log} instance.
     */
    public static Log getLogger(final Class clazz) {
        // Delegate the actual manufacturing of the logger to the logger repository.
        return getLoggerRepository().getLogger(clazz.getName());
    }

}
