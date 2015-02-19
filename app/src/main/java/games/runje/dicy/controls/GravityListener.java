package games.runje.dicy.controls;

import android.view.View;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.animatedData.FallAnimation;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 06.01.2015.
 */
public class GravityListener implements View.OnClickListener
{
    Gravity gravity;
    private AnimatedGamemaster gamemaster;

    public GravityListener(Gravity g, AnimatedGamemaster gm)
    {
        this.gravity = g;
        this.gamemaster = gm;
    }

    @Override
    public void onClick(View view)
    {
        gamemaster.getBoard().setGravity(this.gravity);
        AnimatedBoard board = (AnimatedBoard) gamemaster.getBoard();

        switch (this.gravity)
        {

            case Up:
                board.getGameLayout().setYOffset(0);
                break;
            case Down:
                board.getGameLayout().setYOffset(1);
                break;
            case Right:
                board.getGameLayout().setXOffset(1);
                break;
            case Left:
                board.getGameLayout().setXOffset(0);
                break;
        }

        gamemaster.disableControls();
        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                Coords pos = new Coords(i, j);
                AnimatedBoardElement aE = board.getAnimatedElement(pos);
                gamemaster.startAnimation();
                new FallAnimation(aE, pos, false, gamemaster).start();
            }
        }
        gamemaster.updateGravity();
    }
}
