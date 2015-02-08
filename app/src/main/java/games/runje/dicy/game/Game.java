package games.runje.dicy.game;

import java.util.ArrayList;

import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 17.01.2015.
 */
public abstract class Game
{
    /**
     * @return if the game is over.
     */
    abstract public boolean moveEnds();


    public abstract void addPointElements(ArrayList<PointElement> elements);

    public abstract void endSwitch();

    public abstract boolean hasTurn(Player player);

    public abstract boolean hasAIPlayerTurn();

    public abstract Player getPlayingPlayer();

    public abstract boolean isFinishedOrCancelled();

    public abstract void cancel();

    public abstract int getPointsLimit();
}
