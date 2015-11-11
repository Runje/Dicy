package games.runje.dicy.animatedData;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.Duration;

/**
 * Created by Thomas on 16.03.2015.
 */
public class AnimationHandler
{
    private Runnable runnable;
    private int animationEnded;
    private List<DicyAnimation> animations;
    private Duration duration;

    public AnimationHandler(List<DicyAnimation> animations, Runnable runnable, Duration duration)
    {
        this.animations = animations;
        this.animationEnded = 0;
        this.runnable = runnable;
        this.duration = duration;
    }

    public AnimationHandler(List<DicyAnimation> animations, Runnable runnable)
    {
        this(animations, runnable, Duration.Normal);
    }

    public AnimationHandler(Runnable runnable)
    {
        this(new ArrayList<DicyAnimation>(), runnable);
    }

    public void addAnimation(DicyAnimation animation)
    {
        animations.add(animation);
    }

    public void start()
    {
        if (animations.size() == 0)
        {
            runnable.run();
            return;
        }

        for (DicyAnimation animation : animations)
        {
            animation.start();
        }
    }

    public void endAnimation()
    {
        animationEnded++;

        if (animationEnded == animations.size())
        {
            runnable.run();
        }
    }

    public Duration getDuration()
    {
        return duration;
    }

    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }
}
