package games.runje.dicy.tutorial;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 04.04.2015.
 */
public class Hider extends View
{
    private static String LogKey = "Hider";

    public Hider(View v)
    {
        super(v.getContext());
        ImageView image = new ImageView(v.getContext());
        image.setClickable(true);
        image.setBackgroundColor(Color.WHITE);
        image.setAlpha(0.7f);


        //v.setAlpha(v.getAlpha() / 2);
        //v.setClickable(false);

        ViewGroup parent = (ViewGroup) image.getParent();

        parent.addView(image, v.getWidth(), v.getHeight());
        image.setX(v.getX());
        image.setY(v.getY());
    }

    public static View createHideView(Context c)
    {
        TextView image = new TextView(c);
        //ImageView image = new ImageView(c);
        image.setClickable(true);
        //image.setText("Hidden");
        image.setBackgroundColor(Color.BLACK);
        image.setAlpha(0.5f);
        image.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Logger.logDebug(LogKey, "Click Hider");
            }
        });

        image.bringToFront();

        return image;
    }
}
