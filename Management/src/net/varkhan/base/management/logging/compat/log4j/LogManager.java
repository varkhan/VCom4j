package net.varkhan.base.management.logging.compat.log4j;


import net.varkhan.base.management.logging.LogConfigMap;
import net.varkhan.base.management.logging.LogMultiplexer;
import net.varkhan.base.management.logging.LogResolver;
import net.varkhan.base.management.logging.SimpleLogResolver;
import net.varkhan.base.management.metric.MilliTime;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 5:08 AM
 */
public class LogManager {

    private static final LoggerRepository repo = new DefaultRepository();

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

    public static class DefaultRepository implements LoggerRepository {

        public static final String DEFAULT_CONTEXT = "log4j";
        public final String ctx = DEFAULT_CONTEXT;
        public final LogResolver<Throwable> resolver = new SimpleLogResolver<Throwable>(new LogMultiplexer<Throwable>(), new LogConfigMap(), new MilliTime());
        public final ConcurrentMap<String,Log> loggers = new ConcurrentHashMap<String,Log>();

        public Log getLogger(String name) {
            Log logger=loggers.get(name);
            if(logger!=null) return logger;
            logger=new Log(resolver.getLogger(ctx, name));
            if(loggers.replace(name,null,logger)) return logger;
            return loggers.get(name);
        }
    }
}
