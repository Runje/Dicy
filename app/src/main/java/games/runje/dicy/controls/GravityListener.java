package games.runje.dicy.controls;

import android.view.View;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.animatedData.AnimationHandler;
import games.runje.dicy.animatedData.FallingAnimation;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.communication.messages.GravityMessage;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 06.01.2015.
 */
public class GravityListener implements View.OnClickListener
{
    Gravity gravity;
    private AnimatedBoard board;
    private GameControls controls;
    private AnimatedGamemaster gamemaster;

    public GravityListener(Gravity g, AnimatedGamemaster gm, Controls controls, AnimatedBoard board)
    {
        this.gravity = g;
        this.controls = controls;
        this.gamemaster = gm;
        this.board = board;
    }

    @Override
    public void onClick(View view)
    {
        if (gamemaster != null)
        {
            gamemaster.updateGravity(this.gravity);
            gamemaster.sendMessageToServer(new GravityMessage(this.gravity));
        }
        else
        {
            updateGravity(this.gravity);
        }
    }

    public void updateGravity(Gravity gravity)
    {
        board.setGravity(gravity);
        switch (gravity)
        {

            case Up:
                board.getBoardLayout().setYOffset(0);
                break;
            case Down:
                board.getBoardLayout().setYOffset(1);
                break;
            case Right:
                board.getBoardLayout().setXOffset(1);
                break;
            case Left:
                board.getBoardLayout().setXOffset(0);
                break;
        }

        controls.setEnabledControls(false);
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                controls.setEnabledControls(true);
            }
        });

        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                Coords pos = new Coords(i, j);
                AnimatedBoardElement aE = board.getAnimatedElement(pos);

                animationHandler.addAnimation(new FallingAnimation(aE, pos, board, animationHandler));
            }
        }

        animationHandler.start();
        controls.update();
    }
}
