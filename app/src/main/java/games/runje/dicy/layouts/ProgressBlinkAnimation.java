package games.runje.dicy.layouts;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Thomas on 09.08.2015.
 */
public class ProgressBlinkAnimation extends Animation
{
    private final DicyProgress progress;

    public ProgressBlinkAnimation(DicyProgress progress)
    {
        this.progress = progress;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        int newAlpha = (int) (interpolatedTime * 255);
        progress.setPointLimitAlpha(newAlpha);
        progress.postInvalidate();
    }
}
