package games.runje.dicy.animatedData;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 28.10.2014.
 */
public class FallAnimation implements Animation.AnimationListener
{
    private final AnimatedBoardElement element;
    private final Coords to;
    private final AnimatedGamemaster gamemaster;
    private String LogKey = "FallAnimation";
    private boolean update;

    public FallAnimation(AnimatedBoardElement element, Coords to, boolean update, AnimatedGamemaster gm)
    {
        this.gamemaster = gm;
        this.element = element;
        this.to = to;
        this.update = update;
    }

    public FallAnimation(AnimatedBoardElement element, Coords to, AnimatedGamemaster gm)
    {
        this(element, to, true, gm);
    }

    public void start()
    {
        AnimatedBoard board = (AnimatedBoard) gamemaster.getBoard();
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
        AnimatedBoard board = (AnimatedBoard) gamemaster.getBoard();
        AnimatedBoardElement toElement = board.getAnimatedElement(to);
        board.setAnimatedElement(to, element);

        element.setPosition(to);
        element.setX(board.getGameLayout().CoordsToX(to));
        element.setY(board.getGameLayout().CoordsToY(to));

        Logger.logDebug(LogKey, "End from To: " + this.to + " ex: " + element.getX() + ", ey: " + element.getY());

        gamemaster.anmiationEnded();
        if (update)
        {
            gamemaster.updateAfterFall();
        }
        else
        {
            gamemaster.getControls().enable();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
