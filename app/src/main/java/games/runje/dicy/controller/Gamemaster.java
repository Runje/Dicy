package games.runje.dicy.controller;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.util.Controls;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 18.10.2014.
 */
public class Gamemaster
{
    private static Gamemaster instance;
    private final String LogKey = "Gamemaster";
    // TODO
    public int points = 0;
    private Board board;
    private Rules rules;
    private int animationEnded = 0;
    private int animationsWillStart = 0;
    private Controls controls;

    private Gamemaster(Board b, Rules r, Controls controls)
    {
        this.board = b;
        this.rules = r;
        this.controls = controls;
    }

    public static Gamemaster getInstance()
    {
        return instance;
    }

    public static void createAnimatedGame(Activity a)
    {
        assert instance == null : "Game is already created";
       /* Board b = new AnimatedBoard(new int[]{3, 3, 1, 2, 2,
                1, 2, 2, 3, 1,
                3, 1, 3, 3, 2,
                3, 2, 3, 2, 2,
                1, 2, 1, 1, 2}, a);*/
        Board b = new AnimatedBoard(5, 5, a);
        Rules rules = new Rules();
        rules.setDiagonalActive(false);
        rules.setMinStraight(7);
        rules.initStraightPoints(2);
        b.setGravity(Gravity.Down);
        Controls controls = new Controls(a);
        instance = new Gamemaster(b, rules, controls);
        Gamemaster.getInstance().update();
    }

    public void anmiationEnded()
    {
        animationEnded++;
    }

    private void update()
    {
        this.controls.update();
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean isAnimationIsRunning()
    {
        return animationEnded != animationsWillStart;
    }

    public void performAction(Action action)
    {
        action.setBoard(this.board);
        if (action.isPossible())
        {
            action.execute();
        }
        else
        {
            Logger.logInfo(LogKey, action + " not possible");
        }
    }

    public void updateAfterSwitch()
    {
        if (isAnimationIsRunning())
        {
            return;
        }

        ((AnimatedBoard) board).consistencyCheck();

        this.animationEnded = 0;
        this.animationsWillStart = 0;

        ArrayList<PointElement> elements = BoardChecker.getAll(board, rules);
        for (PointElement e : elements)
        {
            points += e.getPoints();
        }

        updatePoints();
        board.deleteElements(elements);
    }

    private void updatePoints()
    {
        controls.updatePoints();
    }

    public void updateAfterFall()
    {
        if (isAnimationIsRunning())
        {
            return;
        }
        ((AnimatedBoard) board).consistencyCheck();

        this.animationEnded = 0;
        // check board if there have to be created new elements
        ArrayList<BoardElement> elements = board.recreateElements();
        this.animationsWillStart = elements.size();
        if (elements.size() == 0)
        {
            updateAfterSwitch();
        }
    }

    public Rules getRules()
    {
        return rules;
    }

    public void startAnimation()
    {
        this.animationsWillStart++;
    }

    public RelativeLayout getControls()
    {
        return controls;
    }

    public void disableControls()
    {
        controls.disable();
    }

    public void restart()
    {
        Activity a = ((AnimatedBoard) board).getActivity();
        board = new AnimatedBoard(5, 5, a);
        points = 0;
        AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();
        RelativeLayout b = board.getRelativeLayout();
        b.setId(R.id.board);
        RelativeLayout l = new RelativeLayout(a);
        l.addView(b);

        ((RelativeLayout) controls.getParent()).removeView(controls);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.board);
        params.topMargin = 50;
        l.addView(controls, params);
        a.setContentView(l);
        this.controls.updatePoints();
    }

    public void updaterAfterPoints()
    {
        this.animationEnded = 0;
        ArrayList<BoardElement> fallingElements = board.moveElementsFromGravity();
        this.animationsWillStart = fallingElements.size();
        if (fallingElements.size() == 0)
        {
            ArrayList<BoardElement> rElements = board.recreateElements();
            this.animationsWillStart = rElements.size();
            if (rElements.size() == 0)
            {
                controls.enable();
            }
        }
    }

    public void updateGravity()
    {
        this.controls.updateGravity();
    }
}
