package games.runje.dicy.util;

import android.app.Activity;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * Created by Thomas on 19.01.2015.
 */
public class ViewUtilities
{
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


}
