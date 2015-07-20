package games.runje.dicymodel;

import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.game.GameState;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 13.07.2015.
 */
public class SavedGame
{
    private int activeSkillIndex;
    private Move lastMove;
    private Rules rules;
    private LocalGame game;
    private Board board;
    private GameState nextGameState;

    public SavedGame(Rules rules, LocalGame game, Board board, GameState nextGameState, Move lastMove, int activeSkillIndex)
    {
        this.rules = rules;
        this.game = game;
        this.board = board;
        this.nextGameState = nextGameState;
        this.lastMove = lastMove;
        this.activeSkillIndex = activeSkillIndex;
    }

    public Move getLastMove()
    {
        return lastMove;
    }

    public void setLastMove(Move lastMove)
    {
        this.lastMove = lastMove;
    }

    public Rules getRules()
    {
        return rules;
    }

    public void setRules(Rules rules)
    {
        this.rules = rules;
    }

    public LocalGame getGame()
    {
        return game;
    }

    public void setGame(LocalGame game)
    {
        this.game = game;
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public GameState getNextGameState()
    {
        return nextGameState;
    }

    public void setNextGameState(GameState nextGameState)
    {
        this.nextGameState = nextGameState;
    }

    public int getActiveSkillIndex()
    {
        return activeSkillIndex;
    }

    public void setActiveSkillIndex(int activeSkillIndex)
    {
        this.activeSkillIndex = activeSkillIndex;
    }
}
