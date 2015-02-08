package games.runje.dicy.layouts;

import android.content.Context;

/**
 * Created by Thomas on 02.02.2015.
 */
public class StraightLayout extends DicesLayout
{

    public StraightLayout(Context context, int length, int size)
    {
        super(context, length, size, 6);
        init();
    }

    @Override
    protected int getValue(int i)
    {
        return i + 1;
    }

    @Override
    protected String getText()
    {
        return "No Straight";
    }

    @Override
    public void increaseLength()
    {

    }

    @Override
    public int getLength()
    {
        return 3;
    }
}
