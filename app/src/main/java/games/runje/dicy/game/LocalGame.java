package games.runje.dicy.game;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 17.01.2015.
 */
public class LocalGame extends Game
{
    private List<Player> players;
    /**
     * Which players turn it is.
     */
    private int turn;

    private int movePoints;

    private int switchPoints;
    private String LogKey = "LocalGame";
    private int pointsLimit;

    public LocalGame(int p, int pLimit)
    {
        pointsLimit = pLimit;

        players = new ArrayList<>();
        for (int i = 0; i < p; i++)
        {
            players.add(new Player());
        }
    }

    @Override
    public void addSwitchPoints(int points)
    {
        Logger.logInfo(LogKey, "Add switch points: " + points);
        switchPoints += points;
    }

    @Override
    public void endSwitch()
    {
        Logger.logInfo(LogKey, "End switch");
        if (switchPoints > pointsLimit)
        {
            movePoints += switchPoints;
        }
        else
        {
            players.get(turn).addStrike();
            moveEnds();
        }

        switchPoints = 0;
    }

    public int getMovePoints()
    {
        return movePoints;
    }

    public void setMovePoints(int movePoints)
    {
        this.movePoints = movePoints;
    }

    public int getSwitchPoints()
    {
        return switchPoints;
    }

    public void setSwitchPoints(int switchPoints)
    {
        this.switchPoints = switchPoints;
    }

    public int getTurn()
    {
        return turn;
    }

    public void setTurn(int turn)
    {
        this.turn = turn;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    private void nextPlayer()
    {
        turn = (turn + 1) % players.size();
    }

    @Override
    public void moveEnds()
    {
        players.get(turn).addPoints(movePoints);
        movePoints = 0;
        nextPlayer();
    }
}
