package net.varkhan.core.management.logging;

import net.varkhan.core.management.metric.Time;


public class SimpleLogResolver<T> implements LogResolver<T> {

    private final LogWriter<T> wrt;
    private final LogConfig    cfg;
    private final Time         tmr;

    /**
     * Create a simple logger.
     *
     * @param wrt a log writer
     * @param cfg a log configuration
     * @param tmr a log timer
     */
    public SimpleLogResolver(LogWriter<T> wrt, LogConfig cfg, Time tmr) {
        this.wrt=wrt;
        this.cfg=cfg;
        this.tmr=tmr;
    }

    public Logger<T> getLogger(String ctx, String key) {
        return new SimpleLogger<T>(ctx, key, this);
    }

    public void log(String ctx, String key, int lev, T msg) {
        if(cfg.isLevelEnabled(ctx, key, lev)) wrt.log(ctx, key, lev, tmr.time(), msg);
    }

    public void log(String ctx, String key, int lev, double val, T msg) {
        if(cfg.isLevelEnabled(ctx, key, lev)) wrt.log(ctx, key, lev, tmr.time(), val, msg);
    }

    public long getLevelMask(String ctx, String key) {
        return cfg.getLevelMask(ctx, key);
    }

    public boolean isLevelEnabled(String ctx, String key, int lev) {
        return cfg.isLevelEnabled(ctx, key, lev);
    }

    public Iterable<String> contexts() {
        return cfg.contexts();
    }

    public Iterable<Level> levels(String ctx) {
        return cfg.levels(ctx);
    }

    public static class SimpleLogger<T> implements Logger<T> {

        private final String    ctx;
        private final String    key;
        private final LogResolver<T> lgr;

        /**
         * Create a simple log, linked to a logger.
         *
         * @param ctx the logging context
         * @param key the logging key
         * @param lgr the logger
         */
        public SimpleLogger(String ctx, String key, LogResolver<T> lgr) {
            this.ctx=ctx;
            this.key=key;
            this.lgr=lgr;
        }

        public String getContext() { return ctx; }

        public String getKey() { return key; }

        public long getLevelMask() { return lgr.getLevelMask(ctx, key); }

        public boolean isLevelEnabled(int lev) { return lgr.isLevelEnabled(ctx, key, lev); }

        public void log(int lev, T msg) { lgr.log(ctx, key, lev, msg); }

        public void log(int lev, double val, T msg) { lgr.log(ctx, key, lev, val, msg); }

    }
}
