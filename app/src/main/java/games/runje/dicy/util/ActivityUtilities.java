package games.runje.dicy.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.nio.ByteBuffer;

import games.runje.dicy.R;
import games.runje.dicymodel.SavedGame;
import games.runje.dicymodel.communication.MessageConverter;

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
}
