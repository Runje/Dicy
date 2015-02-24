package games.runje.dicymodel.data;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.ai.OnlineStrategy;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.skills.Skill;

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
    private List<Skill> skills = new ArrayList<>();

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
        return strategy != null && !(strategy instanceof OnlineStrategy);
    }

    public boolean isHuman()
    {
        return strategy == null;
    }

    public boolean isOnlinePlayer()
    {
        return strategy instanceof OnlineStrategy;
    }

    public boolean lastMoveWasStrike()
    {
        return lastMoveWasStrike;
    }

    public void setLastMoveWasStrike(boolean lastMoveWasStrike)
    {
        this.lastMoveWasStrike = lastMoveWasStrike;
    }

    public void loadSkills(int[] count)
    {
        for (Skill s : skills)
        {
            s.load(count[s.getLoadValue()]);

        }
    }

    public Skill getSkill(String s)
    {
        // TODO with isInstanceOf???
        for (Skill sk : skills)
        {
            if (sk.getName().equals(s))
            {
                return sk;
            }
        }

        return null;
    }

    public void addSkill(Skill skill)
    {
        skills.add(skill);
    }
}
