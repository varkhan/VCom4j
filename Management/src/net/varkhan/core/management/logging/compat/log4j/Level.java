package net.varkhan.core.management.logging.compat.log4j;

import java.io.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 5:25 AM
 */
public class Level implements Serializable {

    transient int level;
    transient String levelStr;
    transient int syslogEquivalent;

    public static final int FATAL_INT = 1;
    public static final int ERROR_INT = 2;
    public static final int WARN_INT  = 3;
    public static final int INFO_INT  = 4;
    public static final int DEBUG_INT = 5;
    public static final int TRACE_INT = 6;

    /**
     * The {@code FATAL} level designates very severe error
     * events that will presumably lead the application to abort.
     */
    public static final Level FATAL = new Level(FATAL_INT, "FATAL", 0);

    /**
     * The {@code ERROR} level designates error events that
     * might still allow the application to continue running.
     */
    public static final Level ERROR = new Level(ERROR_INT, "ERROR", 3);

    /**
     * The {@code WARN} level designates potentially harmful situations.
     */
    public static final Level WARN  = new Level(WARN_INT, "WARN",  4);

    /**
     * The {@code INFO} level designates informational messages
     * that highlight the progress of the application at coarse-grained
     * level.
     */
    public static final Level INFO  = new Level(INFO_INT, "INFO",  6);

    /**
     * The {@code DEBUG} Level designates fine-grained
     * informational events that are most useful to debug an
     * application.
     */
    public static final Level DEBUG = new Level(DEBUG_INT, "DEBUG", 7);

    /**
     * The {@code TRACE} Level designates finer-grained
     * informational events than the <code>DEBUG</code level.
     */
    public static final Level TRACE = new Level(TRACE_INT, "TRACE", 7);

    /**
     * Serialization version id.
     */
    static final long serialVersionUID = 3491141966387921974L;

    /**
     * Instantiate a Level object.
     */
    protected Level(int level, String levelStr, int syslogEquivalent) {
        this.level = level;
        this.levelStr = levelStr;
        this.syslogEquivalent = syslogEquivalent;
    }


    /**
     * Convert the string passed as argument to a level. If the
     * conversion fails, then this method returns {@link #DEBUG}.
     */
    public static Level toLevel(String str) {
        return toLevel(str, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a level. If the
     * conversion fails, then this method returns {@link #DEBUG}.
     */
    public static Level toLevel(int val) {
        return (Level) toLevel(val, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a level. If the
     * conversion fails, then this method returns the specified default.
     */
    public static Level toLevel(int val, Level defaultLevel) {
        switch(val) {
            case DEBUG_INT: return Level.DEBUG;
            case INFO_INT: return Level.INFO;
            case WARN_INT: return Level.WARN;
            case ERROR_INT: return Level.ERROR;
            case FATAL_INT: return Level.FATAL;
            case TRACE_INT: return Level.TRACE;
            default: return defaultLevel;
        }
    }

    /**
     * Convert the string passed as argument to a level. If the
     * conversion fails, then this method returns the value of
     * {@code defaultLevel}.
     */
    public static Level toLevel(String str, Level defaultLevel) {
        if(str == null) return defaultLevel;

        if("DEBUG".equalsIgnoreCase(str)) return Level.DEBUG;
        if("INFO" .equalsIgnoreCase(str))  return Level.INFO;
        if("WARN" .equalsIgnoreCase(str))  return Level.WARN;
        if("ERROR".equalsIgnoreCase(str)) return Level.ERROR;
        if("FATAL".equalsIgnoreCase(str)) return Level.FATAL;
        if("TRACE".equalsIgnoreCase(str)) return Level.TRACE;
        return defaultLevel;
    }

    /**
     * Custom deserialization of Level.
     * @param s serialization stream.
     * @throws java.io.IOException if IO exception.
     * @throws ClassNotFoundException if class not found.
     */
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        level = s.readInt();
        syslogEquivalent = s.readInt();
        levelStr = s.readUTF();
        if (levelStr == null) {
            levelStr = "";
        }
    }

    /**
     * Serialize level.
     * @param s serialization stream.
     * @throws IOException if exception during serialization.
     */
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(level);
        s.writeInt(syslogEquivalent);
        s.writeUTF(levelStr);
    }

    /**
     * Resolved deserialized level to one of the stock instances.
     * May be overriden in classes derived from Level.
     * @return resolved object.
     * @throws java.io.ObjectStreamException if exception during resolution.
     */
    private Object readResolve() throws ObjectStreamException {
        //
        //  if the deserizalized object is exactly an instance of Level
        //
        if (getClass() == Level.class) {
            return toLevel(level);
        }
        //
        //   extension of Level can't substitute stock item
        //
        return this;
    }
}
