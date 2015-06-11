package games.runje.dicy.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import games.runje.dicy.util.ViewUtilities;
import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 31.05.2015.
 */
public class DicyProgress2 extends View
{
    private int currentPoints;
    private int futurePoints;

    private int opponentPoints = 0;


    private int goal = 100;

    private int fillColor = Color.YELLOW;

    private int warnColor = Color.RED;
    private int goodColor = Color.BLUE;
    public final static String LogKey = "DicyProgress";

    public DicyProgress2(Context context)
    {
        super(context);
    }

    public DicyProgress2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DicyProgress2(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public int getCurrentPoints()
    {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints)
    {
        this.currentPoints = currentPoints;
        Logger.logInfo(LogKey, "Setting Progress: " + currentPoints);
        postInvalidate();
    }

    public void setFuturePoints(int futurePoints)
    {
        this.futurePoints = futurePoints;
    }

    public void setOpponentPoints(int opponentPoints)
    {
        this.opponentPoints = opponentPoints;
    }

    public void setGoal(int goal)
    {
        this.goal = goal;
    }

    public int getFillColor()
    {
        return fillColor;
    }

    public void setFillColor(int fillColor)
    {
        this.fillColor = fillColor;
    }

    public int getWarnColor()
    {
        return warnColor;
    }

    public void setWarnColor(int warnColor)
    {
        this.warnColor = warnColor;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAlpha(255);



        paint.setTextSize(40);
        int color = fillColor;

        if (opponentPoints >= goal && opponentPoints > currentPoints)
        {
            color = warnColor;
        }

        paint.setColor(color);

        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        int redLine = canvas.getHeight() / 4;
        int redValue = Math.max(opponentPoints, goal);

        int currentPointsHeight = canvas.getHeight() - (int) (currentPoints * canvas.getHeight() / (float) (redValue * 4 / 3));
        int futurePointsHeight = canvas.getHeight() - (int) (futurePoints * canvas.getHeight() / (float) (redValue * 4 / 3));

        // current points
        paint.setStyle(Paint.Style.FILL);
        if (currentPoints > 0)
        {
            canvas.drawRect(0, currentPointsHeight, canvas.getWidth(), canvas.getHeight(), paint);
        }

        // future points
        if (futurePoints > 0 && futurePoints > currentPoints)
        {
            paint.setColor(goodColor);
            canvas.drawRect(paint.getStrokeWidth() / 2, futurePointsHeight, canvas.getWidth() - paint.getStrokeWidth() / 2, currentPointsHeight, paint);
        }

        // red line
        paint.setColor(warnColor);
        canvas.drawLine(paint.getStrokeWidth() / 2, redLine, canvas.getWidth() - paint.getStrokeWidth() / 2, redLine, paint);





    }
}
