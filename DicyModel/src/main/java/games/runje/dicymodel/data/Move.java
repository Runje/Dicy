package games.runje.dicymodel.data;

import java.util.ArrayList;

import games.runje.dicymodel.Utilities;

/**
 * Created by Thomas on 07.01.2015.
 */
public class Move
{
    private Gravity gravity;
    private int points = 0;
    private Coords first;
    private Coords second;
    private ArrayList<PointElement> pointElements;
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

    public Move(Coords first, Coords second, Gravity gravity, ArrayList<PointElement> elements)
    {
        this.pointElements = elements;
        this.points = Utilities.getPointsFrom(elements);
        this.first = first;
        this.second = second;
        this.gravity = gravity;
    }

    public Gravity getGravity()
    {
        return gravity;
    }

    public void setGravity(Gravity gravity)
    {
        this.gravity = gravity;
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
                "second=" + second +
                ", first=" + first +
                ", points=" + points +
                ", gravity=" + gravity +
                '}';
    }

    public ArrayList<PointElement> getPointElements()
    {
        return pointElements;
    }

    public void setPointElements(ArrayList<PointElement> pointElements)
    {
        this.pointElements = pointElements;
    }
}
