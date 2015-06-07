package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.CalcPointLimit;
import games.runje.dicy.layouts.StraightLayout;
import games.runje.dicy.layouts.XOfAKindLayout;
import games.runje.dicy.util.ViewUtilities;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.ai.Strategy;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see games.runje.dicy.util.SystemUiHider
 */
public class OptionActivity extends Activity
{
    public static final String PlayingIntent = "Playing";
    public static final String PlayerIntent = "Player";
    public static final String LengthIntent = "Length";
    public static final String DiagonalIntent = "Diagonal";
    public static final String StraightIntent = "Straight";
    public static final String XOfAKindIntent = "XOfAKind";
    public static final String StrategyIntent = "Strategy";
    final static int MaxPlayers = 4;
    private static String PointLimitIntent = "PointLimit";
    private EditText[] editPlayers = new EditText[MaxPlayers];
    private CheckBox[] playingCb = new CheckBox[MaxPlayers];
    private Spinner[] strategySpinner = new Spinner[MaxPlayers];
    private Spinner lengthSpinner;
    public static String LogKey = "Options";
    private CheckBox diagonal;
    private StraightLayout straight;
    private XOfAKindLayout xOfAKind;
    private int size = 75;

    public static Rules getRulesFromBundle(Bundle bundle)
    {
        String length = bundle.getString(OptionActivity.LengthIntent);
        int f = 5;
        switch (length)
        {
            case "Short":
                f = 5;
                break;
            case "Middle":
                f = 10;
                break;
            case "Long":
                f = 20;
                break;
        }

        boolean diagonal = bundle.getBoolean(OptionActivity.DiagonalIntent, false);

        Rules rules = new Rules();
        rules.setDiagonalActive(diagonal);
        rules.setMinStraight(bundle.getInt(OptionActivity.StraightIntent, 7));
        rules.setMinXOfAKind(bundle.getInt(OptionActivity.XOfAKindIntent, 11));
        rules.initStraightPoints(4);

        int pointLimit = bundle.getInt(PointLimitIntent, -1);
        rules.setPointLimit(pointLimit);
        return rules;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Logger.logInfo(LogKey, "On Create");

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_option);
        lengthSpinner();
        View straight = straight();
        LinearLayout layout = (LinearLayout) findViewById(R.id.rules_layout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        if (straight.getParent() == null)
        {
            layout.addView(straight, params);
        }

        View x = xOfAKind();
        if (x.getParent() == null)
        {
            layout.addView(x, params);
        }

        this.diagonal = (CheckBox) findViewById(R.id.checkbox_diagonal);
    }

    private View xOfAKind()
    {
        RelativeLayout l = new RelativeLayout(this);
        ImageView t = new ImageView(this);
        t.setImageResource(R.drawable.dice3droll);
        t.setId(Utilities.generateViewId());
        l.addView(t, new ViewGroup.LayoutParams(size, size));

        // TODO: height of dices should be same as textview
        xOfAKind = new XOfAKindLayout(this, 3, size, 7);
        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        final int id = t.getId();
        p.addRule(RelativeLayout.RIGHT_OF, id);
        //p.addRule(RelativeLayout.ALIGN_LEFT, straight.getId());
        p.leftMargin = 55;


        l.addView(xOfAKind, p);

        xOfAKind.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                xOfAKind.increaseLength();
            }
        });

        l.setId(Utilities.generateViewId());
        return l;
    }

    private View straight()
    {
        RelativeLayout l = new RelativeLayout(this);
        ImageView t = new ImageView(this);
        t.setImageResource(R.drawable.straight);
        t.setId(Utilities.generateViewId());
        l.addView(t, new ViewGroup.LayoutParams(this.size, size));


        // TODO: height of dices should be same as textview
        straight = new StraightLayout(this, 3, size, 1);
        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        final int id = t.getId();
        p.addRule(RelativeLayout.RIGHT_OF, id);
        p.leftMargin = 55;
        l.addView(straight, p);

        straight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                straight.increaseLength();
            }
        });

        l.setId(Utilities.generateViewId());
        return l;
    }

    private View lengthSpinner()
    {
        lengthSpinner = (Spinner) findViewById(R.id.length_dropdown);
        List<String> l = new ArrayList<>();
        // TODO: make strings static variables
        l.add("Short");
        l.add("Normal");
        l.add("Long");
        int id = Utilities.generateViewId();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthSpinner.setAdapter(adapter);
        return lengthSpinner;
    }

    private void saveToBundle(Bundle intent)
    {
        boolean[] playing = {true, true, false, false};


        String player1 = ((EditText) findViewById(R.id.player1_name)).getText().toString();
        String player2 = ((EditText) findViewById(R.id.player2_name)).getText().toString();
        String[] players = {player1, player2, "", ""};

        ToggleButton p1 = (ToggleButton) findViewById(R.id.player1_strategy);
        String s1 = p1.isChecked() ? Strategy.Human : Strategy.Simple;
        ToggleButton p2 = (ToggleButton) findViewById(R.id.player2_strategy);
        String s2 = p2.isChecked() ? Strategy.Human : Strategy.Simple;
        String[] strategies = {s1, s2, Strategy.Human, Strategy.Human};

        intent.putBooleanArray(PlayingIntent, playing);
        intent.putStringArray(StrategyIntent, strategies);

        intent.putStringArray(PlayerIntent, players);
        intent.putString(LengthIntent, (String) lengthSpinner.getSelectedItem());
        intent.putBoolean(DiagonalIntent, diagonal.isChecked());
        intent.putInt(StraightIntent, straight.getLength());
        intent.putInt(XOfAKindIntent, xOfAKind.getLength());
    }


    public void clickPlay(final View v)
    {

        final Intent intent = new Intent(OptionActivity.this, LocalGameActivity.class);
        final Bundle b = new Bundle();
        saveToBundle(b);


        if (straight.getLength() == straight.MaxLength + 1 && xOfAKind.getLength() == xOfAKind.MaxLength + 1)
        {
            Toast.makeText(OptionActivity.this, "No Points possible", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: show Loading screen
        v.setEnabled(false);

        final Rules rules = getRulesFromBundle(b);

        new CalcPointLimit(rules, new Runnable()
        {
            @Override
            public void run()
            {
                b.putInt(PointLimitIntent, rules.getPointLimit());

                intent.putExtras(b);
                startActivity(intent);
                v.setEnabled(true);
            }
        }).execute();


    }

    View createPlayerEdit(int number)
    {
        RelativeLayout l = new RelativeLayout(this);
        EditText e = new EditText(this);
        e.setText("Player " + (number + 1));
        e.setId(Utilities.generateViewId());
        l.addView(e);

        editPlayers[number] = e;
        CheckBox playing = new CheckBox(this);
        playing.setId(Utilities.generateViewId());

        RelativeLayout.LayoutParams p = ViewUtilities.createRelativeLayoutParams();
        p.addRule(RelativeLayout.RIGHT_OF, e.getId());

        l.addView(playing, p);
        playingCb[number] = playing;

        Spinner s = new Spinner(this);
        s.setId(Utilities.generateViewId());
        List<String> list = new ArrayList<>();
        list.add(Strategy.Human);
        list.add(Strategy.Simple);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        strategySpinner[number] = s;

        RelativeLayout.LayoutParams pS = ViewUtilities.createRelativeLayoutParams();
        pS.addRule(RelativeLayout.RIGHT_OF, playing.getId());
        l.addView(s, pS);
        return l;
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Logger.logInfo(LogKey, "On Start");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Logger.logInfo(LogKey, "On Stop");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Logger.logInfo(LogKey, "On Pause");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Logger.logInfo(LogKey, "On Resume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        Logger.logInfo(LogKey, "On Save");

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed()
    {

        startActivity(new Intent(this, StartActivity.class));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {

        super.onRestoreInstanceState(savedInstanceState);


        EditText player1 = (EditText) findViewById(R.id.player1_name);
        EditText player2 = (EditText) findViewById(R.id.player2_name);

        ToggleButton p1 = (ToggleButton) findViewById(R.id.player1_strategy);
        ToggleButton p2 = (ToggleButton) findViewById(R.id.player2_strategy);

        String[] strategies = savedInstanceState.getStringArray(StrategyIntent);

        p1.setChecked(strategies[0] == Strategy.Human);
        p2.setChecked(strategies[1] == Strategy.Human);

        String[] players = savedInstanceState.getStringArray(PlayerIntent);

        player1.setText(players[0]);
        player2.setText(players[1]);


        String selectedItem = savedInstanceState.getString(LengthIntent);
        int pos = 0;
        switch (selectedItem)
        {
            case "Short":
                pos = 0;
                break;
            case "Middle":
                pos = 1;
                break;
            case "Long":
                pos = 2;
                break;
        }

        lengthSpinner.setSelection(pos);

        diagonal.setChecked(savedInstanceState.getBoolean(DiagonalIntent));
        straight.setLength(savedInstanceState.getInt(StraightIntent));
        xOfAKind.setLength(savedInstanceState.getInt(XOfAKindIntent));
        Logger.logInfo(LogKey, "On Restore");
    }
}
