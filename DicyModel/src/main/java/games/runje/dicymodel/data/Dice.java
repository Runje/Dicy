package games.runje.dicymodel.data;

/**
 * Created by Thomas on 05.10.2014.
 */
public class Dice implements BoardElement
{
    /**
     * Value of the dice.
     */
    private int value;

    public Dice(int value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return Integer.toString(value);
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
