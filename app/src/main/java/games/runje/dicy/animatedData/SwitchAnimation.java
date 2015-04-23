package games.runje.dicy.animatedData;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 21.10.2014.
 */
public class SwitchAnimation implements Animation.AnimationListener
{
    private final String LogKey = "SwitchAnimation";
    private final boolean withSwitchBack;
    private GamemasterAnimated gmAnimated;
    private AnimatedBoardElement secondImage;
    private AnimatedBoardElement firstImage;
    private boolean update = true;

    public SwitchAnimation(AnimatedBoardElement firstImage, AnimatedBoardElement secondImage, GamemasterAnimated gmAnimated)
    {
        this.gmAnimated = gmAnimated;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.withSwitchBack = true;
    }

    public SwitchAnimation(AnimatedBoardElement firstImage, AnimatedBoardElement secondImage, boolean withSwitchBack, GamemasterAnimated gmAnimated)
    {
        this.gmAnimated = gmAnimated;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.withSwitchBack = withSwitchBack;
    }

    public void start()
    {
        float dx = firstImage.getX() - secondImage.getX();
        float dy = firstImage.getY() - secondImage.getY();
        TranslateAnimation first = this.createAnimation(-dx, -dy, false);
        TranslateAnimation second = this.createAnimation(dx, dy, true);
        firstImage.startAnimation(first);
        secondImage.startAnimation(second);
    }

    private TranslateAnimation createAnimation(float dx, float dy, boolean withListener)
    {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dx,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dy);
        animation.setDuration(500);

        if (withListener)
        {
            animation.setAnimationListener(this);
        }

        return animation;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        firstImage.clearAnimation();
        secondImage.clearAnimation();
        float firstX = firstImage.getX();
        float firstY = firstImage.getY();
        firstImage.setX(secondImage.getX());
        firstImage.setY(secondImage.getY());
        secondImage.setX(firstX);
        secondImage.setY(firstY);

        int firstId = firstImage.getId();
        firstImage.setId(secondImage.getId());
        secondImage.setId(firstId);

        // Touchlistener
        Coords first = firstImage.getPosition();
        Coords second = secondImage.getPosition();
        firstImage.setPosition(second);
        secondImage.setPosition(first);

        AnimatedBoard aB = gmAnimated.getAnimatedBoard();
        aB.setAnimatedElement(first, secondImage);
        aB.setAnimatedElement(second, firstImage);
        gmAnimated.endSwitchAnimation();
    }

    /**
     * No UpdateAfterSwitch will be called.
     */
    private void setNoUpdate()
    {
        this.update = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
