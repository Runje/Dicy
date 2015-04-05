package games.runje.dicymodel;

import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 16.03.2015.
 */
public interface GameControls
{
    public void setEnabledControls(boolean enabled);

    public boolean areControlsEnabled();

    public void update();

    void setGamemaster(AbstractGamemaster gamemaster);

    void setAnimatedBoard(Board board);

    void startGame();
}
