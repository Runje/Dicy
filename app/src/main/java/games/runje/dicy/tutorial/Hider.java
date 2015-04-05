package games.runje.dicy.tutorial;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Thomas on 04.04.2015.
 */
public class Hider extends View
{
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
}
