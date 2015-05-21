package games.runje.dicy.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import games.runje.dicy.R;

/**
 * Created by Thomas on 26.03.2015.
 */
public class Border extends View
{
    private Paint paint;
    private boolean active;

    public Border(Context context)
    {
        super(context);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
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
        int radius;
        radius = 100;
        Paint paint = getPaint();
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors
        //paint.setColor(Color.parseColor("#CD5C5C"));
        if (active)
        {
            paint.setStyle(Paint.Style.FILL);
        }
        else
        {
            paint.setStyle(Paint.Style.STROKE);
        }

        canvas.drawRect(0, radius, radius, 0, paint);


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
