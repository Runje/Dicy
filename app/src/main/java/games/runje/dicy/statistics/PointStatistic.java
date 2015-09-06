package games.runje.dicy.statistics;

import java.util.Date;

import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 19.08.2015.
 */
public class PointStatistic
{
    int points;
    Date date;
    String name;
    RuleVariant ruleVariant;

    public PointStatistic(String name, Date date, int points, RuleVariant ruleVariant)
    {
        this.points = points;
        this.date = date;
        this.name = name;
        this.ruleVariant = ruleVariant;
    }

    public RuleVariant getRuleVariant()
    {
        return ruleVariant;
    }

    public void setRuleVariant(RuleVariant ruleVariant)
    {
        this.ruleVariant = ruleVariant;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "PointStatistic{" +
                "points=" + points +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }
}
