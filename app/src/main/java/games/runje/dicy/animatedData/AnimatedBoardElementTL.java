package games.runje.dicy.animatedData;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import games.runje.dicy.controller.Action;
import games.runje.dicy.controller.Direction;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicy.controller.SwitchAction;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 16.10.2014.
 */
public class AnimatedBoardElementTL implements View.OnTouchListener
{
    protected Coords position;
    private boolean switchEnabled = true;

    public AnimatedBoardElementTL(Coords position)
    {
        this.position = position;
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
                Gamemaster.getInstance().select(position);
                break;


        }
        return true;
    }

    private boolean switchOnTouch(View view, MotionEvent motionEvent)
    {
        float x1 = 0;
        float x2 = 0;
        float y1 = 0;
        float y2 = 0;
        float dx = 0;
        float dy = 0;
        float offsetX = 65f;
        float offsetY = 85f;
        Direction direction;
        float epsilon = 2f;
        switch (motionEvent.getAction())
        {
            case (MotionEvent.ACTION_DOWN):
                x1 = motionEvent.getX();
                y1 = motionEvent.getY();
                break;
            case (MotionEvent.ACTION_UP):
            {
                x2 = motionEvent.getX();
                y2 = motionEvent.getY();
                dx = x2 - x1 - offsetX;
                dy = y2 - y1 - offsetY;

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
                else
                {
                    Logger.logInfo("Direction", "No Switch" + ", dx = " + dx + ", dy = " + dy + ", lrRatio = " + ratioLeftRight + ", udRatio = " + ratioUpDown);
                    Toast t = Toast.makeText(view.getContext(), "No Switch" + ", dx = " + dx + ", dy = " + dy + ", lrRatio = " + ratioLeftRight + ", udRatio = " + ratioUpDown, Toast.LENGTH_LONG);
                    //t.show();
                    return true;
                }

                Action a = new SwitchAction(position, direction, true);
                Gamemaster.getInstance().performAction(a);
                Logger.logInfo("Direction", direction.toString() + ", dx = " + dx + ", dy = " + dy + ", lrRatio = " + ratioLeftRight + ", udRatio = " + ratioUpDown);
                Toast t = Toast.makeText(view.getContext(), direction.toString() + ", dx = " + dx + ", dy = " + dy + ", lrRatio = " + ratioLeftRight + ", udRatio = " + ratioUpDown, Toast.LENGTH_LONG);
                //t.show();
            }
        }
        return true;
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
