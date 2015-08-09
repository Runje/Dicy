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
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.03.2015.
 */
public abstract class AbstractGamemaster
{
    public static String LogKey = "AbstractGamemaster";
    protected Board board;
    protected GameState state = GameState.Normal;
    protected Rules rules;
    protected Move lastMove;
    protected GameControls controls;
    protected LocalGame game;
    protected Skill activeSkill;
    protected ArrayList<BoardElement> recreateElements;
    private GameState oldState = GameState.Normal;
    private boolean switchback = false;
    private SavedGame savedGame;

    protected AbstractGamemaster(Rules rules, LocalGame game)
    {
        this(rules, game, Board.createBoardNoPoints(rules));
    }

    protected AbstractGamemaster(Rules rules, LocalGame game, Board board)
    {
        this.board = board;
        this.rules = rules;
        this.game = game;
        saveGame(GameState.Normal);
    }

    public AbstractGamemaster(SavedGame savedGame)
    {
        this(savedGame.getRules(), savedGame.getGame(), savedGame.getBoard());
        this.lastMove = savedGame.getLastMove();
        if (savedGame.getActiveSkillIndex() != -1)
        {
            this.activeSkill = getGame().getPlayingPlayer().getSkills().get(savedGame.getActiveSkillIndex());
        }
    }

    public void switchElements(Coords first, Coords second)
    {
        Logger.logDebug(LogKey, "Switching elements");
        controls.setEnabledControls(false);
        this.lastMove = new Move(first, second);
        saveGame(GameState.Switched);
        startSwitchAnimation(first, second);
    }

    public void executeSkillFromUser(Skill s)
    {
        this.activeSkill = s;

        saveGame(GameState.Wait);
        stateTransition(GameState.Wait);
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

    public Skill getActiveSkill()
    {
        return activeSkill;
    }

    public void stateTransition(GameState state)
    {
        Logger.logInfo(LogKey, "State Transition to " + state.toString());
        oldState = this.state;
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
                    switchback = true;
                    switchback();
                }
                else
                {
                    switchback = false;
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
                board.setEnabled(true && hasTurn());
                activeSkill.startWaiting(board, this, hasTurn());
                break;
            case Executed:
                getGame().setStrikePossible(false);
                controls.setPointLimit(0);
                ArrayList<PointElement> pointElements = BoardChecker.getAll(board, rules);
                int Executedpoints = Utilities.getPointsFrom(pointElements);
                game.addPointElements(pointElements, board, false);
                if (Executedpoints == 0)
                {
                    if (activeSkill.isSwitchSkill())
                    {
                        switchback = true;
                        switchback();
                    }
                    else
                    {
                        activeSkill.pay();
                        switchback = false;
                        stateTransition(GameState.Normal);
                    }
                }
                else
                {
                    switchback = false;
                    activeSkill.pay();
                    activeSkill.setWaiting(false);
                    if (activeSkill.isSwitchSkill())
                    {
                        activeSkill.pay();
                    }
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

    protected boolean hasTurn()
    {
        // if human player has turn
        return getGame().getPlayingPlayer().getStrategy() == null;
    }

    protected void transitionToNormal()
    {
        getGame().setStrikePossible(true);
        controls.setPointLimit(rules.getPointLimit());

        if (game.willGameBeOver())
        {
            controls.highlightPoints();
            Logger.logInfo(LogKey, "Game will be over (to normal)");
        } else
        {
            controls.clearHighlights();
        }
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
        board.shuffle(false, rules);
        saveGame(GameState.Normal);
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
        saveGame(GameState.Recreated);
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
        if (switchback)
        {
            // back to last state, was a switchback
            stateTransition(oldState);
        }
        else if (state == GameState.Wait)
        {
            stateTransition(GameState.Executed);
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
        if (game.willGameBeOver())
        {
            // don't allow , if game would be over
            controls.highlightPoints();
            Logger.logInfo(LogKey, "Game will be over (next)");
            return;
        }

        game.moveEnds();
        controls.setEnabledControls(true);
        controls.update();
        if (game.willGameBeOver())
        {
            // highlight for next player
            controls.highlightPoints();
        }
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
        saveGame(GameState.Executed);
        stateTransition(GameState.Executed);
    }


    public Board getBoard()
    {
        return board;
    }

    public SavedGame getSavedGame()
    {
        return savedGame;
    }

    private void saveGame(GameState nextState)
    {
        int i = getIndex(activeSkill);
        savedGame = new SavedGame(rules, game, board, nextState, lastMove, i);
    }

    protected int getIndex(Skill skill)
    {
        int i = -1;
        if (skill != null)
        {
            i = getGame().getPlayingPlayer().getSkills().indexOf(skill);
        }

        return i;
    }
}
