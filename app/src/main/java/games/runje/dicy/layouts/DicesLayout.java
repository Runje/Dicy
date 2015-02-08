package games.runje.dicy.layouts;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by thomas on 07.02.15.
 */
public abstract class DicesLayout extends RelativeLayout
{
    public int MaxLength = 7;

    public DicesLayout(Context context, int length, int size, int max)
    {
        super(context);
    }

    protected void init()
    {

    }

    protected abstract int getValue(int i);

    protected abstract String getText();

    public abstract void increaseLength();

    public abstract int getLength();
}
