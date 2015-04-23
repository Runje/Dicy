package games.runje.dicymodel.data;


import java.util.Arrays;

/**
 * Created by Thomas on 01.10.2014.
 */
public class PointElement
{
    private int points;

    private PointType type;
    /**
     * Highest number of dice.
     */
    private int diceValue;
    /**
     * The strength of the points. (e.g. the x in x of a kind)
     */
    private int multiplicity;
    private Orientation orientation;
    private Coords[] coords;

    public PointElement(PointType type, int multiplicity, int value, Coords[] coords, Orientation orientation, int points)
    {
        this.type = type;
        this.diceValue = value;
        this.multiplicity = multiplicity;
        this.coords = coords;
        this.orientation = orientation;
        this.points = points;
    }

    public int getDiceValue()
    {
        return diceValue;
    }

    public PointType getType()
    {
        return type;
    }

    public int getMultiplicity()
    {
        return multiplicity;
    }

    public Coords[] getCoords()
    {
        return coords;
    }

    @Override
    public String toString()
    {
        return "PointElement{" +
                "points=" + points +
                ", type=" + type +
                ", diceValue=" + diceValue +
                ", multiplicity=" + multiplicity +
                ", orientation=" + orientation +
                ", coords=" + Arrays.toString(coords) +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        PointElement that = (PointElement) o;

        if (diceValue != that.diceValue)
        {
            return false;
        }
        if (multiplicity != that.multiplicity)
        {
            return false;
        }
        if (points != that.points)
        {
            return false;
        }
        if (!Arrays.equals(coords, that.coords))
        {
            return false;
        }
        if (orientation != that.orientation)
        {
            return false;
        }
        return type == that.type;

    }

    @Override
    public int hashCode()
    {
        int result = points;
        result = 31 * result + type.hashCode();
        result = 31 * result + diceValue;
        result = 31 * result + multiplicity;
        result = 31 * result + orientation.hashCode();
        result = 31 * result + Arrays.hashCode(coords);
        return result;
    }

    public int getPoints()
    {
        return points;
    }
}
