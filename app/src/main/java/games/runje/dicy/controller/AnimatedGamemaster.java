package games.runje.dicy.controller;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Simulator;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.communication.Message;
import games.runje.dicymodel.communication.SkillMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 18.10.2014.
 */
public class AnimatedGamemaster extends Gamemaster
{
    private static AnimatedGamemaster animatedInstance;
    private final String LogKey = "Gamemaster";
    protected Activity activity;

    protected Game game;
    protected Board board;
    protected Rules rules;
    protected int animationEnded = 0;
    protected int animationsWillStart = 0;
    protected Controls controls;
    protected boolean locked = false;

    protected AnimatedGamemaster(LocalGame game, Board b, Rules r, Activity a)
    {
        this.game = game;
        this.activity = a;
        this.board = new AnimatedBoard(b, a, this);
        this.rules = r;
        this.controls = new LocalGameControls(activity, game, (AnimatedBoard) b, this);
    }

    protected AnimatedGamemaster()
    {

    }

    public static AnimatedGamemaster getInstance()
    {
        return animatedInstance;
    }

    public static void createAnimatedGame(Activity a)
    {
        assert animatedInstance == null : "Game is already created";
        /*AnimatedBoard b = new AnimatedBoard(new int[]{3, 3, 1, 2, 1,
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
        Controls controls = new ArenaControls(a, g, b);
        instance = new AnimatedGamemaster(g, b, rules, controls);
        update();*/
    }

    public static void createLocalGame(Activity activity, List<String> players, int length, Rules rules, List<Strategy> s)
    {
        Board b = Board.createBoardNoPoints(5, 5, rules);
        b.setGravity(Gravity.Down);
        rules.setPointLimit(Simulator.getLimit(rules, b));
        // TODO: gamemaster
        LocalGame game = new LocalGame(rules.getPointLimit(), rules.getPointLimit() * length, players, s);
        for (Player p : game.getPlayers())
        {
            if (p.isAi())
            {
                // TODO: gamemaster
                new AIController(p, activity, null);
            }
        }

        animatedInstance = new AnimatedGamemaster(game, b, rules, activity);

        AnimatedGamemaster.getInstance().update();
    }

    public void anmiationEnded()
    {
        animationEnded++;
    }

    protected void update()
    {
        this.controls.update();
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
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

    public void lock()
    {
        locked = true;
    }

    public void unlock()
    {
        locked = false;
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

        game.addPointElements(elements, board);

        controls.update();
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

    public void setRules(Rules rules)
    {
        this.rules = rules;
    }

    public void startAnimation()
    {
        this.animationsWillStart++;
    }

    public Controls getControls()
    {
        return controls;
    }

    public void setControls(Controls controls)
    {
        this.controls = controls;
    }

    public void disableControls()
    {
        controls.disable();
    }

    public void restart()
    {
        /*Activity a = ((AnimatedBoard) board).getActivity();
        board = new AnimatedBoard(board.getNumberOfRows(), board.getNumberOfColumns(), a);
        AnimatedBoard board = (AnimatedBoard) getBoard();
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
        this.controls.updatePoints();*/
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
                    this.board = new AnimatedBoard(board.getNumberOfRows(), board.getNumberOfColumns(), a, this);
                    AnimatedBoard board = (AnimatedBoard) getBoard();
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
                game.setStrikePossible(true);
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

    public void setGame(LocalGame game)
    {
        this.game = game;
    }

    public void executeSkill(Skill s)
    {
        // TODO

        switch (s.getName())
        {
            case Skill.Help:
                if (s.isExecutable())
                {
                    Move move = BoardChecker.getPossiblePointMoves(board, rules).get(0);
                    ((AnimatedBoard) board).highlightElements(move);
                    s.execute();
                }
                else
                {
                    // TODO: Show dialog
                    Move move = BoardChecker.getPossiblePointMoves(board, rules).get(0);
                    ((AnimatedBoard) board).highlightElements(move);
                    s.execute();
                    game.getPlayingPlayer().setPoints(game.getPlayingPlayer().getPoints() - game.getPointsLimit());
                }

                sendMessageToClient(new SkillMessage(Skill.Help));
                break;

            case Skill.Change:
                if (s.isExecutable())
                {
                    // TODO: Build Skill Executor
                    controls.disable();
                    waitForDiceToGetTouched(s);
                    s.execute();
                }
                else
                {
                    // TODO: Show dialog
                    controls.disable();
                    waitForDiceToGetTouched(s);
                    s.execute();
                    game.getPlayingPlayer().setPoints(game.getPlayingPlayer().getPoints() - game.getPointsLimit());
                }
                break;

        }

        controls.update();
    }

    private void waitForDiceToGetTouched(Skill s)
    {
        AnimatedBoard b = (AnimatedBoard) board;
        b.changeToSelectListener();

        // TODO: SwitchListener still on!
    }

    public void select(Coords position)
    {
        int newValue = 6;
        board.changeElement(position, newValue);
        AnimatedBoard b = (AnimatedBoard) board;
        b.changeToSwitchListener();
        game.setStrikePossible(false);
        sendMessageToClient(new SkillMessage(Skill.Change, position));
        updateAfterSwitch();

        // TODO: execute skill
    }

    public void sendMessageToClient(Message message)
    {
    }
}
