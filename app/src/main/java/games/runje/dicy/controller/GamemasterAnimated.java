package games.runje.dicy.controller;

import android.app.Activity;

import java.util.ArrayList;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimationHandler;
import games.runje.dicy.animatedData.PointsAnimation;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 11.03.2015.
 */
public class GamemasterAnimated extends AbstractGamemaster
{
    private final String LogKey = "GamemasterAnimated";
    AnimatedBoard animatedBoard;
    private Activity activity;

    public GamemasterAnimated(Board board, Rules rules, Activity activity, GameControls controls, LocalGame game)
    {
        super(board, rules, controls, game);
        this.activity = activity;
        animatedBoard = new AnimatedBoard(board, activity, null, this);
        this.board = animatedBoard;
        this.controls = controls;
        controls.setGamemaster(this);
        controls.setAnimatedBoard(animatedBoard);
        // start game when point calculation are ready
        controls.setEnabledControls(false);
        controls.update();
    }


    public AnimatedBoard getAnimatedBoard()
    {
        return animatedBoard;
    }

    @Override
    protected void startSwitchAnimation(Coords first, Coords second)
    {
        Logger.logInfo(LogKey, "Start Animation Switching elements");

        //check if possible
        if (!(second.row < 0 || second.row >= board.getNumberOfRows() ||
                second.column < 0 || second.column >= board.getNumberOfColumns()))
        {
            animatedBoard.switchElements(first, second, true);
        }
        else
        {
            //endSwitchAnimation();
        }



    }

    @Override
    protected void startPointAnimation(ArrayList<PointElement> elements)
    {
        Logger.logInfo(LogKey, "Start Point animated Animation");
        new PointsAnimation(elements, null, this).start();
    }

    @Override
    protected void startDeleteAnimation(ArrayList<PointElement> elements)
    {
        board.deleteElements(elements);
        ArrayList<Coords> coords = Coords.pointElementsToCoords(elements);

        for (Coords c : coords)
        {
            animatedBoard.getAnimatedElement(c).remove();
            animatedBoard.getAnimatedElement(c).setValue(0);
        }

        endDeleteAnimation();
    }

    @Override
    protected void startFallAnimation()
    {
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                endFallAnimation();
            }
        });

        animatedBoard.moveElementsFromGravity(animationHandler);
        //endFallAnimation();
    }

    @Override
    protected void startRecreateAnimation()
    {
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                endRecreateAnimation();
            }
        });

        animatedBoard.recreateElements(recreateElements, animationHandler);
    }


    public Activity getActivity()
    {
        return activity;
    }
}
