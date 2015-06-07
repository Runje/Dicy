package games.runje.dicy.layouts;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicymodel.Utilities;

/**
 * Created by Thomas on 02.02.2015.
 */
public abstract class DicesLayout extends RelativeLayout
{
    public final int MaxLength;
    private TextView textView;
    private int length;
    private ImageView[] dices;

    public DicesLayout(Context context, int length, int size, int maxLength)
    {
        super(context);
        MaxLength = maxLength;
        this.length = length;
        textView = new TextView(context);
        textView.setId(Utilities.generateViewId());
        addView(textView);

        dices = new ImageView[MaxLength];

        int lastId = 0;
        for (int i = 0; i < MaxLength; i++)
        {
            ImageView iv = new ImageView(context);
            iv.setId(Utilities.generateViewId());
            dices[i] = iv;
            LayoutParams p = new LayoutParams(size, size);
            if (lastId != -1)
            {
                p.addRule(RelativeLayout.RIGHT_OF, lastId);
            }

            addView(iv, p);
            lastId = iv.getId();
        }

        update();
    }

    protected void init()
    {
        for (int i = 0; i < MaxLength; i++)
        {
            dices[i].setImageResource(AnimatedBoardElement.valueToImageResource(getValue(i)));
        }

        textView.setText(getText());
    }
    protected abstract int getValue(int i);

    protected abstract String getText();


    public void increaseLength()
    {
        if (length == MaxLength)
        {
            length = 2;
        }
        else
        {
            length++;
        }

        update();
    }

    private void update()
    {
        if (length < 3)
        {
            textView.setVisibility(View.VISIBLE);

            for (int i = 0; i < MaxLength; i++)
            {
                dices[i].setVisibility(View.GONE);
            }
        }
        else
        {
            textView.setVisibility(View.GONE);

            for (int i = 0; i < length; i++)
            {
                dices[i].setVisibility(View.VISIBLE);
            }

            for (int i = length; i < MaxLength; i++)
            {
                dices[i].setVisibility(View.GONE);
            }
        }
    }

    public int getLength()
    {
        if (length < 3)
        {
            // Not possible
            return MaxLength + 1;
        }

        return length;
    }

    public void setLength(int anInt)
    {
        if (length == MaxLength + 1)
        {
            length = 0;
        }
        else
        {
            length = anInt;
        }

        update();
    }
}
