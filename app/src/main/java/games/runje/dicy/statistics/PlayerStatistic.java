package games.runje.dicy.statistics;

import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 15.06.2015.
 */
public class PlayerStatistic
{
    private String strategy;
    private long[] games;
    private long[] wins;
    private String name;
    private long id;

    // TODO: wins / games for each rule variant
    public PlayerStatistic(String name)
    {
        this.name = name;
    }

    public PlayerStatistic(long id, String name, long[] games, long[] wins, String strategy)
    {
        this.name = name;
        this.id = id;
        this.strategy = strategy;
        this.games = games;
        this.wins = wins;
    }

    public String getStrategy()
    {
        return strategy;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getLooses()
    {
        long l = 0;
        for (int i = 0; i < games.length; i++)
        {
            l += games[i] - wins[i];
        }
        return l;
    }

    public long getLooses(RuleVariant ruleVariant)
    {
        return getGames(ruleVariant) - getWins(ruleVariant);
    }

    public double getPercentageWin()
    {
        return (double) getWins() / getGames() * 100;
    }

    public double getPercentageWin(RuleVariant ruleVariant)
    {
        return (double) getWins(ruleVariant) / getGames(ruleVariant) * 100;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getGames()
    {
        long g = 0;
        for (int i = 0; i < games.length; i++)
        {
            g += games[i];
        }
        return g;
    }

    public long getGames(RuleVariant ruleVariant)
    {
        return games[ruleVariant.ordinal()];
    }

    public long getWins()
    {
        long w = 0;
        for (int i = 0; i < games.length; i++)
        {
            w += wins[i];
        }
        return w;
    }

    public long getWins(RuleVariant ruleVariant)
    {
        return wins[ruleVariant.ordinal()];
    }

    @Override
    public String toString()
    {
        return "PlayerStatistic{" +
                "strategy='" + strategy + '\'' +
                ", games=" + games +
                ", wins=" + wins +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public void increaseGames(RuleVariant ruleVariant)
    {
        games[ruleVariant.ordinal()]++;
    }

    public void increaseWins(RuleVariant ruleVariant)
    {
        wins[ruleVariant.ordinal()]++;
    }


}
