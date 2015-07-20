package games.runje.dicymodel;


/**
 * Created by Thomas on 03.04.2015.
 */
public class Logger
{
    public static int LogLevel = 1;
    protected static int Debug = 2;
    protected static int Info = 3;
    protected static int Error = 4;

    protected static Logger instance = new Logger();

    public Logger()
    {
    }

    public static Logger getInstance()
    {
        return instance;
    }

    public static void setInstance(Logger logger)
    {
        instance = logger;
    }

    public static void logInfo(String key, String message)
    {
        getInstance().log(Info, key, message);
    }

    public static void logDebug(String key, String message)
    {
        getInstance().log(Debug, key, message);
    }

    public static void logError(String key, String message)
    {
        getInstance().log(Error, key, message);
    }

    public static void init()
    {
        getInstance().initLevels();
    }

    protected void log(int level, String key, String message)
    {
        System.out.println(key + ": " + message);
    }

    protected void initLevels()
    {

    }
}
