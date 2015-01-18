package games.runje.dicymodel.data;

/**
 * Created by Thomas on 28.10.2014.
 */
public class NoElement extends BoardElement
{
    public NoElement(Coords c)
    {
        super(c);
    }

    @Override
    public int getValue()
    {
        return -1;
    }

    @Override
    public BoardElement copy()
    {
        return new NoElement(position);
    }
}
