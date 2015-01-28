package games.runje.dicy.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 17.01.2015.
 */
public class LocalGame extends Game
{
    private final int startingPlayer;
    int gameEndPoints;
    private List<Player> players;

    /**
     * Which players turn it is.
     */
    private int turn;
    private int movePoints;
    private int switchPoints;
    private String LogKey = "LocalGame";
    private int pointsLimit;
    private String winner;

    public LocalGame(int p, int pLimit, int gameLimit)
    {
        pointsLimit = pLimit;
        gameEndPoints = gameLimit;
        players = new ArrayList<>();
        for (int i = 0; i < p; i++)
        {
            players.add(new Player("Player " + (i + 1)));
        }

        // random starting player
        startingPlayer = new Random().nextInt(p);
        turn = startingPlayer;
    }

    public LocalGame(int pointLimit, int gameLimit, List<String> playerNames)
    {
        pointsLimit = pointLimit;
        gameEndPoints = gameLimit;
        players = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++)
        {
            players.add(new Player(playerNames.get(i)));
            Logger.logInfo("LocalGame", playerNames.get(i));
        }

        // random starting player
        startingPlayer = new Random().nextInt(playerNames.size());
        turn = startingPlayer;
    }

    public int getGameEndPoints()
    {
        return gameEndPoints;
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
            movePoints = 0;
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
    public boolean moveEnds()
    {
        players.get(turn).addPoints(movePoints);
        if (movePoints > 0)
        {
            players.get(turn).setStrikes(0);
        }

        movePoints = 0;
        nextPlayer();

        return isGameOver();
    }

    public boolean isGameOver()
    {
        boolean lastPlayerTurn = (turn == startingPlayer);
        boolean enoughPoints = false;
        int maxPoints = 0;
        String w = "Unknown";
        // TODO: What if two player with same points?
        for (Player p : players)
        {
            if (p.getPoints() >= maxPoints)
            {
                maxPoints = p.getPoints();
                w = p.getName();
            }
        }

        if (maxPoints >= gameEndPoints)
        {
            enoughPoints = true;
            winner = w;
        }

        Logger.logInfo(LogKey, "Last Player: " + lastPlayerTurn + ", MaxPoints; " + maxPoints);
        return lastPlayerTurn && enoughPoints;
    }

    public int getPointsLimit()
    {
        return pointsLimit;
    }

    public String getWinner()
    {
        return winner;
    }
}
