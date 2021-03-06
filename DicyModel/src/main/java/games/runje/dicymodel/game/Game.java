package games.runje.dicymodel.game;

import java.util.ArrayList;

import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 17.01.2015.
 */
public abstract class Game
{
    private boolean strikePossible = true;

    /**
     * @return if the game is over.
     */
    abstract public boolean moveEnds(int allowedStrikes);


    public abstract void addPointElements(ArrayList<PointElement> elements, Board board, boolean load);

    public abstract void endSwitch(int allowedStrikes);

    public abstract boolean hasTurn(Player player);

    public abstract boolean hasAIPlayerTurn();

    public abstract Player getPlayingPlayer();

    public abstract boolean isFinishedOrCancelled();

    public abstract void cancel();

    public boolean isStrikePossible()
    {
        return strikePossible;
    }

    public void setStrikePossible(boolean strikePossible)
    {
        this.strikePossible = strikePossible;
    }

}
