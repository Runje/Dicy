package games.runje.dicy.controller;

import android.util.Log;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 19.10.2014.
 */
public class AnimatedLogger extends Logger
{
    @Override
    protected void log(int level, String key, String message)
    {
        Log.d(key, message);
    }
}
