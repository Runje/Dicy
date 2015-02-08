package games.runje.dicy.game;

import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 17.01.2015.
 */
public abstract class Game
{
    /**
     * @return if the game is over.
     */
    abstract public boolean moveEnds();


    public abstract void addSwitchPoints(int points);

    public abstract void endSwitch();

    public abstract boolean hasTurn(Player player);

    public abstract boolean hasAIPlayerTurn();

    public abstract Player getPlayingPlayer();

    public abstract boolean isFinishedOrCancelled();

    public abstract void cancel();
}
