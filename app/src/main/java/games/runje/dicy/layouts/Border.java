package games.runje.dicy.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import games.runje.dicy.R;
import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 26.03.2015.
 */
public class Border extends View
{
    private Paint paint;
    private boolean active;
    public final static String LogKey = "Border";

    public Border(Context context)
    {
        super(context);

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.dicy_yellow));
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        Paint paint = getPaint();
        if (active)
        {
            Logger.logDebug(LogKey, "Active");
            paint.setStyle(Paint.Style.FILL);
        }
        else
        {
            Logger.logDebug(LogKey, "Inactive");
            paint.setStyle(Paint.Style.STROKE);
        }

        canvas.drawPaint(paint);


    }

    public Paint getPaint()
    {
        return paint;
    }

    public void setActive(boolean active) {
        this.active = active;
        postInvalidate();
    }

    public boolean isActive() {
        return active;
    }
}
