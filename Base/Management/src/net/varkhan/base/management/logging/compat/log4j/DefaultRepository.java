package net.varkhan.base.management.logging.compat.log4j;

import net.varkhan.base.management.config.MapConfiguration;
import net.varkhan.base.management.config.PrefixConfiguration;
import net.varkhan.base.management.config.SettableConfiguration;
import net.varkhan.base.management.logging.*;
import net.varkhan.base.management.metric.MilliTime;

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
    protected final SettableConfiguration     config  =new MapConfiguration();
    public final    LogMultiplexer<Throwable> writers =new LogMultiplexer<Throwable>();
    public          LogConfigProps            logconf =new LogConfigProps(Level.levelStrings(), null, config);
    private final   LogResolver<Throwable>    resolver=new SimpleLogResolver<Throwable>(
            writers,
            new LogConfigProps(Level.levelStrings(), new PrefixConfiguration(config)),
            new MilliTime()
    );
    private final   ConcurrentMap<String,Log> loggers =new ConcurrentHashMap<String,Log>();

    public DefaultRepository(String ctx) {
        this.ctx=ctx;
    }

    public Log getLogger(String name) {
        Log logger=loggers.get(name);
        if(logger!=null) return logger;
        logger=new Log(resolver.getLogger(ctx, name));
        if(loggers.putIfAbsent(name, logger)==null) return logger;
        return loggers.get(name);
    }

    public void addWriter(LogWriter<Throwable>... mtx) { writers.add(mtx); }

    public void setLevelMask(String ctx, String key, long lvm) { logconf.setLevelMask(ctx, key, lvm); }

    public void loadConfig(LogConfig cfg) { logconf.loadConfig(cfg); }

    @SuppressWarnings("unchecked")
    public void loadConfig(String ctx, Properties props) {
        logconf.loadConfig(ctx, (Map) props);
    }

}
