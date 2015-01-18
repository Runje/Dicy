package games.runje.dicy.game;

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

}
