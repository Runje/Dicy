package games.runje.dicy.controller;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.LocalGameActivity;
import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controls.ArenaControls;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicy.game.Game;
import games.runje.dicy.game.LocalGame;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.ai.Simulator;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 18.10.2014.
 */
public class Gamemaster
{
    private static Gamemaster instance;
    private final String LogKey = "Gamemaster";

    private Game game;
    private Board board;
    private Rules rules;
    private int animationEnded = 0;
    private int animationsWillStart = 0;
    private Controls controls;
    private boolean locked = false;

    private Gamemaster(Game game, Board b, Rules r, Controls controls)
    {
        this.game = game;
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
        Board b = new AnimatedBoard(new int[]{3, 3, 1, 2, 1,
                1, 2, 2, 3, 2,
                3, 1, 3, 3, 2,
                3, 2, 3, 2, 2,
                1, 2, 1, 1, 2}, a);
        //Board b = new AnimatedBoard(5, 5, a);
        Rules rules = new Rules();
        rules.setDiagonalActive(false);
        rules.setMinStraight(7);
        rules.setMinXOfAKind(4);
        rules.initStraightPoints(2);
        b.setGravity(Gravity.Down);
        LocalGame g = new LocalGame(1, -1, 9999);
        Controls controls = new ArenaControls(a, g);
        instance = new Gamemaster(g, b, rules, controls);
        Gamemaster.getInstance().update();
    }

    public static void createLocalGame(LocalGameActivity activity, List<String> players, int length, Rules rules)
    {
        Board b = AnimatedBoard.createBoardNoPoints(5, 5, activity, rules);
        b.setGravity(Gravity.Down);
        rules.setPointLimit(Simulator.getLimit(rules, b));
        LocalGame game = new LocalGame(rules.getPointLimit(), rules.getPointLimit() * length, players);
        for (Player p : game.getPlayers())
        {
            if (p.isAi())
            {
                new AIController(p, activity);
            }
        }
        Controls controls = new LocalGameControls(activity, game);
        instance = new Gamemaster(game, b, rules, controls);

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
        return animationEnded != animationsWillStart || locked;
    }

    public void performAction(Action action)
    {
        action.setBoard(this.board);
        if (action.isPossible())
        {
            locked = true;
            action.execute();
            locked = false;
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

        locked = true;
        ((AnimatedBoard) board).consistencyCheck();

        this.animationEnded = 0;
        this.animationsWillStart = 0;

        ArrayList<PointElement> elements = BoardChecker.getAll(board, rules);
        int points = Utilities.getPointsFrom(elements);
        game.addSwitchPoints(points);

        updatePoints();
        board.deleteElements(elements);
        locked = false;
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

        locked = true;
        ((AnimatedBoard) board).consistencyCheck();

        this.animationEnded = 0;
        // check board if there have to be created new elements
        ArrayList<BoardElement> elements = board.recreateElements();
        this.animationsWillStart = elements.size();
        locked = false;
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

    public Controls getControls()
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
        board = new AnimatedBoard(board.getNumberOfRows(), board.getNumberOfColumns(), a);
        AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();
        RelativeLayout b = board.getGameLayout();
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
        if (isAnimationIsRunning())
        {
            return;
        }

        locked = true;

        this.animationEnded = 0;
        ArrayList<BoardElement> fallingElements = board.moveElementsFromGravity();
        this.animationsWillStart = fallingElements.size();
        if (fallingElements.size() == 0)
        {
            ArrayList<BoardElement> rElements = board.recreateElements();
            this.animationsWillStart = rElements.size();
            if (rElements.size() == 0)
            {
                // end move
                game.endSwitch();

                controls.update();
                // check if moves are possible
                ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

                if (moves.size() == 0)
                {
                    // recreate board
                    Logger.logInfo(LogKey, "No more moves possible");
                    Activity a = ((AnimatedBoard) board).getActivity();
                    this.board = new AnimatedBoard(board.getNumberOfRows(), board.getNumberOfColumns(), a);
                    AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();
                    RelativeLayout b = board.getGameLayout();
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

                controls.enable();
            }
        }

        locked = false;
    }

    public void updateGravity()
    {
        this.controls.updateGravity();
    }

    public void next()
    {
        locked = true;
        Logger.logInfo(LogKey, "Next");
        game.moveEnds();
        controls.enable();

        controls.update();
        locked = false;
    }

    public Game getGame()
    {
        return game;
    }
}
