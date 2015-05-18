package games.runje.dicy.animatedData;

import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 04.01.2015.
 */
public class PointsAnimation implements Animation.AnimationListener
{
    public static String LogKey;
    private final ArrayList<PointElement> elements;
    private final AnimatedGamemaster gmAnimated;
    private ArrayList<TextView> tvs;

    public PointsAnimation(ArrayList<PointElement> elements, AnimatedGamemaster gmAnimated)
    {
        this.elements = elements;
        this.gmAnimated = gmAnimated;
        this.tvs = new ArrayList<>();
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
        AnimatedBoard board = gmAnimated.getAnimatedBoard();
        board.highlightElements(elements);
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        // Delete Points
        for (TextView tv : tvs)
        {
            ((RelativeLayout) tv.getParent()).removeView(tv);
        }

        gmAnimated.endPointAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }

    public void start()
    {
        AnimatedBoard board = gmAnimated.getAnimatedBoard();
        TranslateAnimation animation = new TranslateAnimation(
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
        animation.setDuration(1000);
        animation.setAnimationListener(this);

        board.getAnimatedElement(new Coords(0, 0)).startAnimation(animation);

        for (PointElement pe : elements)
        {
            TextView tv = new TextView(board.getActivity());
            tv.setText(Integer.toString(pe.getPoints()));
            tv.setTextSize(40);
            tv.setTextColor(Color.BLUE);
            Coords pos = pe.getCoords()[0];
            float x = board.getBoardLayout().CoordsToX(pos);
            float y = board.getBoardLayout().CoordsToY(pos);
            tv.setX(x);
            tv.setY(y);
            board.getBoardLayout().addView(tv);
            tvs.add(tv);
        }
    }
}
