package games.runje.dicymodel.data;

/**
 * Created by Thomas on 17.01.2015.
 */
public class Player
{
    private int points;
    private int strikes;

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
}
