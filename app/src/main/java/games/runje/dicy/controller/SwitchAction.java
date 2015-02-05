package games.runje.dicy.controller;

import android.util.Log;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 19.10.2014.
 */
public class SwitchAction extends Action
{
    private final boolean switchBackPossible;
    private Coords first;
    private Coords second;

    public SwitchAction(Coords p, Direction direction, boolean switchBackPossible)
    {
        this.first = p;
        this.switchBackPossible = switchBackPossible;

        switch (direction)
        {
            case Up:
                second = new Coords(first.row - 1, first.column);
                break;
            case Down:
                second = new Coords(first.row + 1, first.column);
                break;
            case Left:
                second = new Coords(first.row, first.column - 1);
                break;
            case Right:
                second = new Coords(first.row, first.column + 1);
                break;
        }
    }

    public SwitchAction(Coords p1, Coords p2, boolean switchBackPossible)
    {
        this.first = p1;
        this.second = p2;
        this.switchBackPossible = switchBackPossible;
    }

    @Override
    public void execute()
    {
        ((AnimatedBoard) this.board).switchElements(first, second, switchBackPossible);
        Log.d("TL", "Exectued switch elements: " + first + " with " + second);
    }

    @Override
    public String toString()
    {
        return "SwitchAction{" +
                ", first=" + first +
                ", second=" + second +
                '}';
    }

    @Override
    public boolean isPossible()
    {
        if (Gamemaster.getInstance().isAnimationIsRunning())
        {
            return false;
        }

        Board board = Gamemaster.getInstance().getBoard();

        return !(second.row < 0 || second.row >= board.getNumberOfRows() ||
                second.column < 0 || second.column >= board.getNumberOfColumns());

    }
}
