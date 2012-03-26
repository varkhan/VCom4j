package net.varkhan.core.management.logging.compat.log4j;

import net.varkhan.core.management.logging.*;
import net.varkhan.core.management.metric.MilliTime;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 3/25/12
* @time 4:25 PM
*/
public class DefaultRepository implements LoggerRepository {

    public final String ctx;
    public final LogMultiplexer<Throwable>  writers  = new LogMultiplexer<Throwable>();
    public final LogConfigMap               config = new LogConfigMap();
    private final LogResolver<Throwable>    resolver = new SimpleLogResolver<Throwable>(writers, new LogConfigPrefix(config), new MilliTime());
    private final ConcurrentMap<String,Log> loggers = new ConcurrentHashMap<String,Log>();

    public DefaultRepository(String ctx) {
        this.ctx = ctx;
    }

    public Log getLogger(String name) {
        Log logger=loggers.get(name);
        if(logger!=null) return logger;
        logger=new Log(resolver.getLogger(ctx, name));
        if(loggers.putIfAbsent(name,logger)==null) return logger;
        return loggers.get(name);
    }

    public void addWriter(LogWriter<Throwable>... mtx) { writers.add(mtx); }

    public void setLevelMask(String ctx, String key, long lvm) { config.setLevelMask(ctx, key, lvm); }

    public void loadConfig(LogConfig cfg) { config.loadConfig(cfg); }

    public void loadConfig(String ctx, Properties props) {
        LogConfigProps cfg = new LogConfigProps(Level.levelStrings());
        //noinspection unchecked
        cfg.loadConfig(ctx,(Map) props);
        config.loadConfig(cfg);
    }

}
