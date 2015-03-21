package games.runje.dicy.animatedData;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 28.10.2014.
 */
public class FallingAnimation implements DicyAnimation
{
    private final AnimatedBoardElement element;
    private final Coords to;
    private AnimationHandler animationHandler;
    private AnimatedBoard board;
    private String LogKey = "FallingAnimation";

    public FallingAnimation(AnimatedBoardElement element, Coords to, AnimatedBoard board, AnimationHandler animationHandler)
    {
        this.element = element;
        this.to = to;
        this.board = board;
        this.animationHandler = animationHandler;

        // statt gamemaster reicht animated board
    }


    public void start()
    {
        float dx = board.getGameLayout().CoordsToX(to) - element.getX();
        float dy = board.getGameLayout().CoordsToY(to) - element.getY();

        TranslateAnimation animation = new TranslateAnimation(
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dx,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dy);
        animation.setDuration(1000);
        animation.setAnimationListener(this);
        element.startAnimation(animation);
        Logger.logDebug(LogKey, "dx: " + dx + ", dy: " + dy + ", from: " + element);
        Logger.logDebug(LogKey, "Start from To: " + this.to + " ex: " + element.getX() + ", ey: " + element.getY() + ", from: " + element);
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
        Logger.logInfo(LogKey, "Starting Fall Animation for pos: " + element.getPosition() + ". To: " + this.to);
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        element.clearAnimation();
        // set new position
        AnimatedBoardElement toElement = board.getAnimatedElement(to);
        board.setAnimatedElement(to, element);

        element.setPosition(to);
        element.setX(board.getGameLayout().CoordsToX(to));
        element.setY(board.getGameLayout().CoordsToY(to));

        Logger.logDebug(LogKey, "End from To: " + this.to + " ex: " + element.getX() + ", ey: " + element.getY());

        animationHandler.endAnimation();


    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
