package games.runje.dicy.layouts;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import games.runje.dicy.animatedData.AnimatedBoardElement;

/**
 * Created by Thomas on 02.02.2015.
 */
public class StraightLayout extends RelativeLayout
{
    private final int MaxLength = 6;
    private TextView textView;
    private int length;
    private ImageView[] dices;

    public StraightLayout(Context context, int length, int size)
    {
        super(context);
        this.length = length;
        textView = new TextView(context);
        textView.setText("No Straight");
        textView.setId(View.generateViewId());
        addView(textView);

        dices = new ImageView[6];
        int start = 1;

        int lastId = 0;
        for (int i = start; i < MaxLength + start; i++)
        {
            ImageView iv = new ImageView(context);
            iv.setImageResource(AnimatedBoardElement.valueToImageResource(i));
            iv.setId(View.generateViewId());
            dices[i - start] = iv;
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


    public void increaseLength()
    {
        if (length == 6)
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
            // No straight possible
            return 7;
        }

        return length;
    }

}
