package games.runje.dicymodel;

import java.util.ArrayList;

import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.GameState;
import games.runje.dicymodel.game.LocalGame;

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

    protected ArrayList<BoardElement> recreateElements;

    protected AbstractGamemaster(Board board, Rules rules, GameControls controls, LocalGame game)
    {
        this.board = board;
        this.rules = rules;
        this.controls = controls;
        this.game = game;
    }

    public void switchElements(Coords first, Coords second)
    {
        System.out.println("Switching elements");
        this.lastMove = new Move(first, second);
        startSwitchAnimation(first, second);
    }

    protected void startSwitchAnimation(Coords first, Coords second)
    {
        System.out.println("Start Switching elements");
        board.switchElements(first, second);
        endSwitchAnimation();
    }

    public GameState getState()
    {
        return state;
    }

    public void stateTransition(GameState state)
    {
        System.out.println("State Transition to " + state.toString());
        GameState oldState = this.state;
        this.state = state;
        this.controls.setEnabledControls(this.state == GameState.Normal);
        switch (state)
        {
            case Normal:
                break;
            case Switched:


                ArrayList<PointElement> elements = BoardChecker.getAll(board, rules);
                int points = Utilities.getPointsFrom(elements);
                game.addPointElements(elements, board);
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
                game.addPointElements(elements, board);
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
                break;
            case Executed:
                break;
        }


        this.controls.update();

    }

    private void startRecreateBoard()
    {
        if (BoardChecker.getPossiblePointMoves(board, rules).size() == 0)
        {
            System.out.println("Recreating Board");
            startRecreateBoardAnimation();
        }
        else
        {
            endRecreateBoardAnimation();
        }
    }

    private void startRecreateBoardAnimation()
    {
        board.recreateBoard();
        endRecreateBoardAnimation();
    }

    private void endRecreateBoardAnimation()
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

    public void endRecreateAnimation()
    {
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
        startSwitchAnimation(lastMove.getSecond(), lastMove.getFirst());
    }

    protected void startPointAnimation(ArrayList<PointElement> elements)
    {
        System.out.println("Start Point animated Animation");
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
        controls.update();
    }

    public GameControls getControls()
    {
        return controls;
    }
}
