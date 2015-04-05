package games.runje.dicy.animatedData;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 28.10.2014.
 */
public class FallAnimation implements Animation.AnimationListener
{
    private final AnimatedBoardElement element;
    private final Coords to;
    private final AnimatedGamemaster gamemaster;
    private GamemasterAnimated gmAnimated;
    private String LogKey = "FallAnimation";
    private boolean update;

    public FallAnimation(AnimatedBoardElement element, Coords to, boolean update, AnimatedGamemaster gm, GamemasterAnimated gmAnimated)
    {
        this.gamemaster = gm;
        this.element = element;
        this.to = to;
        this.update = update;
        this.gmAnimated = gmAnimated;
    }

    public FallAnimation(AnimatedBoardElement element, Coords to, AnimatedGamemaster gm, GamemasterAnimated gmAnimated)
    {
        this(element, to, true, gm, gmAnimated);
    }

    public void start()
    {
        AnimatedBoard board = null;
        if (gamemaster != null)
        {
            board = (AnimatedBoard) gamemaster.getBoard();
        }
        else
        {
            board = gmAnimated.getAnimatedBoard();
        }

        float dx = board.getBoardLayout().CoordsToX(to) - element.getX();
        float dy = board.getBoardLayout().CoordsToY(to) - element.getY();

        TranslateAnimation animation = new TranslateAnimation(
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dx,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, dy);
        animation.setDuration(1000);
        animation.setAnimationListener(this);
        element.startAnimation(animation);
        AnimatedLogger.logDebug(LogKey, "dx: " + dx + ", dy: " + dy + ", from: " + element);
        AnimatedLogger.logDebug(LogKey, "Start from To: " + this.to + " ex: " + element.getX() + ", ey: " + element.getY() + ", from: " + element);
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
        AnimatedLogger.logInfo(LogKey, "Starting Fall Animation for pos: " + element.getPosition() + ". To: " + this.to);
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        element.clearAnimation();
        // set new position
        AnimatedBoard board = null;
        if (gamemaster != null)
        {
            board = (AnimatedBoard) gamemaster.getBoard();
        }
        else
        {
            board = gmAnimated.getAnimatedBoard();
        }
        AnimatedBoardElement toElement = board.getAnimatedElement(to);
        board.setAnimatedElement(to, element);

        element.setPosition(to);
        element.setX(board.getBoardLayout().CoordsToX(to));
        element.setY(board.getBoardLayout().CoordsToY(to));

        AnimatedLogger.logDebug(LogKey, "End from To: " + this.to + " ex: " + element.getX() + ", ey: " + element.getY());

        if (gamemaster != null)
        {

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
        else
        {
            gmAnimated.endRecreateAnimation();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }
}
