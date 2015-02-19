package games.runje.dicymodel.data;

/**
 * Created by Thomas on 05.10.2014.
 */
public abstract class BoardElement
{
    /**
     * Value of the dice.
     */
    int value;

    Coords position;

    public BoardElement()
    {

    }

    public BoardElement(int v, Coords p)
    {
        this.value = v;
        this.position = p;
    }

    public BoardElement(int v)
    {
        this.value = v;
    }

    public BoardElement(Coords c)
    {
        this.position = c;
    }

    public static BoardElement createFromValue(int value, int row, int column)
    {
        return createFromValue(value, new Coords(row, column));
    }

    public static BoardElement createFromValue(int value, Coords pos)
    {
        switch (value)
        {
            case 0:
                return new NoElement(pos);
            default:
                return new Dice(value, pos);
        }

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

        BoardElement element = (BoardElement) o;

        if (value != element.value)
        {
            return false;
        }
        if (!position.equals(element.position))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = value;
        result = 31 * result + position.hashCode();
        return result;
    }

    public int getValue()
    {
        return this.value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public Coords getPosition()
    {
        return this.position;
    }

    public void setPosition(Coords position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return "BoardElement{" +
                "value=" + value +
                ", position=" + position +
                '}';
    }

    public abstract BoardElement copy();
}
