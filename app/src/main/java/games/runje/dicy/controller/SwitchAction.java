package games.runje.dicy.controller;

import android.util.Log;

import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 19.10.2014.
 */
public class SwitchAction extends Action
{
    private Direction direction;

    private Coords position;

    public SwitchAction(Coords p, Direction d)
    {
        this.position = p;
        this.direction = d;
    }

    @Override
    public void execute()
    {
        Coords second = new Coords(0, 0);
        switch (direction)
        {
            case Up:
                second = new Coords(position.row - 1, position.column);
                break;
            case Down:
                second = new Coords(position.row + 1, position.column);
                break;
            case Left:
                second = new Coords(position.row, position.column - 1);
                break;
            case Right:
                second = new Coords(position.row, position.column + 1);
                break;
        }

        this.board.switchElements(position, second);
        Log.d("TL", "Exectued switch elements: " + position + " with " + second);
    }

    @Override
    public String toString()
    {
        return "SwitchAction{" +
                "direction=" + direction +
                ", position=" + position +
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

        switch (direction)
        {
            case Up:
                return position.row != 0;
            case Down:
                return position.row != board.getNumberOfRows() - 1;
            case Left:
                return position.column != 0;
            case Right:
                return position.column != board.getNumberOfColumns() - 1;
        }

        assert false : "Unknown Direction";
        return false;
    }
}
