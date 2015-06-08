package games.runje.dicy.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import games.runje.dicy.R;
import games.runje.dicy.util.ViewUtilities;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Utilities;

/**
 * Created by Thomas on 31.05.2015.
 */
public class DicyProgress extends View
{
    private int progress;

    private int maxProgress = 100;

    private int fillColor = Color.YELLOW;

    private int warnColor = Color.RED;
    public final static String LogKey = "DicyProgress";

    public DicyProgress(Context context)
    {
        super(context);
    }

    public DicyProgress(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public DicyProgress(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public int getProgress()
    {
        return progress;
    }

    public void setProgress(int progress)
    {
        this.progress = progress;
        Logger.logInfo(LogKey, "Setting Progress: " + progress);
        postInvalidate();
    }

    public int getMaxProgress()
    {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress)
    {
        this.maxProgress = maxProgress;
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
        paint.setColor(warnColor);


        // max value
        String text = Integer.toString(maxProgress);
        float width = ViewUtilities.getTextWidth(text, paint);
        float height = ViewUtilities.getTextHeight(text, paint);

        // current value
        String maxCurrent = Integer.toString(99999);
        float widthMaxCurrent = ViewUtilities.getTextWidth(maxCurrent, paint);

        float textPadding = 10;

        float barWidth = canvas.getWidth() - widthMaxCurrent - 2 * textPadding - width;
        float barHeight = canvas.getHeight() - getPaddingBottom() - getPaddingTop();

        // max
        float leftText = canvas.getWidth() - width;
        canvas.drawText(text, leftText, barHeight / 2 + height / 2, paint);

        // current
        paint.setColor(fillColor);
        float progressPercent = progress / (float) maxProgress;
        progressPercent = Math.min(progressPercent, 1);



        String progressText = Integer.toString(progress);
        float progressWidth = ViewUtilities.getTextWidth(progressText, paint);
        float progressHeight = ViewUtilities.getTextHeight(progressText, paint);

        float progressPercentWidth = progressPercent * (barWidth);

        if (progress < maxProgress)
        {
            paint.setColor(warnColor);
        }
        else
        {
            paint.setColor(fillColor);
        }

        canvas.drawText(progressText, widthMaxCurrent / 2 - progressWidth / 2, barHeight / 2 + progressHeight / 2, paint);

        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawRect(widthMaxCurrent + textPadding, canvas.getHeight() - barHeight, canvas.getWidth() - textPadding - width, barHeight, paint);

        paint.setStyle(Paint.Style.FILL);


        if (progress != 0)
        {
            canvas.drawRect(widthMaxCurrent + textPadding, canvas.getHeight() - barHeight, widthMaxCurrent + textPadding + progressPercentWidth, barHeight, paint);
        }

        Logger.logInfo(LogKey, String.format("Canvas.height: %d, canvas.width: %d", canvas.getHeight(), canvas.getWidth()));


    }
}
