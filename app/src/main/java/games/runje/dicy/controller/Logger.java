package games.runje.dicy.controller;

import android.util.Log;

/**
 * Created by Thomas on 19.10.2014.
 */
public class Logger
{
    public static int LogLevel = 1;

    public static void logInfo(String key, String message)
    {
        Log.d(key, message);
    }

    public static void logDebug(String key, String message)
    {
        if (LogLevel > 0)
        {
            Log.d(key, message);
        }
    }

    public static void logError(String key, String message)
    {
        Log.e(key, message);
    }

}
