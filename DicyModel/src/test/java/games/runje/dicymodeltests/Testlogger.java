package games.runje.dicymodeltests;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 05.10.2014.
 */
public class Testlogger
{
    public static String LogKey = "Test";

    public static void log(String message)
    {
        Logger.logInfo(LogKey, message);
    }
}
