package games.runje.dicymodel.data;

import java.util.Random;

/**
 * Created by Thomas on 05.10.2014.
 */
public class Dice extends BoardElement
{

    public Dice()
    {
        super();
        this.roll();
    }

    public Dice(int v)
    {
        super(v);
    }

    public Dice(Coords coords)
    {
        super(coords);
        this.roll();
    }

    public Dice(int v, Coords c)
    {
        super(v, c);
    }

    private void roll()
    {
        Random random = new Random();
        this.value = random.nextInt(6) + 1;
    }

    @Override
    public String toString()
    {
        return "Pos = " + this.position + ", value = " + Integer.toString(value);
    }

    @Override
    public BoardElement copy()
    {
        return new Dice(value, position);
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
