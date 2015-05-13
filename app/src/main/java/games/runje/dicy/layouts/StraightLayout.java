package games.runje.dicy.layouts;

import android.content.Context;

/**
 * Created by Thomas on 02.02.2015.
 */
public class StraightLayout extends DicesLayout
{

    private int value;

    public StraightLayout(Context context, int length, int size, int value)
    {
        super(context, length, size, 5);
        this.value = value;
        init();
    }

    @Override
    protected int getValue(int i)
    {
        return i + value;
    }

    @Override
    protected String getText()
    {
        return "No Straight";
    }
}
