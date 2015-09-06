package games.runje.dicy.controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games.runje.dicy.LocalGameActivity;
import games.runje.dicy.animatedData.AnimatedBoardElementTL;
import games.runje.dicy.animatedData.FallingAnimation;
import games.runje.dicy.animatedData.PointsAnimation;
import games.runje.dicy.layouts.Border;
import games.runje.dicy.statistics.GameTable;
import games.runje.dicy.statistics.PlayerTable;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 19.10.2014.
 */
public class AnimatedLogger extends Logger
{
    /*protected static int Debug = 2;
    protected static int Info = 3;
    protected static int Error = 4;*/
    protected static Map<String, Integer> keyMap;
    private static HashMap<String, Integer> keyGroup;
    private static HashMap<String, List<String>> groupMap;

    private static String animations = "Animations";

    @Override
    protected void initLevels()
    {
        Log.d("Logger", "Init Levels");
        LogLevel = 3;
        keyMap = new HashMap<>();
        groupMap = new HashMap<>();
        keyGroup = new HashMap<>();

        initAnimationGroup();

        // single keys
        keyMap.put(AnimatedBoardElementTL.LogKey, 3);
        keyMap.put(AbstractGamemaster.LogKey, 3);
        keyMap.put(AnimatedGamemaster.LogKey, 3);
        keyMap.put(PlayerTable.LogKey, 3);
        keyMap.put(GameTable.LogKey, 3);
        keyMap.put(LocalGameActivity.LogKey, 3);
        keyMap.put(Border.LogKey, 3);
        keyMap.put(AIController.LogKey, 2);


        for(String key : keyGroup.keySet())
        {
            int value = keyGroup.get(key);
            List<String> keys = groupMap.get(key);

            for (String groupKey:keys)
            {
                keyMap.put(groupKey, value);
            }
        }
    }

    private void initAnimationGroup()
    {
        // group Animation
        keyGroup.put(animations, 3);

        List<String> anims = new ArrayList<>();
        anims.add(FallingAnimation.LogKey);
        anims.add(PointsAnimation.LogKey);

        groupMap.put(animations, anims);
    }

    protected boolean printMessage(int level, String key)
    {
        if (level >= LogLevel)
        {
            return true;
        }

        if (!keyMap.containsKey(key))
        {
            return false;
        }

        return level >= keyMap.get(key);
    }

    @Override
    protected void log(int level, String key, String message)
    {
        if (printMessage(level, key))
        {
            Log.d(key, message);
        }
    }
}
