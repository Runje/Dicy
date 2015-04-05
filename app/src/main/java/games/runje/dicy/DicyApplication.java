package games.runje.dicy;

import android.app.Application;
import android.content.Context;

public class DicyApplication extends Application
{

    private static Context context;

    public static Context getAppContext()
    {
        return DicyApplication.context;
    }

    public void onCreate()
    {
        super.onCreate();
        DicyApplication.context = getApplicationContext();
    }
}