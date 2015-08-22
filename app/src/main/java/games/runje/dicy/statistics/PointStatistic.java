package games.runje.dicy.statistics;

import java.util.Date;

/**
 * Created by Thomas on 19.08.2015.
 */
public class PointStatistic
{
    int points;
    Date date;
    String name;

    public PointStatistic(String name, Date date, int points)
    {
        this.points = points;
        this.date = date;
        this.name = name;
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
