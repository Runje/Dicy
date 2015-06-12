package games.runje.dicymodel;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.GameState;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.03.2015.
 */
public abstract class AbstractGamemaster
{
    protected Board board;
    protected GameState state;
    protected Rules rules;
    protected Move lastMove;
    protected GameControls controls;
    protected LocalGame game;
    protected Skill activeSkill;
    protected ArrayList<BoardElement> recreateElements;
    public static String LogKey = "AbstractGamemaster";

    protected AbstractGamemaster(Rules rules, List<Player> players)
    {
        this(rules, players, Board.createBoardNoPoints(rules));
    }

    protected AbstractGamemaster(Rules rules, List<Player> players, Board board)
    {
        this.board = board;
        this.rules = rules;
        // TODO: starting player
        this.game = new LocalGame(rules.getPointLimit(), rules.getLengthFactor(), players, 0);
    }

    public void switchElements(Coords first, Coords second)
    {
        controls.setEnabledControls(false);
        Logger.logDebug(LogKey, "Switching elements");
        this.lastMove = new Move(first, second);
        startSwitchAnimation(first, second);
    }

    public void executeSkillFromUser(Skill s)
    {
        this.activeSkill = s;
        stateTransition(GameState.Wait);
        s.startWaiting(board, this);
    }

    public void endWait(Coords pos)
    {
        if (pos != null)
        {
            activeSkill.setPos(pos);
        }

        activeSkill.execute(board, this);
    }

    protected void startSwitchAnimation(Coords first, Coords second)
    {
        Logger.logDebug(LogKey, "Start Switching elements");
        board.switchElements(first, second);
        endSwitchAnimation();
    }

    public GameState getState()
    {
        return state;
    }

    public void stateTransition(GameState state)
    {
        Logger.logInfo(LogKey, "State Transition to " + state.toString());
        GameState oldState = this.state;
        this.state = state;

        // disable controls
        this.controls.setEnabledControls(false);
        switch (state)
        {
            case Normal:
                transitionToNormal();

                break;
            case Switched:
                ArrayList<PointElement> elements = BoardChecker.getAll(board, rules);
                int points = Utilities.getPointsFrom(elements);
                Logger.logDebug(LogKey, "Points after switch: " + points);
                Logger.logDebug(LogKey, "Board after switch: " + board);
                game.addPointElements(elements, board, true);

                if (points == 0)
                {
                    switchback();
                }
                else
                {
                    startPointAnimation(elements);
                }
                break;
            case Points:
                ArrayList<PointElement> deleteElements = BoardChecker.getAll(board, rules);
                startDeleteAnimation(deleteElements);
                break;
            case Deleted:
                startFallAnimation();
                break;
            case Fallen:
                //TODO: Einheitlich, entweder alles in start Animation oder alles hier!
                recreateElements = board.getRecreateElements();
                startRecreateAnimation();
                break;
            case Recreated:
                elements = BoardChecker.getAll(board, rules);
                points = Utilities.getPointsFrom(elements);
                game.addPointElements(elements, board, true);
                if (points == 0)
                {
                    game.endSwitch();
                    startRecreateBoard();
                }
                else
                {
                    startPointAnimation(elements);
                }
                break;
            case Wait:
                getGame().setStrikePossible(false);
                controls.setPointLimit(0);
                // TODO: should handle the skill
                board.setEnabled(true);
                break;
            case Executed:
                ArrayList<PointElement> pointElements = BoardChecker.getAll(board, rules);
                int Executedpoints = Utilities.getPointsFrom(pointElements);
                game.addPointElements(pointElements, board, false);
                if (Executedpoints == 0)
                {
                    stateTransition(GameState.Normal);
                }
                else
                {
                    startPointAnimation(pointElements);
                }
                break;
        }

        // enable
        if (this.state == GameState.Normal)
        {
            this.controls.setEnabledControls(true);
        }
        this.controls.update();

    }

    protected void transitionToNormal()
    {
        getGame().setStrikePossible(true);
        controls.setPointLimit(rules.getPointLimit());
    }

    private void startRecreateBoard()
    {
        if (BoardChecker.getPossiblePointMoves(board, rules).size() == 0)
        {
            Logger.logInfo(LogKey, "Recreating Board");
            startRecreateBoardAnimation();
        }
        else
        {
            endRecreateBoardAnimation();
        }
    }

    protected void startRecreateBoardAnimation()
    {
        Logger.logDebug(LogKey, "Old board: " + board.toString());
        board.recreateBoard();
        Logger.logDebug(LogKey, "New board: " + board.toString());
        endRecreateBoardAnimation();
    }

    protected void endRecreateBoardAnimation()
    {
        stateTransition(GameState.Normal);
    }

    protected void startFallAnimation()
    {
        board.moveElementsFromGravity();
        endFallAnimation();
    }

    public void endFallAnimation()
    {
        stateTransition(GameState.Fallen);
    }

    protected void startRecreateAnimation()
    {
        board.recreateElements(recreateElements);
        endRecreateAnimation();
    }

    public void cancelWaiting()
    {
        stateTransition(GameState.Normal);
    }

    public void endRecreateAnimation()
    {
        Logger.logDebug(LogKey, "Board after recreate: " + board);
        stateTransition(GameState.Recreated);
    }


    protected void startDeleteAnimation(ArrayList<PointElement> elements)
    {
        board.deleteElements(elements);
        endDeleteAnimation();
    }

    protected void endDeleteAnimation()
    {
        stateTransition(GameState.Deleted);
    }

    protected void switchback()
    {
        Logger.logDebug(LogKey, "Switching back");
        startSwitchAnimation(lastMove.getSecond(), lastMove.getFirst());
    }

    protected void startPointAnimation(ArrayList<PointElement> elements)
    {
        Logger.logDebug(LogKey, "Start Point animated Animation");
        endPointAnimation();
    }

    public void endPointAnimation()
    {
        stateTransition(GameState.Points);
    }

    public void endSwitchAnimation()
    {
        if (this.state == GameState.Switched)
        {
            // back to normal, was a switchback
            stateTransition(GameState.Normal);
        }
        else
        {
            stateTransition(GameState.Switched);
        }
    }

    public Rules getRules()
    {
        return rules;
    }

    public void next()
    {
        game.moveEnds();
        controls.setEnabledControls(true);
        controls.update();
    }

    public GameControls getControls()
    {
        return controls;
    }

    public LocalGame getGame()
    {
        return game;
    }

    public boolean areControlsEnabled()
    {
        return this.controls.areControlsEnabled();
    }

    public void endExecuteSkill()
    {
        stateTransition(GameState.Executed);
    }


    public Board getBoard()
    {
        return board;
    }
}
