package games.runje.dicy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.layouts.SkillChooser;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.SavedGame;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LocalGameActivity extends Activity
{
    public static final String KEY_RESUME_GAME = "resume game";
    public static final String KEY_RULES = "rules";
    public static String LogKey = "LocalGameActivity";
    private AnimatedGamemaster gmAnimated;
    private String KEY_GAME = "game";
    private String KEY_BOARD = "board";

    static Player playerStatisticsToPlayer(PlayerStatistic statistic, List<Skill> skills)
    {
        return new Player(statistic.getName(), Strategy.makeStrategy(statistic.getStrategy()), statistic.getId(), skills);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Logger.setInstance(new AnimatedLogger());
        Logger.init();
        Logger.logInfo(LogKey, "On Create");
        setContentView(R.layout.game);
        View mainView = findViewById(R.id.board);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.game_file_key), MODE_PRIVATE);
        boolean resumeGame = sharedPreferences.getBoolean(KEY_RESUME_GAME, false);

        final LocalGame game;
        final Rules rules;
        final Board board;
        final SavedGame savedGame;

        if (resumeGame)
        {
            Logger.logInfo(LogKey, "Resuming Game");
            String gameString = sharedPreferences.getString(KEY_GAME, "");
            byte[] bytes = Base64.decode(gameString, Base64.NO_WRAP);
            savedGame = MessageConverter.byteToSavedGame(ByteBuffer.wrap(bytes));
            game = savedGame.getGame();
            rules = savedGame.getRules();
            board = savedGame.getBoard();

            Logger.logDebug(LogKey, "Rules: " + rules);
            Logger.logDebug(LogKey, "Game: " + game);
        }
        else
        {
            savedGame = null;
            Logger.logInfo(LogKey, "New Game");
            rules = OptionActivity.getRulesFromBundle(getIntent().getExtras());
            List<Player> players = getPlayersFromIntent();

            game = new LocalGame(rules.getPointLimit(), rules.getLengthFactor(), players, new Random().nextInt(2));
            board = Board.createBoardNoPoints(rules);
        }

        mainView.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (savedGame == null)
                {
                    LocalGameActivity.this.gmAnimated = new AnimatedGamemaster(game, rules, LocalGameActivity.this, board);
                }
                else
                {
                    LocalGameActivity.this.gmAnimated = new AnimatedGamemaster(savedGame, LocalGameActivity.this);
                }
                ViewGroup boardContainer = (ViewGroup) findViewById(R.id.board);
                boardContainer.addView(gmAnimated.getAnimatedBoard().getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Logger.logInfo(LogKey, "On Start");
    }

    @Override
    protected void onResume()
    {
        Logger.logInfo(LogKey, "On Resume");

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Logger.logInfo(LogKey, "On Pause");
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.game_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GAME, Base64.encodeToString(MessageConverter.savedGameToByte(gmAnimated.getSavedGame()), Base64.NO_WRAP));
        editor.putBoolean(KEY_RESUME_GAME, true);
        Logger.logDebug(LogKey, "Rules: " + gmAnimated.getGame());
        Logger.logDebug(LogKey, "Game: " + gmAnimated.getRules());
        editor.commit();


        super.onPause();
    }

    @Override
    protected void onStop()
    {
        Logger.logInfo(LogKey, "On Stop");

        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Logger.logInfo(LogKey, "On Destroy");
    }

    void showQuitDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Game?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        builder.setPositiveButton("Quit Game", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                gmAnimated.getGame().cancel();
                Intent intent = new Intent(LocalGameActivity.this, OptionActivity.class);
                startActivity(intent);
            }
        });

        builder.create().show();
    }

    private List<Player> getPlayersFromIntent()
    {
        Bundle intent = getIntent().getExtras();
        String[] players = intent.getStringArray(OptionActivity.Player1Intent);
        List<String> p = new ArrayList<>();

        for (int i = 0; i < 2; i++)
        {
                p.add(players[i]);
                AnimatedLogger.logDebug("LocalGameActivity", "adding " + players[i]);
        }

        SkillChooser skillChooser1 = new SkillChooser();
        SkillChooser skillChooser2 = new SkillChooser();
        skillChooser1.setName(intent.getString(OptionActivity.Player1Skill1Intent, Skill.Help), 0);
        skillChooser1.setName(intent.getString(OptionActivity.Player1Skill2Intent, Skill.Change),1);
        skillChooser1.setName(intent.getString(OptionActivity.Player1Skill3Intent, Skill.Shuffle), 2);
        skillChooser2.setName(intent.getString(OptionActivity.Player2Skill1Intent, Skill.Help), 0);
        Logger.logInfo(LogKey, intent.getString(OptionActivity.Player2Skill1Intent, Skill.Help));
        skillChooser2.setName(intent.getString(OptionActivity.Player2Skill2Intent, Skill.Change), 1);
        skillChooser2.setName(intent.getString(OptionActivity.Player2Skill3Intent, Skill.Shuffle), 2);

        skillChooser1.setValue(intent.getInt(OptionActivity.Player1Skill1ValueIntent, 1), 0);
        skillChooser1.setValue(intent.getInt(OptionActivity.Player1Skill2ValueIntent, 2), 1);
        skillChooser1.setValue(intent.getInt(OptionActivity.Player1Skill3ValueIntent, 3), 2);
        skillChooser2.setValue(intent.getInt(OptionActivity.Player2Skill1ValueIntent, 1), 0);
        skillChooser2.setValue(intent.getInt(OptionActivity.Player2Skill2ValueIntent, 2), 1);
        skillChooser2.setValue(intent.getInt(OptionActivity.Player2Skill3ValueIntent, 3), 2);

        List<Player> playerList = new ArrayList<>();
        StatisticManager manager = new SQLiteHandler(this);

        PlayerStatistic player = manager.getPlayer(players[0]);

        List<Skill> skills = skillChooser1.getSkills();
        playerList.add(playerStatisticsToPlayer(player, skills));

        PlayerStatistic player2 = manager.getPlayer(players[1]);

        List<Skill> skills2 = skillChooser2.getSkills();
        playerList.add(playerStatisticsToPlayer(player2, skills2));

        return playerList;
    }

    @Override
    public void onBackPressed()
    {
        showQuitDialog();
    }

}
