package games.runje.dicy.controller;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.animatedData.AnimationHandler;
import games.runje.dicy.animatedData.FallingAnimation;
import games.runje.dicy.animatedData.PointsAnimation;
import games.runje.dicy.animatedData.SwitchAnimation;
import games.runje.dicy.animatedData.animatedSkills.AnimatedSkill;
import games.runje.dicy.controls.ControlHandler;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 11.03.2015.
 */
public class AnimatedGamemaster extends AbstractGamemaster implements BoardListener, AIControllerHandler, ControlHandler
{
    public static final String LogKey = "GamemasterAnimated";
    protected Activity activity;
    AnimatedBoard animatedBoard;

    public AnimatedGamemaster(List<Player> players, Rules rules, Activity activity)
    {
        this(players, rules, activity, Board.createBoardNoPoints(rules));
    }

    public AnimatedGamemaster(List<Player> players, Rules rules, Activity activity, Board board)
    {
        super(rules, players, board);
        this.activity = activity;

        // start AI
        for (Player pl : game.getPlayers())
        {
            if (pl.isAi())
            {
                new AIController(pl, activity, this);
            }
        }

        for (Player player : game.getPlayers())
        {
            List<Skill> animatedSkills = new ArrayList<>();
            for (Skill skill : player.getSkills())
            {
                animatedSkills.add(AnimatedSkill.create(skill));
            }

            player.setSkills(animatedSkills);
        }

        animatedBoard = new AnimatedBoard(board, activity, this);
        this.board = animatedBoard;
        this.controls = new Controls(activity, this, game);
        controls.setEnabledControls(true);
        controls.update();
    }

    @Override
    public void setEnabledBoard(boolean enabled)
    {
        this.animatedBoard.setEnabled(enabled);
    }

    @Override
    public boolean isEnabledBoard()
    {
        return this.animatedBoard.isEnabled();
    }

    public AnimatedBoard getAnimatedBoard()
    {
        return animatedBoard;
    }

    @Override
    public int getPointsFromSwitch(Coords position, Coords second)
    {
        if (!Utilities.coordsInBoard(second, board))
        {
            return 0;
        }

        Board b = new Board(board);
        b.switchElements(position, second);
        return Utilities.getPointsFrom(BoardChecker.getAll(b, rules));
    }

    @Override
    protected void startSwitchAnimation(Coords first, Coords second)
    {
        Logger.logDebug(LogKey, "Start Animation Switching elements");

        //check if possible
        if (!(second.row < 0 || second.row >= board.getNumberOfRows() ||
                second.column < 0 || second.column >= board.getNumberOfColumns()))
        {
            AnimatedLogger.logDebug(LogKey, "Animated Switch, first: " + first + ", second: " + second + ", Board: " + this.board + "\n" + this.animatedBoard);

            board.switchElements(first, second, true, rules);

            AnimatedBoardElement firstImage = animatedBoard.getAnimatedElement(first);
            AnimatedBoardElement secondImage = animatedBoard.getAnimatedElement(second);

            SwitchAnimation s = new SwitchAnimation(firstImage, secondImage, this);
            s.start();
        }
        else
        {
            // enabling controls
            setAllEnabled(true);
        }
    }

    @Override
    protected void startPointAnimation(ArrayList<PointElement> elements)
    {
        Logger.logDebug(LogKey, "Start Point animated Animation");
        new PointsAnimation(elements, this).start();
    }

    @Override
    protected void startDeleteAnimation(ArrayList<PointElement> elements)
    {
        animatedDelete(elements);
        endDeleteAnimation();
    }

    protected void animatedDelete(ArrayList<PointElement> elements)
    {
        board.deleteElements(elements);
        ArrayList<Coords> coords = Coords.pointElementsToCoords(elements);

        for (Coords c : coords)
        {
            animatedBoard.getAnimatedElement(c).remove();
            animatedBoard.getAnimatedElement(c).setValue(0);
        }
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




    public void switchElementsFromUser(Coords first, Coords second)
    {
        switchElements(first, second);
    }

    public void nextFromUser()
    {
        next();
    }

    @Override
    public BoardLayout getBoardLayout()
    {
        return animatedBoard.getBoardLayout();
    }

    public void changeGravityFromUser(Gravity gravity)
    {
        changeGravity(gravity);
    }

    public void changeGravity(Gravity gravity)
    {
        if (board.getGravity() == gravity)
        {
            return;
        }

        getAnimatedBoard().getBoardLayout().updateGravity(gravity);
        board.setGravity(gravity);
        controls.save();
        controls.setEnabledControls(false);
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                getControls().restore();
            }
        });

        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                Coords pos = new Coords(i, j);
                AnimatedBoardElement aE = animatedBoard.getAnimatedElement(pos);

                animationHandler.addAnimation(new FallingAnimation(aE, pos, animatedBoard, animationHandler));
            }
        }

        animationHandler.start();
    }

    public void setAllEnabled(boolean b)
    {
        controls.setEnabledControls(b);
        setEnabledBoard(b);
    }

    @Override
    public void executeOnTouch(Coords pos)
    {
        endWait(pos);
    }

    @Override
    public void executeOnSwitch(Coords first, Coords second)
    {
        switchElementsFromUser(first, second);
    }
}
