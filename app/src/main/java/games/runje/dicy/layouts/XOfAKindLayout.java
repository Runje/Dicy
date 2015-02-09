package games.runje.dicy.layouts;

import android.content.Context;

import java.util.Random;

/**
 * Created by Thomas on 02.02.2015.
 */
public class XOfAKindLayout extends DicesLayout
{
    int value = new Random().nextInt(6) + 1;

    public XOfAKindLayout(Context context, int length, int size, int maxLength)
    {
        super(context, length, size, maxLength);
        init();
    }

    @Override
    protected int getValue(int i)
    {
        return value;
    }

    @Override
    protected String getText()
    {
        return "No X Of A Kind";
    }
}

