package games.runje.dicymodel;

import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 13.04.2015.
 */
public class DummyControls implements GameControls
{
    @Override
    public void setEnabledControls(boolean enabled)
    {

    }

    @Override
    public boolean areControlsEnabled()
    {
        return false;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void setGamemaster(AbstractGamemaster gamemaster)
    {

    }

    @Override
    public void setAnimatedBoard(Board board)
    {

    }

    @Override
    public void startGame()
    {

    }
}
