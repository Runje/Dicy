package games.runje.dicymodel.data;

import games.runje.dicymodel.ai.Strategy;

/**
 * Created by Thomas on 17.01.2015.
 */
public class Player
{
    private int points;
    private int strikes;
    private String name;
    private Strategy strategy;
    private boolean lastMoveWasStrike = false;

    public Player(String n, Strategy strategy)
    {
        this.strategy = strategy;
        name = n;
    }

    public int getStrikes()
    {
        return strikes;
    }

    public void setStrikes(int strikes)
    {
        this.strikes = strikes;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public void addStrike()
    {
        this.strikes++;
        if (strikes == 3)
        {
            points = 0;
            strikes = 0;
        }


    }

    public void addPoints(int p)
    {
        points += p;
    }

    public String getName()
    {
        return name;
    }

    public Strategy getStrategy()
    {
        return strategy;
    }

    public boolean isAi()
    {
        return strategy != null;
    }

    public boolean lastMoveWasStrike()
    {
        return lastMoveWasStrike;
    }

    public void setLastMoveWasStrike(boolean lastMoveWasStrike)
    {
        this.lastMoveWasStrike = lastMoveWasStrike;
    }
}
