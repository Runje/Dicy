package games.runje.dicymodel.skills;

import java.util.ArrayList;

import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;

/**
 * Created by Thomas on 20.06.2015.
 */
public class ShuffleSkill extends Skill
{
    private ArrayList<ArrayList<BoardElement>> newBoard;
    private String LogKey = "ShuffleSkill";

    public ShuffleSkill(Skill skill)
    {
        super(skill);
    }

    public ShuffleSkill(int value, int max)
    {
        super(value, max, Skill.Shuffle, 0);

    }

    public void shuffle(Board board)
    {
        // TODO: shuffle only on host and send result to others
        if (newBoard == null)
        {
            Board b = new Board(board.getNumberOfRows(), board.getNumberOfColumns());
            newBoard = b.getBoard();
        }
    }
    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        Logger.logInfo(LogKey, "Before shuffle: " + board);
        board.updateBoard(newBoard);
        Logger.logInfo(LogKey, "After shuffle: " + board);
        super.startExecute(board, gm);
    }

    public void setNewBoard(ArrayList<ArrayList<BoardElement>> newBoard)
    {
        this.newBoard = newBoard;
    }

    public ArrayList<ArrayList<BoardElement>> getNewBoard()
    {
        return newBoard;
    }
}
