package games.runje.dicy.animatedData;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 21.10.2014.
 */
public class SwitchAnimation implements Animation.AnimationListener
{
    private final String LogKey = "SwitchAnimation";
    private final boolean withSwitchBack;
    private AnimatedBoardElement secondImage;
    private AnimatedBoardElement firstImage;
    private boolean update = true;
    private AnimatedGamemaster gamemaster;

    public SwitchAnimation(AnimatedBoardElement firstImage, AnimatedBoardElement secondImage, AnimatedGamemaster gm)
    {
        this.gamemaster = gm;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.withSwitchBack = true;
    }

    public SwitchAnimation(AnimatedBoardElement firstImage, AnimatedBoardElement secondImage, boolean withSwitchBack, AnimatedGamemaster gm)
    {
        this.gamemaster = gm;
        this.firstImage = firstImage;
        this.secondImage = secondImage;
        this.withSwitchBack = withSwitchBack;
    }

    public void start()
    {
        gamemaster.startAnimation();
        gamemaster.disableControls();
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

        AnimatedBoard board = (AnimatedBoard) gamemaster.getBoard();
        board.setAnimatedElement(first, secondImage);
        board.setAnimatedElement(second, firstImage);
        gamemaster.anmiationEnded();

        if (withSwitchBack)
        {
            // switch back
            SwitchAnimation s = new SwitchAnimation(secondImage, firstImage, false, gamemaster);
            s.setNoUpdate();
            s.start();
        }
        else
        {
            if (update)
            {
                gamemaster.updateAfterSwitch();
            }
            else
            {
                gamemaster.getControls().enable();
            }
        }
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
