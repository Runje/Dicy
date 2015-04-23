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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicy.layouts.PlayerLayout;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 02.01.2015.
 */
public class Controls extends RelativeLayout implements GameControls
{
    protected final Activity activity;
    protected GamemasterAnimated gmAnimated;
    protected AnimatedBoard board;
    protected List<PlayerLayout> playerLayouts = new ArrayList<>();
    protected boolean enabled;
    LocalGame game;
    List<TextView> playersView = new ArrayList<>();
    private List<TextView> strikesView = new ArrayList<>();
    private String LogKey = "Controls";

    //TODO: controls as member
    public Controls(Activity context, AnimatedBoard b, GamemasterAnimated gmAnimated)
    {
        super(context);
        this.gmAnimated = gmAnimated;
        this.activity = context;
        this.board = b;

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
                //gamemaster.restart();
            }
        });

        b.setId(R.id.restart);
        return b;
    }

    public List<PlayerLayout> getPlayerLayouts()
    {
        return playerLayouts;
    }

    public View playerPoints()
    {
        /*RelativeLayout l = new RelativeLayout(getContext());

        List<Player> players = game.getPlayers();
        int lastId = -1;
        for (int i = 0; i < players.size(); i++)
        {
            TextView pointsText = new TextView(getContext());
            pointsText.setText(players.get(i).getName() + ": 0");
            Logger.logInfo(LogKey, players.get(i).getName());
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
        return l;*/

        RelativeLayout l = new RelativeLayout(getContext());
        List<Player> players = game.getPlayers();
        PlayerLayout pl = new PlayerLayout(getContext(), players.get(0), R.drawable.blueyellowchip, gmAnimated, R.id.player1);
        this.playerLayouts.add(pl);

        PlayerLayout pl2 = new PlayerLayout(getContext(), players.get(1), R.drawable.bluewhitechip, gmAnimated, R.id.player2);
        this.playerLayouts.add(pl2);


        return null;
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
                game.getRules().setDiagonalActive(b);
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
                int value = game.getRules().getMinStraight();
                try
                {
                    int v = Integer.parseInt(edit.getText().toString());
                    if (v > 2)
                    {
                        value = v;
                    }
                }
                catch (NumberFormatException nfe)
                {
                    // do nothing
                }

                game.getRules().setMinStraight(value);
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
                int value = game.getRules().getMinXOfAKind();
                try
                {
                    int v = Integer.parseInt(edit.getText().toString());
                    if (v > 2)
                    {
                        value = v;
                    }
                }
                catch (NumberFormatException nfe)
                {
                    // do nothing
                }

                game.getRules().setMinXOfAKind(value);
            }
        });
        l.setId(R.id.xOfAKind);
        return l;
    }

    @Override
    public void setEnabledControls(boolean enabledControls)
    {
        AnimatedLogger.logInfo(LogKey, "Set EnabledControls: " + enabledControls);
        if (enabledControls)
        {
            enabled = true;
            enable();
        }
        else
        {
            enabled = false;
            disable();
        }
    }

    @Override
    public boolean areControlsEnabled()
    {
        return enabled;
    }

    public void update()
    {
        updateDiagonal();
        updateStraight();
        updateXOfAKind();
        updateGravity();
        updatePoints();
        enable();
    }

    @Override
    public void setGamemaster(AbstractGamemaster gamemaster)
    {
        // TODO:
        this.gmAnimated = (GamemasterAnimated) gamemaster;
    }

    @Override
    public void setAnimatedBoard(Board board)
    {
        this.board = (AnimatedBoard) board;
    }

    @Override
    public void startGame()
    {
        gmAnimated.startGame();
    }

    private void updateXOfAKind()
    {
        EditText edit = (EditText) findViewById(R.id.xOfAKindEdit);
        edit.setText(Integer.toString(game.getRules().getMinXOfAKind()));
    }

    private void updateDiagonal()
    {
        CheckBox cb = (CheckBox) findViewById(R.id.diagonalCB);
        cb.setChecked(game.getRules().isDiagonalActive());
    }

    private void updateStraight()
    {
        EditText edit = (EditText) findViewById(R.id.straightEdit);
        edit.setText(Integer.toString(game.getRules().getMinStraight()));
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
        BoardLayout l = board.getBoardLayout();
        l.getAboveBorder().setEnabled(false);
        l.getBelowBorder().setEnabled(false);
        l.getLeftBorder().setEnabled(false);
        l.getRightBorder().setEnabled(false);
    }

    public void enable()
    {
        if (game.isGameOver())
        {
            AnimatedLogger.logInfo(LogKey, "Game Over");
            return;
        }

        if (game.hasAIPlayerTurn())
        {
            disable();
            AnimatedLogger.logInfo(LogKey, "Disabling controls for " + game.getPlayingPlayer().getName() + ":" + game.getPlayingPlayer().getId());
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
        BoardLayout l = board.getBoardLayout();
        l.getAboveBorder().setEnabled(true);
        l.getBelowBorder().setEnabled(true);
        l.getLeftBorder().setEnabled(true);
        l.getRightBorder().setEnabled(true);


    }

    public void updatePoints()
    {
        updatePlayers();
    }

    public void updatePlayers()
    {
        List<Player> players = game.getPlayers();
        for (int i = 0; i < players.size(); i++)
        {
            /*playersView.get(i).setText(players.get(i).getName() + ": " + Integer.toString(players.get(i).getPoints()));
            if (i == game.getTurn())
            {
                playersView.get(i).setTextColor(Color.RED);
            }
            else
            {
                playersView.get(i).setTextColor(Color.GRAY);
            }

            strikesView.get(i).setText(strikesToString(players.get(i).getStrikes()));*/
            playerLayouts.get(i).updatePlayer(players.get(i), i == game.getTurn(), game);

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
        BoardLayout l = board.getBoardLayout();
        int brown = Color.parseColor("#795548");
        l.getAboveBorder().setBackgroundColor(brown);
        l.getBelowBorder().setBackgroundColor(brown);
        l.getLeftBorder().setBackgroundColor(brown);
        l.getRightBorder().setBackgroundColor(brown);
        int yellow = Color.YELLOW;
        switch (board.getGravity())
        {
            case Up:
                l.getAboveBorder().setBackgroundColor(yellow);
                break;
            case Down:
                l.getBelowBorder().setBackgroundColor(yellow);
                break;
            case Right:
                l.getRightBorder().setBackgroundColor(yellow);
                break;
            case Left:
                l.getLeftBorder().setBackgroundColor(yellow);
                break;
        }
    }
}
