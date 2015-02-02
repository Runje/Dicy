package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.Logger;
import games.runje.dicy.layouts.StraightLayout;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicy.util.ViewUtilities;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class OptionActivity extends Activity
{

    public static final String PlayingIntent = "Playing";
    public static final String PlayerIntent = "Player";
    public static final String LengthIntent = "Length";
    public static final String DiagonalIntent = "Diagonal";
    public static final String StraightIntent = "Straight";
    final static int MaxPlayers = 4;
    private EditText[] editPlayers = new EditText[MaxPlayers];
    private CheckBox[] playingCb = new CheckBox[MaxPlayers];
    private Spinner lengthSpinner;
    private String LogKey = "Options";
    private CheckBox diagonal;
    private StraightLayout straight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RelativeLayout l = new RelativeLayout(this);
        View players = createPlayers();
        RelativeLayout.LayoutParams params = ViewUtilities.createRelativeLayoutParams();
        //params.topMargin = android.R.attr.actionBarSize;
        params.topMargin = ViewUtilities.getActionBarHeight(this);
        l.addView(players, params);

        View playButton = playButton();
        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        p.addRule(RelativeLayout.BELOW, players.getId());
        l.addView(playButton, p);

        View length = lengthSpinner();
        RelativeLayout.LayoutParams ps = ViewUtilities.createRelativeLayoutParams();
        ps.addRule(RelativeLayout.BELOW, playButton.getId());
        l.addView(length, ps);

        View diagonal = diagonal();
        RelativeLayout.LayoutParams pD = ViewUtilities.createRelativeLayoutParams();
        pD.addRule(RelativeLayout.BELOW, length.getId());
        l.addView(diagonal, pD);

        View straight = straight();
        RelativeLayout.LayoutParams pS = ViewUtilities.createRelativeLayoutParams();
        pS.addRule(RelativeLayout.BELOW, diagonal.getId());
        l.addView(straight, pS);

        setContentView(l);


    }

    private View straight()
    {
        RelativeLayout l = new RelativeLayout(this);
        TextView t = new TextView(this);
        t.setText("Straight");
        t.setId(View.generateViewId());
        l.addView(t);

        // TODO: height of dices should be same as textview
        straight = new StraightLayout(this, 3, 50);
        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        final int id = t.getId();
        p.addRule(RelativeLayout.RIGHT_OF, id);
        p.leftMargin = 100;
        l.addView(straight, p);

        straight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                straight.increaseLength();
            }
        });

        l.setId(View.generateViewId());
        return l;
    }

    private View diagonal()
    {
        RelativeLayout l = new RelativeLayout(this);
        diagonal = new CheckBox(this);
        TextView t = new TextView(this);
        t.setText("Diagonal");
        t.setId(View.generateViewId());
        l.addView(t);
        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        p.addRule(RelativeLayout.RIGHT_OF, t.getId());
        l.addView(diagonal, p);
        l.setId(View.generateViewId());
        return l;
    }

    private View lengthSpinner()
    {
        lengthSpinner = new Spinner(this);
        lengthSpinner.setId(View.generateViewId());
        List<String> l = new ArrayList<>();
        // TODO: make strings static variables
        l.add("Short");
        l.add("Normal");
        l.add("Long");
        int id = View.generateViewId();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthSpinner.setAdapter(adapter);
        return lengthSpinner;
    }

    View playButton()
    {
        Button b = new Button(this);
        b.setId(View.generateViewId());
        b.setText("Play");

        b.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(OptionActivity.this, LocalGameActivity.class);
                boolean[] playing = new boolean[4];
                int numberPlayers = 0;
                for (int i = 0; i < MaxPlayers; i++)
                {
                    playing[i] = playingCb[i].isChecked();
                    if (playing[i])
                    {
                        numberPlayers++;
                    }
                    Logger.logInfo("Options", i + " playing: " + playing[i]);
                }

                if (numberPlayers == 0)
                {
                    Toast.makeText(OptionActivity.this, "At least one player should play", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra(PlayingIntent, playing);

                String[] players = new String[4];
                for (int i = 0; i < MaxPlayers; i++)
                {
                    players[i] = editPlayers[i].getText().toString();
                    Logger.logInfo("Options", players[i]);
                }

                intent.putExtra(PlayerIntent, players);
                intent.putExtra(LengthIntent, (String) lengthSpinner.getSelectedItem());
                intent.putExtra(DiagonalIntent, diagonal.isChecked());
                intent.putExtra(StraightIntent, straight.getLength());

                startActivity(intent);
            }
        });
        b.setId(View.generateViewId());
        return b;
    }

    View createPlayers()
    {
        RelativeLayout l = new RelativeLayout(this);

        int lastId = 0;
        for (int i = 0; i < MaxPlayers; i++)
        {
            View player = createPlayerEdit(i);
            player.setId(View.generateViewId());
            if (lastId == 0)
            {
                l.addView(player);
            }
            else
            {
                RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
                p.addRule(RelativeLayout.BELOW, lastId);
                l.addView(player, p);
            }

            lastId = player.getId();
        }

        l.setId(View.generateViewId());
        return l;
    }

    View createPlayerEdit(int number)
    {
        RelativeLayout l = new RelativeLayout(this);
        EditText e = new EditText(this);
        e.setText("Player " + (number + 1));
        e.setId(View.generateViewId());
        l.addView(e);

        editPlayers[number] = e;
        CheckBox playing = new CheckBox(this);
        playing.setId(View.generateViewId());

        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        p.addRule(RelativeLayout.RIGHT_OF, e.getId());

        l.addView(playing, p);
        playingCb[number] = playing;
        return l;
    }
}
