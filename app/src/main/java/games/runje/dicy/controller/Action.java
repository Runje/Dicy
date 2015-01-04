package games.runje.dicy.controller;

import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 19.10.2014.
 */
public abstract class Action
{
    protected Board board;

    public abstract void execute();

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public abstract boolean isPossible();
}
