package net.varkhan.base.management.logging.compat.log4j;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 5:20 AM
 */
public class Log {

    private final net.varkhan.base.management.logging.Log<Throwable> log;


    public Log(net.varkhan.base.management.logging.Log<Throwable> log) { this.log=log; }

    /**
     * Check whether this category is enabled for a given {@link Level} passed as parameter.
     *
     * @param level the level specification
     * @return {@code true} iff this category is enabled for {@code level}.
     */
    public boolean isEnabledFor(Level level) {
        return log.isLevelEnabled(level.level);
    }

    /**
     * Log a message object with the specified level including
     * the stack trace of the {@link Throwable} {@code t} passed as
     * parameter.
     *
     * @param level   the level to log under
     * @param message the message object to log.
     * @param t       the exception to log, including its stack trace.
     */
    public void log(Level level, Object message, Throwable t) {
        if(message==null)
            log.log(level.level,new Throwable(t));
        else
            log.log(level.level,new Throwable(message.toString()));
    }

    /**
     * Log a message object with the {@link Level#FATAL FATAL} Level.
     *
     * <p>This method first checks if this category is {@code FATAL} enabled by
     * comparing the level of this category with {@link Level#FATAL FATAL} Level.
     * If the category is {@code FATAL} enabled, then it converts the message
     * object passed as parameter to a string.
     *
     * <p><b>WARNING</b> Note that passing a {@link Throwable} to this method
     * will print the name of the Throwable but no stack trace.
     * To print a stack trace use the {@link #fatal(Object, Throwable)} form
     * instead.
     *
     * @param message the message object to log
     */
    public
    void fatal(Object message) {
        fatal(message,null);
    }

    /**
     * Log a message object with the {@link Level#FATAL FATAL} level including
     * the stack trace of the {@link Throwable} {@code t} passed as
     * parameter.
     *
     * <p>See {@link #fatal(Object)} for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void fatal(Object message, Throwable t) {
        log(Level.ERROR, message, t);
    }

    /**
     * Check whether this category is enabled for the {@link Level#ERROR ERROR} Level.
     *
     * @return boolean - {@code true} if this category is enabled
     * for level info, {@code false} otherwise.
     */
    public boolean isErrorEnabled() {
        return isEnabledFor(Level.ERROR);
    }

    /**
     * Log a message object with the {@link Level#ERROR ERROR} Level.
     *
     * <p>This method first checks if this category is {@code ERROR} enabled by
     * comparing the level of this category with {@link Level#ERROR ERROR} Level.
     * If this category is {@code ERROR} enabled, then it converts the message
     * object passed as parameter to a string.
     *
     * <p><b>WARNING</b> Note that passing a {@link Throwable} to this method
     * will print the name of the {@code Throwable} but no stack trace.
     * To print a stack trace use the {@link #error(Object, Throwable)} form instead.
     *
     * @param message the message object to log
     */
    public void error(Object message) {
        error(message,null);
    }

    /**
     * Log a message object with the {@link Level#ERROR ERROR} level including
     * the stack trace of the {@link Throwable} {@code t} passed as
     * parameter.
     *
     * <p>See {@link #error(Object)} form for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void error(Object message, Throwable t) {
        log(Level.ERROR,message,t);
    }

    /**
     * Check whether this category is enabled for the {@link Level#WARN WARN} Level.

     @return boolean - {@code true} if this category is enabled
     for level warn, {@code false} otherwise.
     */
    public boolean isWarnEnabled() {
        return isEnabledFor(Level.WARN);
    }

    /**
     * Log a message object with the {@link Level#WARN WARN} Level.
     *
     * <p> This method first checks if this category is {@code WARN} enabled by
     * comparing the level of this category with {@link Level#WARN WARN} Level.
     * If the category is {@code WARN} enabled, then it converts the
     * message object passed as parameter to a string.

     * <p><b>WARNING</b> Note that passing a {@link Throwable} to this method
     * will print the name of the {@code Throwable} but no stack trace.
     * To print a stack trace use the {@link #warn(Object, Throwable)} form
     * instead.
     *
     * @param message the message object to log
     */
    public void warn(Object message) {
        warn(message,null);
    }

    /**
     * Log a message object with the {@link Level#WARN WARN} level including
     * the stack trace of the {@link Throwable} {@code t} passed as
     * parameter.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void warn(Object message, Throwable t) {
        log(Level.WARN,message,t);
    }

    /**
     * Check whether this category is enabled for the {@link Level#INFO INFO} Level.
     * See also {@link #isDebugEnabled}.
     *
     * @return {@code true} if this category is enabled
     * for level info, {@code false} otherwise.
     */
    public boolean isInfoEnabled() {
        return isEnabledFor(Level.INFO);
    }

    /**
     * Log a message object with the {@link Level#INFO INFO} Level.
     *
     * <p>This method first checks if this category is {@code INFO} enabled by
     * comparing the level of this category with {@link Level#INFO INFO} Level.
     * If the category is {@code INFO} enabled, then it converts the message
     * object passed as parameter to a string.
     *
     * <p><b>WARNING</b> Note that passing a {@link Throwable} to this method
     * will print the name of the {@code Throwable} but no stack trace.
     * To print a stack trace use the {@link #info(Object, Throwable)} form
     * instead.
     *
     * @param message the message object to log
     */
    public void info(Object message) {
        info(message, null);
    }

    /**
     * Log a message object with the {@link Level#INFO INFO} level including
     * the stack trace of the {@link Throwable} {@code t} passed as
     * parameter.
     *
     * <p>See {@link #info(Object)} for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void info(Object message, Throwable t) {
        log(Level.INFO,message,t);
    }

    /**
     *  Check whether this category is enabled for the {@link Level#DEBUG DEBUG} level.
     *
     *  <p> This function is intended to lessen the computational cost of
     *  disabled log debug statements.
     *
     *  <p> For some {@code cat} Category object, when you write,
     *  <pre>
     *      cat.debug("This is entry number: " + i );
     *  </pre>
     *
     *  <p>You incur the cost constructing the message, concatenation in
     *  this case, regardless of whether the message is logged or not.
     *
     *  <p>If you are worried about speed, then you should write
     *  <pre>
     * 	 if(cat.isDebugEnabled()) {
     * 	   cat.debug("This is entry number: " + i );
     * 	 }
     *  </pre>
     *
     *  <p>This way you will not incur the cost of parameter
     *  construction if debugging is disabled for {@code cat}. On
     *  the other hand, if the {@code cat} is debug enabled, you
     *  will incur the cost of evaluating whether the category is debug
     *  enabled twice. Once in {@code isDebugEnabled} and once in
     *  the {@code debug}.  This is an insignificant overhead
     *  since evaluating a category takes about 1%% of the time it
     *  takes to actually log.
     *
     *  @return boolean - {@code true} if this category is debug
     *  enabled, {@code false} otherwise.
     */
    public boolean isDebugEnabled() {
        return isEnabledFor(Level.DEBUG);
    }

    /**
     * Log a message object with the {@link Level#DEBUG DEBUG} level.
     *
     * <p>This method first checks if this category is {@code DEBUG} enabled by
     * comparing the level of this category with the {@link Level#DEBUG DEBUG}
     * level. If this category is {@code DEBUG} enabled, then it converts the
     * message object (passed as parameter) to a string.
     *
     * <p><b>WARNING</b> Note that passing a {@link Throwable} to this method
     * will print the name of the {@code Throwable} but no stack trace.
     * To print a stack trace use the {@link #debug(Object, Throwable)} form
     * instead.
     *
     * @param message the message object to log.
     */
    public void debug(Object message) {
        debug(message,null);
    }


    /**
     * Log a message object with the {@link Level#DEBUG DEBUG} level including
     * the stack trace of the {@link Throwable} {@code t} passed as
     * parameter.
     *
     * <p> See {@link #debug(Object)} form for more detailed information.
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     */
    public void debug(Object message, Throwable t) {
        log(Level.DEBUG,message,t);
    }

    /**
     * Check whether this category is enabled for the {@link Level#TRACE TRACE} level.
     * @since 1.2.12
     *
     * @return {@code true} if this category is enabled for level
     *         TRACE, {@code false} otherwise.
     */
    public boolean isTraceEnabled() {
        return isEnabledFor(Level.TRACE);
    }

    /**
     * Log a message object with the {@link Level#TRACE TRACE} level.
     *
     * @param message the message object to log.
     * @see #debug(Object) for an explanation of the logic applied.
     * @since 1.2.12
     */
    public void trace(Object message) {
        trace(message,null);
    }

    /**
     * Log a message object with the {@link Level#TRACE TRACE} level including
     * the stack trace of the {@link Throwable}{@code t} passed as parameter.
     *
     * <p>
     * See {@link #trace(Object)} form for more detailed information.
     * </p>
     *
     * @param message the message object to log.
     * @param t the exception to log, including its stack trace.
     * @since 1.2.12
     */
    public void trace(Object message, Throwable t) {
        log(Level.TRACE,message,t);
    }






}
