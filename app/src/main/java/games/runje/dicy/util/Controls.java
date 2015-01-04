package games.runje.dicy.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import games.runje.dicy.R;
import games.runje.dicy.controller.Gamemaster;

/**
 * Created by Thomas on 02.01.2015.
 */
public class Controls extends RelativeLayout
{
    //TODO: controls as member
    public Controls(Context context)
    {
        super(context);
        addView(diagonalCheck());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.diagonal);
        addView(minStraight(), params);

        RelativeLayout.LayoutParams pX = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pX.addRule(RelativeLayout.RIGHT_OF, R.id.straight);
        pX.addRule(RelativeLayout.ALIGN_BASELINE, R.id.straight);
        addView(minXOfAKind(), pX);

        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pp.addRule(RelativeLayout.BELOW, R.id.straight);
        addView(points(), pp);

        RelativeLayout.LayoutParams pR = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pR.addRule(RelativeLayout.RIGHT_OF, R.id.straight);
        pR.addRule(RelativeLayout.ALIGN_BASELINE, R.id.pointsText);
        addView(restart(), pR);
    }

    private View restart()
    {
        Button b = new Button(getContext());
        b.setText("Restart");
        b.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Gamemaster.getInstance().restart();
            }
        });
        return b;
    }

    private View points()
    {
        TextView pointsText = new TextView(getContext());
        pointsText.setText("Points: 0");
        pointsText.setId(R.id.pointsText);
        return pointsText;
    }

    View diagonalCheck()
    {
        RelativeLayout l = new RelativeLayout(getContext());
        TextView diagonalText = new TextView(getContext());
        diagonalText.setText("Diagonal");
        diagonalText.setId(R.id.diagonalText);
        l.addView(diagonalText);

        CheckBox cb = new CheckBox(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.diagonalText);
        params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.diagonalText);
        l.addView(cb, params);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                Gamemaster.getInstance().getRules().setDiagonalActive(b);
            }
        });
        cb.setId(R.id.diagonalCB);
        l.setId(R.id.diagonal);
        return l;
    }

    View minStraight()
    {
        RelativeLayout l = new RelativeLayout(getContext());
        TextView t = new TextView(getContext());
        t.setText("Minimum Straight");
        t.setId(R.id.straightText);
        l.addView(t);

        EditText e = new EditText(getContext());
        e.setId(R.id.straightEdit);
        e.setInputType(InputType.TYPE_CLASS_NUMBER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.straightText);
        params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.straightText);
        l.addView(e, params);

        e.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                EditText edit = (EditText) findViewById(R.id.straightEdit);
                int value = Gamemaster.getInstance().getRules().getMinStraight();
                try
                {
                    int v = Integer.parseInt(edit.getText().toString());
                    if (v > 2)
                    {
                        value = v;
                    }
                } catch (NumberFormatException nfe)
                {
                    // do nothing
                }

                Gamemaster.getInstance().getRules().setMinStraight(value);
            }
        });
        l.setId(R.id.straight);
        return l;
    }

    View minXOfAKind()
    {
        RelativeLayout l = new RelativeLayout(getContext());
        TextView t = new TextView(getContext());
        t.setText("Minimum X of a kind:");
        t.setId(R.id.xOfAKindText);
        l.addView(t);

        EditText e = new EditText(getContext());
        e.setId(R.id.xOfAKindEdit);
        e.setInputType(InputType.TYPE_CLASS_NUMBER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.xOfAKindText);
        params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.xOfAKindText);
        l.addView(e, params);

        e.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                EditText edit = (EditText) findViewById(R.id.xOfAKindEdit);
                int value = Gamemaster.getInstance().getRules().getMinXOfAKind();
                try
                {
                    int v = Integer.parseInt(edit.getText().toString());
                    if (v > 2)
                    {
                        value = v;
                    }
                } catch (NumberFormatException nfe)
                {
                    // do nothing
                }

                Gamemaster.getInstance().getRules().setMinXOfAKind(value);
            }
        });
        l.setId(R.id.xOfAKind);
        return l;
    }

    public void update()
    {
        updateDiagonal();
        updateStraight();
        updateXOfAKind();
    }

    private void updateXOfAKind()
    {
        EditText edit = (EditText) findViewById(R.id.xOfAKindEdit);
        edit.setText(Integer.toString(Gamemaster.getInstance().getRules().getMinXOfAKind()));
    }

    private void updateDiagonal()
    {
        CheckBox cb = (CheckBox) findViewById(R.id.diagonalCB);
        cb.setChecked(Gamemaster.getInstance().getRules().isDiagonalActive());
    }

    private void updateStraight()
    {
        EditText edit = (EditText) findViewById(R.id.straightEdit);
        edit.setText(Integer.toString(Gamemaster.getInstance().getRules().getMinStraight()));
    }

    public void disable()
    {
        EditText edit = (EditText) findViewById(R.id.straightEdit);
        edit.setEnabled(false);
        edit.setInputType(InputType.TYPE_NULL);

        edit = (EditText) findViewById(R.id.xOfAKindEdit);
        edit.setEnabled(false);
        edit.setInputType(InputType.TYPE_NULL);

        CheckBox cb = (CheckBox) findViewById(R.id.diagonalCB);
        cb.setEnabled(false);
    }

    public void enable()
    {
        EditText edit = (EditText) findViewById(R.id.straightEdit);
        edit.setEnabled(true);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        edit = (EditText) findViewById(R.id.xOfAKindEdit);
        edit.setEnabled(true);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        CheckBox cb = (CheckBox) findViewById(R.id.diagonalCB);
        cb.setEnabled(true);
    }

    public void updatePoints()
    {
        TextView t = (TextView) findViewById(R.id.pointsText);
        t.setText("Points: " + Integer.toString(Gamemaster.getInstance().points));
    }
}
