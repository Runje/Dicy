package games.runje.dicy.animatedData;

import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import games.runje.dicy.controller.Gamemaster;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 04.01.2015.
 */
public class PointsAnimation implements Animation.AnimationListener
{
    private final ArrayList<PointElement> elements;
    private ArrayList<TextView> tvs;

    public PointsAnimation(ArrayList<PointElement> elements)
    {
        this.elements = elements;
        this.tvs = new ArrayList<>();
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
        ((AnimatedBoard) Gamemaster.getInstance().getBoard()).highlightElements(elements);
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        AnimatedBoard board = ((AnimatedBoard) Gamemaster.getInstance().getBoard());
        ArrayList<Coords> coords = Coords.pointElementsToCoords(elements);

        for (Coords c : coords)
        {
            board.getAnimatedElement(c).remove();
            board.getAnimatedElement(c).setValue(0);
        }

        // Delete Points
        for (TextView tv : tvs)
        {
            ((RelativeLayout) tv.getParent()).removeView(tv);
        }

        Gamemaster.getInstance().anmiationEnded();
        Gamemaster.getInstance().updaterAfterPoints();
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }

    public void start()
    {
        Gamemaster.getInstance().startAnimation();
        AnimatedBoard board = ((AnimatedBoard) Gamemaster.getInstance().getBoard());
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
            float x = board.getGameLayout().CoordsToX(pos);
            float y = board.getGameLayout().CoordsToY(pos);
            tv.setX(x);
            tv.setY(y);
            board.getGameLayout().addView(tv);
            tvs.add(tv);
        }

    }
}
