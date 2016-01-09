package games.runje.dicy.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicy.LocalGameActivity;
import games.runje.dicy.OptionActivity;
import games.runje.dicy.R;
import games.runje.dicy.StartActivity;
import games.runje.dicy.statistics.NamesDB;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.SavedGame;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.GameMode;
import games.runje.dicymodel.skills.ChangeSkill;
import games.runje.dicymodel.skills.HelpSkill;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 19.01.2015.
 */
public class ActivityUtilities
{
    public static final String KEY_RESUME_GAME = "resume game";
    public static final String KEY_GAME = "game";
    public static int getActionBarHeight(Activity a)
    {
        int actionBarHeight = 0;
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (a.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, a.getResources().getDisplayMetrics());
        }
        else
        {
            // TODO: raise exception???
        }

        return actionBarHeight;
    }

    public static RelativeLayout.LayoutParams createRelativeLayoutParams()
    {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static float getTextWidth(String text, android.graphics.Paint paint)
    {
        return paint.measureText(text);
    }

    public static float getTextHeight(String text, Paint paint)
    {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    public static boolean removeView(View v)
    {
        if (v.getParent() instanceof ViewGroup)
        {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
            {
                parent.removeView(v);
                return true;
            }
        }

        return false;
    }

    public static void setGameResumeable(Context context, boolean b)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.game_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_RESUME_GAME, b);
        editor.commit();

    }

    public static boolean getGameResumeable(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.game_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_RESUME_GAME, false);
    }

    public static SavedGame getSavedGame(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.game_file_key), Context.MODE_PRIVATE);
        String gameString = sharedPreferences.getString(KEY_GAME, "");
        byte[] bytes = Base64.decode(gameString, Base64.NO_WRAP);
        return MessageConverter.byteToSavedGame(ByteBuffer.wrap(bytes));
    }

    public static void saveGame(Context context, SavedGame savedGame)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.game_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GAME, Base64.encodeToString(MessageConverter.savedGameToByte(savedGame), Base64.NO_WRAP));
        editor.commit();
    }

    public static Player playerStatisticsToPlayer(PlayerStatistic statistic, List<Skill> skills)
    {
        return new Player(statistic.getName(), Strategy.getStrategy(statistic.getStrategy()), statistic.getId(), skills);
    }

    public static void createRandomPlayer(Context context)
    {
        Random r = new Random();
        List<Skill> skills1 = new ArrayList<Skill>();
        skills1.add(new ChangeSkill(1, 6));
        skills1.add(new HelpSkill(6, 6));
        skills1.add(new ShuffleSkill(5, 6));
            Strategy strategy = new Strategy(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble());
        String name = NamesDB.getRandomName();

        SQLiteHandler handler = new SQLiteHandler(context);
        handler.createPlayer(name, strategy);
        PlayerStatistic playerStatistic = handler.getPlayer(name);
        Log.d("Created Player", playerStatistic.toString() + ", Strategy: " + strategy.toString());


    }

    public static void startGame(Context context, List<Player> players, Rules rules, GameMode gameMode)
    {
        final Intent intent = new Intent(context, LocalGameActivity.class);
        final Bundle b = saveToBundle(players, rules, gameMode);
        ActivityUtilities.setGameResumeable(context, false);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    private static Bundle saveToBundle(List<Player> players, Rules rules, GameMode gameMode)
    {
        Bundle intent = new Bundle();
        intent.putStringArray(OptionActivity.Player1Intent, new String[] { players.get(0).getName(), players.get(1).getName()});
        intent.putString(OptionActivity.LengthIntent, rules.getGameLength().toString());
        intent.putString(OptionActivity.Player1Skill1Intent, players.get(0).getSkills().get(0).getName());
        intent.putString(OptionActivity.Player1Skill2Intent, players.get(0).getSkills().get(1).getName());
        intent.putString(OptionActivity.Player1Skill3Intent, players.get(0).getSkills().get(2).getName());
        intent.putString(OptionActivity.Player2Skill1Intent, players.get(1).getSkills().get(0).getName());
        intent.putString(OptionActivity.Player2Skill2Intent, players.get(1).getSkills().get(1).getName());
        intent.putString(OptionActivity.Player2Skill3Intent, players.get(1).getSkills().get(2).getName());

        intent.putInt(OptionActivity.Player1Skill1ValueIntent, players.get(0).getSkills().get(0).getLoadValue());
        intent.putInt(OptionActivity.Player1Skill2ValueIntent,  players.get(0).getSkills().get(1).getLoadValue());
        intent.putInt(OptionActivity.Player1Skill3ValueIntent, players.get(0).getSkills().get(2).getLoadValue());
        intent.putInt(OptionActivity.Player2Skill1ValueIntent,  players.get(1).getSkills().get(0).getLoadValue());
        intent.putInt(OptionActivity.Player2Skill2ValueIntent,  players.get(1).getSkills().get(1).getLoadValue());
        intent.putInt(OptionActivity.Player2Skill3ValueIntent,  players.get(1).getSkills().get(2).getLoadValue());

        intent.putInt(OptionActivity.TimeLimitInSIntent, rules.getTimeLimitInS());
        intent.putBoolean(OptionActivity.TimeLimitCheckedIntent, rules.isTimeLimit());

        intent.putString(OptionActivity.RuleVariantIntent, rules.getRuleVariant().toString());
        intent.putString(OptionActivity.GameModeIntent, gameMode.toString());


        return intent;
    }
}
