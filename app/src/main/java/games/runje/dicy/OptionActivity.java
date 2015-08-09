package games.runje.dicy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.CalcPointLimit;
import games.runje.dicy.layouts.NamesArrayAdapter;
import games.runje.dicy.layouts.SimpleObserver;
import games.runje.dicy.layouts.SkillChooser;
import games.runje.dicy.layouts.StraightLayout;
import games.runje.dicy.layouts.XOfAKindLayout;
import games.runje.dicy.statistics.GameStatistic;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicy.util.ViewUtilities;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.skills.Skill;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see games.runje.dicy.util.SystemUiHider
 */
public class OptionActivity extends Activity implements SimpleObserver
{
    public static final String Player1Intent = "Player1";
    public static final String Player2Intent = "Player2";
    public static final String LengthIntent = "Length";
    public static final String DiagonalIntent = "Diagonal";
    public static final String StraightIntent = "Straight";
    public static final String XOfAKindIntent = "XOfAKind";
    public static final String Player1Skill1Intent = "Player1Skill1";
    public static final String Player1Skill2Intent = "Player1Skill2";
    public static final String Player1Skill3Intent = "Player1Skill3";
    public static final String Player2Skill1Intent = "Player2Skill1";
    public static final String Player2Skill2Intent = "Player2Skill2";
    public static final String Player2Skill3Intent = "Player2Skill3";
    public static final String Player1Skill1ValueIntent = "Player1Skill1Value";
    public static final String Player1Skill2ValueIntent = "Player1Skill2Value";
    public static final String Player1Skill3ValueIntent = "Player1Skill3Value";
    public static final String Player2Skill1ValueIntent = "Player2Skill1Value";
    public static final String Player2Skill2ValueIntent = "Player2Skill2Value";
    public static final String Player2Skill3ValueIntent = "Player2Skill3Value";
    public static String LogKey = "Options";
    private static String PointLimitIntent = "PointLimit";
    private Spinner lengthSpinner;
    private Spinner player1Spinner;
    private Spinner player2Spinner;
    private CheckBox diagonal;
    private StraightLayout straight;
    private XOfAKindLayout xOfAKind;
    private int size = 75;
    private NamesArrayAdapter player1Adapter;
    private NamesArrayAdapter player2Adapter;
    private SkillChooser skillChooser1;
    private int[] player1Skills = { R.id.player1_skill1, R.id.player1_skill2, R.id.player1_skill3};
    private SkillChooser skillChooser2;
    private int[] player2Skills = { R.id.player2_skill1, R.id.player2_skill2, R.id.player2_skill3};;
    private TextView player1Stats1;
    private TextView player1Stats2;
    private TextView player2Stats1;
    private TextView player2Stats2;

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
        rules.setLengthFactor(f);

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

        initSkills();

        names();
        lengthSpinner();
        View straightView = straight();
        LinearLayout layout = (LinearLayout) findViewById(R.id.rules_layout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        if (straightView.getParent() == null)
        {
            layout.addView(straightView, params);
        }

        View x = xOfAKind();
        if (x.getParent() == null)
        {
            layout.addView(x, params);
        }

        initStatistics();
    }

    private void initStatistics()
    {
        player1Stats1 = (TextView) findViewById(R.id.text_player1_stats1);
        player1Stats2 = (TextView) findViewById(R.id.text_player1_stats2);
        player2Stats1 = (TextView) findViewById(R.id.text_player2_stats1);
        player2Stats2 = (TextView) findViewById(R.id.text_player2_stats2);

        updateStats();
    }

    private void updateStats()
    {
        StatisticManager manager = new SQLiteHandler(this);

        PlayerStatistic player1 = manager.getPlayer(((PlayerStatistic) player1Spinner.getSelectedItem()).getName());
        PlayerStatistic player2 = manager.getPlayer(((PlayerStatistic) player2Spinner.getSelectedItem()).getName());

        player1Stats1.setText(player1.getWins() + " / " + (player1.getGames()));
        player2Stats1.setText(player2.getWins() + " / " + (player2.getGames()));

        List<GameStatistic> games = manager.getGames(player1.getName(), player2.getName());
        int p1Wins = 0;

        for (GameStatistic game : games)
        {
            if (game.getPlayer1().getName().equals(player1.getName()) && game.hasP1Won())
            {
                p1Wins++;
            }

            if (game.getPlayer2().getName().equals(player1.getName()) && !game.hasP1Won()) {
                p1Wins++;
            }
        }

        int p2Wins = games.size() - p1Wins;
        player1Stats2.setText(p1Wins + " / " + games.size());
        player2Stats2.setText(p2Wins + " / " + games.size());
    }

    private void initSkills()
    {
        skillChooser1 = new SkillChooser(this, Skill.AllSkills, new int[] { 1, 2, 3});
        for (int i = 0; i < player1Skills.length; i++)
        {
            View skill = findViewById(player1Skills[i]);

            final int finalI = i;
            skill.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    skillChooser1.showDialog(OptionActivity.this, finalI);
                }
            });
        }

        skillChooser2 = new SkillChooser(this, Skill.AllSkills, new int[] { 1, 2, 3});
        for (int i = 0; i < player1Skills.length; i++)
        {
            View skill = findViewById(player2Skills[i]);

            final int finalI = i;
            skill.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    skillChooser2.showDialog(OptionActivity.this, finalI);
                }
            });
        }

        update();
    }

    private void names()
    {
        Logger.logInfo(LogKey, "Init names");
        player1Spinner = (Spinner) findViewById(R.id.player1_name);
        StatisticManager manager = new SQLiteHandler(this);
        List<PlayerStatistic> players = manager.getAllPlayers();
        List<PlayerStatistic> players2 = new ArrayList<>();
        for (int i = 0; i < players.size(); i++)
        {
            PlayerStatistic playerStatistic = players.get(i);
            players2.add(playerStatistic);

        }
        player1Adapter = new NamesArrayAdapter(this, players, 1);
        player2Adapter = new NamesArrayAdapter(this, players2, 0);
        player1Spinner.setAdapter(player1Adapter);
        player2Spinner = (Spinner) findViewById(R.id.player2_name);
        player2Spinner.setAdapter(player2Adapter);

        player1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                player2Adapter.setSelectedOther(i);
                updateStats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        player2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                player1Adapter.setSelectedOther(i);
                updateStats();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    public void click_createPlayer(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.create_player_dialog, null);
        builder.setView(view);
// Add the buttons
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                // User clicked OK button
                EditText editText = (EditText) view.findViewById(R.id.editText2);
                String name = editText.getText().toString();
                StatisticManager manager = new SQLiteHandler(OptionActivity.this);
                PlayerStatistic player = manager.createPlayer(name, Strategy.Human);
                player1Adapter.addPlayer(player);
                player2Adapter.addPlayer(player);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, l);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthSpinner.setAdapter(adapter);
        return lengthSpinner;
    }

    private void saveToBundle(Bundle intent)
    {
        String[] players = {((PlayerStatistic) player1Spinner.getSelectedItem()).getName(), ((PlayerStatistic) player2Spinner.getSelectedItem()).getName(), "", ""};

        intent.putStringArray(Player1Intent, players);
        intent.putString(LengthIntent, (String) lengthSpinner.getSelectedItem());
        intent.putBoolean(DiagonalIntent, diagonal.isChecked());
        intent.putInt(StraightIntent, straight.getLength());
        intent.putInt(XOfAKindIntent, xOfAKind.getLength());
        intent.putString(Player1Skill1Intent, skillChooser1.getName(0));
        intent.putString(Player1Skill2Intent, skillChooser1.getName(1));
        intent.putString(Player1Skill3Intent, skillChooser1.getName(2));
        intent.putString(Player2Skill1Intent, skillChooser2.getName(0));
        Logger.logInfo(LogKey, skillChooser2.getName(0));
        intent.putString(Player2Skill2Intent, skillChooser2.getName(1));
        intent.putString(Player2Skill3Intent, skillChooser2.getName(2));

        intent.putInt(Player1Skill1ValueIntent, skillChooser1.getValue(0));
        intent.putInt(Player1Skill2ValueIntent, skillChooser1.getValue(1));
        intent.putInt(Player1Skill3ValueIntent, skillChooser1.getValue(2));
        intent.putInt(Player2Skill1ValueIntent, skillChooser2.getValue(0));
        intent.putInt(Player2Skill2ValueIntent, skillChooser2.getValue(1));
        intent.putInt(Player2Skill3ValueIntent, skillChooser2.getValue(2));
    }

    private void saveToSharedPreferences()
    {
        Logger.logInfo(LogKey, "Save to shared preferences");
        PlayerStatistic player1 = (PlayerStatistic) player1Spinner.getSelectedItem();
        PlayerStatistic player2 = (PlayerStatistic) player2Spinner.getSelectedItem();

        String player1Name = "";
        if (player1 != null)
        {
            player1Name = player1.getName();
        }

        String player2Name = "";
        if (player2 != null)
        {
            player2Name = player2.getName();
        }
        String[] players = {player1Name, player2Name};

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();

        edit.putString(Player1Intent, players[0]);
        edit.putString(Player2Intent, players[1]);
        edit.putString(LengthIntent, (String) lengthSpinner.getSelectedItem());
        edit.putBoolean(DiagonalIntent, diagonal.isChecked());
        edit.putInt(StraightIntent, straight.getLength());
        edit.putInt(XOfAKindIntent, xOfAKind.getLength());
        edit.putString(Player1Skill1Intent, skillChooser1.getName(0));
        edit.putString(Player1Skill2Intent, skillChooser1.getName(1));
        edit.putString(Player1Skill3Intent, skillChooser1.getName(2));
        edit.putString(Player2Skill1Intent, skillChooser2.getName(0));
        edit.putString(Player2Skill2Intent, skillChooser2.getName(1));
        edit.putString(Player2Skill3Intent, skillChooser2.getName(2));

        edit.putInt(Player1Skill1ValueIntent, skillChooser1.getValue(0));
        edit.putInt(Player1Skill2ValueIntent, skillChooser1.getValue(1));
        edit.putInt(Player1Skill3ValueIntent, skillChooser1.getValue(2));
        edit.putInt(Player2Skill1ValueIntent, skillChooser2.getValue(0));
        edit.putInt(Player2Skill2ValueIntent, skillChooser2.getValue(1));
        edit.putInt(Player2Skill3ValueIntent, skillChooser2.getValue(2));
        edit.commit();
    }


    public void clickSkill1(View v)
    {
        skillChooser1.showDialog(this, 1);
    }

    private void loadSharedPreferences()
    {
        Logger.logInfo(LogKey, "Load shared prefs");
        this.diagonal = (CheckBox) findViewById(R.id.checkbox_diagonal);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        int p1 = getIndex(player1Spinner, sharedPreferences.getString(Player1Intent, "Player 1"));
        int p2 = getIndex(player2Spinner, sharedPreferences.getString(Player2Intent, "Player 2"));
        player1Spinner.setSelection(p1);
        if (p2 == p1)
        {
            p2++;
        }
        player2Spinner.setSelection(p2);

        String selectedItem = sharedPreferences.getString(LengthIntent, "Short");
        int pos = 0;
        switch (selectedItem)
        {
            case "Short":
                pos = 0;
                break;
            case "Normal":
                pos = 1;
                break;
            case "Long":
                pos = 2;
                break;
        }

        lengthSpinner.setSelection(pos);

        diagonal.setChecked(sharedPreferences.getBoolean(DiagonalIntent, false));
        straight.setLength(sharedPreferences.getInt(StraightIntent, 3));
        xOfAKind.setLength(sharedPreferences.getInt(XOfAKindIntent, 3));

        skillChooser1.setName(sharedPreferences.getString(Player1Skill1Intent, Skill.Help), 0);
        skillChooser1.setName(sharedPreferences.getString(Player1Skill2Intent, Skill.Change),1);
        skillChooser1.setName(sharedPreferences.getString(Player1Skill3Intent, Skill.Shuffle), 2);
        skillChooser2.setName(sharedPreferences.getString(Player2Skill1Intent, Skill.Help), 0);
        skillChooser2.setName(sharedPreferences.getString(Player2Skill2Intent, Skill.Change), 1);
        skillChooser2.setName(sharedPreferences.getString(Player2Skill3Intent, Skill.Shuffle), 2);

        skillChooser1.setValue(sharedPreferences.getInt(Player1Skill1ValueIntent, 1), 0);
        skillChooser1.setValue(sharedPreferences.getInt(Player1Skill2ValueIntent, 2), 1);
        skillChooser1.setValue(sharedPreferences.getInt(Player1Skill3ValueIntent, 3), 2);
        skillChooser2.setValue(sharedPreferences.getInt(Player2Skill1ValueIntent, 1), 0);
        skillChooser2.setValue(sharedPreferences.getInt(Player2Skill2ValueIntent, 2), 1);
        skillChooser2.setValue(sharedPreferences.getInt(Player2Skill3ValueIntent, 3), 2);
        update();
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (((PlayerStatistic) spinner.getItemAtPosition(i)).getName().equals(myString)){
                index = i;
                break;
            }
        }
        return index;
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
                saveToSharedPreferences();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.game_file_key), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LocalGameActivity.KEY_RESUME_GAME, false);
                editor.commit();
                startActivity(intent);
                v.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        v.setEnabled(true);
                    }
                }, 2000);
            }
        }).execute();


    }


    @Override
    protected void onStart()
    {
        loadSharedPreferences();
        super.onStart();
        Logger.logInfo(LogKey, "On Start");
    }

    @Override
    protected void onStop()
    {
        saveToSharedPreferences();
        super.onStop();
        Logger.logInfo(LogKey, "On Stop");
    }

    @Override
    protected void onPause()
    {
        saveToSharedPreferences();
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
        Logger.logInfo(LogKey, "On Restore");
    }

    @Override
    public void update()
    {
        for (int i = 0; i < player1Skills.length; i++)
        {
            View skill = findViewById(player1Skills[i]);

            TextView name = (TextView) skill.findViewById(R.id.skill_name);
            ImageView image = (ImageView) skill.findViewById(R.id.skill_image);
            ImageView value = (ImageView) skill.findViewById(R.id.skill_value);

            name.setText(skillChooser1.getName(i));

            image.setImageResource(skillChooser1.getImageId(i));
            value.setImageResource(skillChooser1.getLoadId(i));
        }

        for (int i = 0; i < player2Skills.length; i++)
        {
            View skill = findViewById(player2Skills[i]);

            TextView name = (TextView) skill.findViewById(R.id.skill_name);
            ImageView image = (ImageView) skill.findViewById(R.id.skill_image);
            ImageView value = (ImageView) skill.findViewById(R.id.skill_value);

            name.setText(skillChooser2.getName(i));

            image.setImageResource(skillChooser2.getImageId(i));
            value.setImageResource(skillChooser2.getLoadId(i));
        }
    }
}
