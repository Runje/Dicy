package games.runje.dicy.controls;

import android.view.View;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.animatedData.FallAnimation;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 06.01.2015.
 */
public class GravityListener implements View.OnClickListener
{
    Gravity gravity;

    public GravityListener(Gravity g)
    {
        this.gravity = g;
    }

    @Override
    public void onClick(View view)
    {
        Gamemaster.getInstance().getBoard().setGravity(this.gravity);
        AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();

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
        Gamemaster.getInstance().disableControls();
        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                Coords pos = new Coords(i, j);
                AnimatedBoardElement aE = board.getAnimatedElement(pos);
                Gamemaster.getInstance().startAnimation();
                new FallAnimation(aE, pos, false).start();
            }
        }
        Gamemaster.getInstance().updateGravity();
    }
}
