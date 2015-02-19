package games.runje.dicy.animatedData;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import games.runje.dicy.R;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 13.10.2014.
 */
public class AnimatedBoardElement extends ImageView
{
    private final String LogKey = "AnimatedElement";
    private int value;
    private AnimatedBoardElementTL touchListener;
    private Coords position;


    public AnimatedBoardElement(Context context, BoardElement element, AnimatedGamemaster gm)
    {
        super(context);
        this.value = element.getValue();
        setImageResource(ElementToImageResource(element));
        this.position = element.getPosition();
        this.touchListener = new AnimatedBoardElementTL(this.position, gm);
        this.setOnTouchListener(this.touchListener);
    }

    public static int ElementToImageResource(BoardElement element)
    {
        return valueToImageResource(element.getValue());
    }

    public static int valueToImageResource(int value)
    {
        switch (value)
        {
            case 1:
                return R.drawable.one;

            case 2:
                return R.drawable.two;

            case 3:
                return R.drawable.three;

            case 4:
                return R.drawable.four;

            case 5:
                return R.drawable.five;

            case 6:
                return R.drawable.six;

            case 0:
                return R.drawable.dice3d;
            default:
                assert false;
                return 0;

        }
    }

    public AnimatedBoardElementTL getTouchListener()
    {
        return touchListener;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
        this.setImageResource(valueToImageResource(value));
    }

    public Coords getPosition()
    {
        return position;
    }

    public void setPosition(Coords position)
    {
        this.position = position;
        this.touchListener.setPosition(position);
    }

    @Override
    public String toString()
    {
        return "AnimatedBoardElement{" +
                "touchListener=" + touchListener +
                ", position=" + position +
                '}';
    }

    /**
     * Sets the color to black and white if true else to normal colors.
     *
     * @param blackAndWhite
     */
    public void setHighlight(boolean blackAndWhite)
    {
        float brightness = 100;
        float[] colorMatrix = {0.33f, 0.33f, 0.33f, 0, brightness, // red
                0.33f, 0.33f, 0.33f, 0, brightness, // green
                0.33f, 0.33f, 0.33f, 0, brightness, // blue
                0, 0, 0, 1, 0 // alpha
        };

        ColorFilter blackAndWhiteFilter = new ColorMatrixColorFilter(colorMatrix);
        ColorMatrix cm = new ColorMatrix();
        ColorFilter normal = new ColorMatrixColorFilter(cm);

        if (blackAndWhite)
        {
            this.setColorFilter(blackAndWhiteFilter);
        }
        else
        {
            this.setColorFilter(normal);
        }
    }

    /**
     * Removes this ImageView from its parent.
     */
    public void remove()
    {
        Logger.logInfo(LogKey, "Removing from View: " + this.getPosition() + ". Value = " + this.getValue() + ". NULL: " + (this.getParent() == null));
        // Attention: dependent from the parents layout
        ((RelativeLayout) this.getParent()).removeView(this);
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener l)
    {
        super.setOnTouchListener(l);
        touchListener = (AnimatedBoardElementTL) l;
    }
}
