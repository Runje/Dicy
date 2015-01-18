package games.runje.dicy.controls;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.game.LocalGame;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 02.01.2015.
 */
public class Controls extends RelativeLayout
{
    protected final Activity activity;
    LocalGame game;
    List<TextView> playersView = new ArrayList<>();
    private List<TextView> strikesView = new ArrayList<>();

    //TODO: controls as member
    public Controls(Activity context)
    {
        super(context);
        this.activity = context;

    }

    protected View restart()
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

        b.setId(R.id.restart);
        return b;
    }

    public View playerPoints()
    {
        RelativeLayout l = new RelativeLayout(getContext());

        List<Player> players = game.getPlayers();
        int lastId = -1;
        for (int i = 0; i < players.size(); i++)
        {
            TextView pointsText = new TextView(getContext());
            pointsText.setText(players.get(i).getName() + ": 0");
            int id = View.generateViewId();
            pointsText.setId(id);

            if (lastId == -1)
            {
                // first
                l.addView(pointsText);
            }
            else
            {
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.addRule(RelativeLayout.BELOW, lastId);
                l.addView(pointsText, p);
            }


            playersView.add(pointsText);

            // strikes
            TextView strikeText = new TextView(getContext());
            strikeText.setText("Strikes: ");
            int strikeTextId = View.generateViewId();
            strikeText.setId(strikeTextId);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, id);
            l.addView(strikeText, p);

            TextView strikes = new TextView(getContext());
            strikes.setTextColor(Color.RED);
            RelativeLayout.LayoutParams pS = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            pS.addRule(RelativeLayout.BELOW, id);
            pS.addRule(RelativeLayout.RIGHT_OF, strikeTextId);
            l.addView(strikes, pS);

            strikesView.add(strikes);
            lastId = strikeTextId;
        }

        l.setId(R.id.playersPoints);
        return l;
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

    View gravityArrows()
    {
        int arrow = R.drawable.arrow2;
        int length = 100;
        RelativeLayout l = new RelativeLayout(getContext());

        ImageView arrowLeft = new ImageView(getContext());
        ImageView arrowRight = new ImageView(getContext());
        ImageView arrowUp = new ImageView(getContext());
        ImageView arrowDown = new ImageView(getContext());
        arrowLeft.setOnClickListener(new GravityListener(Gravity.Left));
        arrowRight.setOnClickListener(new GravityListener(Gravity.Right));
        arrowUp.setOnClickListener(new GravityListener(Gravity.Up));
        arrowDown.setOnClickListener(new GravityListener(Gravity.Down));
        arrowLeft.setImageResource(arrow);
        arrowLeft.setColorFilter(Color.BLUE);
        arrowLeft.setId(R.id.arrowLeft);
        arrowLeft.setRotation(180);

        RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(length, length);
        paramsLeft.leftMargin = 0;
        paramsLeft.topMargin = length;
        l.addView(arrowLeft, paramsLeft);

        arrowUp.setImageResource(arrow);
        arrowUp.setColorFilter(Color.BLUE);
        arrowUp.setId(R.id.arrowUp);
        arrowUp.setRotation(270);

        RelativeLayout.LayoutParams paramsUp = new RelativeLayout.LayoutParams(length, length);
        paramsUp.leftMargin = length;
        paramsUp.topMargin = 0;
        l.addView(arrowUp, paramsUp);

        arrowRight.setImageResource(arrow);
        arrowRight.setColorFilter(Color.BLUE);
        arrowRight.setId(R.id.arrowRight);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(length, length);
        params.leftMargin = 2 * length;
        params.topMargin = length;
        l.addView(arrowRight, params);

        arrowDown.setImageResource(arrow);
        arrowDown.setColorFilter(Color.BLUE);
        arrowDown.setRotation(90);
        arrowDown.setId(R.id.arrowDown);
        RelativeLayout.LayoutParams paramsDown = new RelativeLayout.LayoutParams(length, length);
        paramsDown.leftMargin = length;
        paramsDown.topMargin = 2 * length;
        l.addView(arrowDown, paramsDown);

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
        updateGravity();
        updatePoints();
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

        disableGravity();
    }

    protected void disableGravity()
    {
        ImageView leftArrow = (ImageView) findViewById(R.id.arrowLeft);
        leftArrow.setEnabled(false);
        ImageView rightArrow = (ImageView) findViewById(R.id.arrowRight);
        rightArrow.setEnabled(false);
        ImageView upArrow = (ImageView) findViewById(R.id.arrowUp);
        upArrow.setEnabled(false);
        ImageView downArrow = (ImageView) findViewById(R.id.arrowDown);
        downArrow.setEnabled(false);
    }

    public void enable()
    {
        if (game.isGameOver())
        {
            return;
        }

        EditText edit = (EditText) findViewById(R.id.straightEdit);
        edit.setEnabled(true);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        edit = (EditText) findViewById(R.id.xOfAKindEdit);
        edit.setEnabled(true);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);

        CheckBox cb = (CheckBox) findViewById(R.id.diagonalCB);
        cb.setEnabled(true);

        enableGravity();
    }

    protected void enableGravity()
    {
        ImageView leftArrow = (ImageView) findViewById(R.id.arrowLeft);
        leftArrow.setEnabled(true);
        ImageView rightArrow = (ImageView) findViewById(R.id.arrowRight);
        rightArrow.setEnabled(true);
        ImageView upArrow = (ImageView) findViewById(R.id.arrowUp);
        upArrow.setEnabled(true);
        ImageView downArrow = (ImageView) findViewById(R.id.arrowDown);
        downArrow.setEnabled(true);
    }

    public void updatePoints()
    {
        updatePlayers();
    }

    public void updatePlayers()
    {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < playersView.size(); i++)
        {
            playersView.get(i).setText(players.get(i).getName() + ": " + Integer.toString(players.get(i).getPoints()));
            if (i == game.getTurn())
            {
                playersView.get(i).setTextColor(Color.RED);
            }
            else
            {
                playersView.get(i).setTextColor(Color.GRAY);
            }

            strikesView.get(i).setText(strikesToString(players.get(i).getStrikes()));
        }
    }

    private String strikesToString(int strikes)
    {
        if (strikes == 1)
        {
            return "X";
        }

        if (strikes == 2)
        {
            return "X X";
        }

        if (strikes == 3)
        {
            return "X X X";
        }

        return "";
    }

    public void updateGravity()
    {
        ImageView leftArrow = (ImageView) findViewById(R.id.arrowLeft);
        leftArrow.setColorFilter(Color.BLUE);
        ImageView rightArrow = (ImageView) findViewById(R.id.arrowRight);
        rightArrow.setColorFilter(Color.BLUE);
        ImageView upArrow = (ImageView) findViewById(R.id.arrowUp);
        upArrow.setColorFilter(Color.BLUE);
        ImageView downArrow = (ImageView) findViewById(R.id.arrowDown);
        downArrow.setColorFilter(Color.BLUE);

        switch (Gamemaster.getInstance().getBoard().getGravity())
        {
            case Up:
                upArrow.setColorFilter(Color.RED);
                break;
            case Down:
                downArrow.setColorFilter(Color.RED);
                break;
            case Right:
                rightArrow.setColorFilter(Color.RED);
                break;
            case Left:
                leftArrow.setColorFilter(Color.RED);
                break;
        }
    }
}
