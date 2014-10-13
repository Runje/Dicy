package games.runje.dicymodel.data;

/**
 * Created by Thomas on 01.10.2014.
 */
public class Coords
{
    public int row;

    public int column;

    public Coords(int x, int y)
    {
        this.row = x;
        this.column = y;
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

        Coords coords = (Coords) o;

        if (column != coords.column)
        {
            return false;
        }
        if (row != coords.row)
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return "Coords{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public int hashCode()
    {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
