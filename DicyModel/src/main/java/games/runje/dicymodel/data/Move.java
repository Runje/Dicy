package games.runje.dicymodel.data;

/**
 * Created by Thomas on 07.01.2015.
 */
public class Move
{
    private int points = 0;
    private Coords first;
    private Coords second;

    public Move(Coords first, Coords second)
    {
        this.points = 0;
        this.first = first;
        this.second = second;
    }

    public Move(int points, Coords first, Coords second)
    {
        this.points = points;
        this.first = first;
        this.second = second;

    }

    public Coords getFirst()
    {
        return first;
    }

    public Coords getSecond()
    {
        return second;
    }

    public int getPoints()
    {

        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    @Override
    public String toString()
    {
        return "Move{" +
                "points=" + points +
                ", first=" + first +
                ", second=" + second +
                '}';
    }
}
