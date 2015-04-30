package games.runje.dicy.animatedData;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import games.runje.dicy.R;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.DiceListener;
import games.runje.dicy.controller.Direction;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 16.10.2014.
 */
public class AnimatedBoardElementTL implements View.OnTouchListener
{
    private final String LogKey = "AnimatedBoardElementTL";
    protected Coords position;
    private boolean switchEnabled = true;
    private boolean disabled = false;
    private ImageView arrow;
    private float x1;
    private float y1;
    private DiceListener diceListener;

    public AnimatedBoardElementTL(Coords position, DiceListener d)
    {
        this.diceListener = d;

        this.position = position;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public void setPosition(Coords position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return "AnimatedBoardElementTL{" +
                "position=" + position +
                '}';
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (this.arrow == null)
        {
            arrow = new ImageView(view.getContext());
            arrow.setImageResource(R.drawable.arrow2);
        }

        AnimatedLogger.logDebug(LogKey, "Touching Dice " + position + ". Disabled: " + disabled);
        if (disabled)
        {
            return false;
        }

        if (switchEnabled)
        {
            return switchOnTouch(view, motionEvent);
        }
        else
        {
            return selectOnTouch(view, motionEvent);
        }
    }

    private boolean selectOnTouch(View view, MotionEvent motionEvent)
    {
        switch (motionEvent.getAction())
        {
            case (MotionEvent.ACTION_UP):

                diceListener.executeOnTouch(position);
                break;
        }

        return true;
    }

    private boolean switchOnTouch(View view, MotionEvent motionEvent)
    {
        float x2 = 0;
        float y2 = 0;
        float dx = 0;
        float dy = 0;
        Direction direction;
        switch (motionEvent.getAction())
        {
            case (MotionEvent.ACTION_DOWN):
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                float brightness = 100;
                float[] colorMatrix = {1f, 0f, 0f, 0, brightness, // red
                        0f, 1f, 0f, 0, brightness, // green
                        1f, 1f, 1f, 0, brightness, // blue
                        0, 0, 0, 1, 0 // alpha
                };

                ColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

                ((ImageView) view).setColorFilter(filter);
                break;

            case MotionEvent.ACTION_MOVE:
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();

                direction = calcDirection(x2, y2);

                if (direction == null)
                {
                    // remove arrow
                    if (arrow.getParent() != null)
                    {
                        AnimatedLogger.logDebug(LogKey, "Removing arrow");
                        ((ViewGroup) arrow.getParent()).removeView(arrow);
                    }
                    disabled = false;
                    return true;
                }


                if (arrow.getParent() == null)
                {
                    // only add if it has no parent
                    arrow.setX(view.getX());
                    arrow.setY(view.getY());
                    // TODO: arrow
                    ((ViewGroup) view.getParent()).addView(arrow);
                    AnimatedLogger.logDebug(LogKey, "Added arrow");
                }

                AnimatedLogger.logDebug(LogKey, "Setting arrow direction: " + direction);
                arrow.setRotation(directionToRotation(direction));
                disabled = false;
                break;
            case (MotionEvent.ACTION_UP):
            {
                // remove highlighting
                ColorMatrix cm = new ColorMatrix();
                ColorFilter normal = new ColorMatrixColorFilter(cm);
                ((ImageView) view).setColorFilter(normal);

                // remove arrow
                if (arrow.getParent() != null)
                {
                    ((ViewGroup) arrow.getParent()).removeView(arrow);
                }
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();

                direction = calcDirection(x2, y2);
                if (direction == null)
                {
                    AnimatedLogger.logInfo("Direction", "No Switch" + ", dx = " + dx + ", dy = " + dy);
                    disabled = false;
                    return true;
                }

                Coords second = calcSecondPos(position, direction);
                diceListener.executeOnSwitch(position, second);
                AnimatedLogger.logInfo("Direction", direction.toString() + ", dx = " + dx + ", dy = " + dy);
            }
        }
        return true;
    }

    private Coords calcSecondPos(Coords position, Direction direction)
    {
        Coords second = null;
        switch (direction)

        {
            case Up:
                second = new Coords(position.row - 1, position.column);
                break;
            case Down:
                second = new Coords(position.row + 1, position.column);
                break;
            case Left:
                second = new Coords(position.row, position.column - 1);
                break;
            case Right:
                second = new Coords(position.row, position.column + 1);
                break;
        }

        return second;
    }

    private float directionToRotation(Direction direction)
    {
        switch (direction)
        {
            case Up:
                return 270;
            case Down:
                return 90;
            case Left:
                return 180;
            case Right:
                return 0;
        }

        return 0;
    }

    private Direction calcDirection(float x2, float y2)
    {
        float offsetX = 0f;
        float offsetY = 0f;
        float epsilon = 2f;
        float dx = x2 - x1 - offsetX;
        float dy = y2 - y1 - offsetY;

        Direction direction = null;
        float ratioLeftRight = Math.abs(dx) / Math.abs(dy);
        float ratioUpDown = Math.abs(dy) / Math.abs(dx);

        // Use dx and dy to determine the direction
        if (Math.abs(dx) > Math.abs(dy) && ratioLeftRight > epsilon)
        {
            if (dx > 0)
            {
                direction = Direction.Right;
            }
            else
            {
                direction = Direction.Left;
            }
        }
        else if (ratioUpDown > epsilon)
        {
            if (dy > 0)
            {
                direction = Direction.Down;
            }
            else
            {
                direction = Direction.Up;
            }
        }

        return direction;
    }

    public boolean isSwitchEnabled()
    {
        return switchEnabled;
    }

    public void setSwitchEnabled(boolean switchEnabled)
    {
        this.switchEnabled = switchEnabled;
    }
}
