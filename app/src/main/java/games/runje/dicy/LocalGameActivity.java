package games.runje.dicy;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Player;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LocalGameActivity extends Activity
{
    public static String LogKey = "LocalGameActivity";
    private AnimatedGamemaster gmAnimated;

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Logger.setInstance(new AnimatedLogger());
        Logger.init();
        Logger.logInfo(LogKey, "On Post Create");
        setContentView(R.layout.game);
        View mainView = findViewById(R.id.board);
        mainView.post(new Runnable()
        {
            @Override
            public void run()
            {
                Rules rules = OptionActivity.getRulesFromBundle(getIntent().getExtras());
                List<Player> players = getPlayersFromIntent();

                LocalGameActivity.this.gmAnimated = new AnimatedGamemaster(players, rules, LocalGameActivity.this);
                LinearLayout boardContainer = (LinearLayout) findViewById(R.id.board);
                boardContainer.addView(gmAnimated.getAnimatedBoard().getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            }
        });

    }

    private List<Player> getPlayersFromIntent()
    {
        Bundle intent = getIntent().getExtras();
        String[] players = intent.getStringArray(OptionActivity.Player1Intent);
        List<String> p = new ArrayList<>();

        for (int i = 0; i < OptionActivity.MaxPlayers; i++)
        {
                p.add(players[i]);
                AnimatedLogger.logDebug("LocalGameActivity", "adding " + players[i]);
        }

        List<Player> playerList = new ArrayList<>();
        StatisticManager manager = new SQLiteHandler(this);
        for (int i = 0; i < 2; i++)
        {
            String name = p.get(i);
            PlayerStatistic player = manager.getPlayer(name);

            playerList.add(playerStatisticsToPlayer(player));
        }

        return playerList;
    }

    static Player playerStatisticsToPlayer(PlayerStatistic statistic)
    {
        return new Player(statistic.getName(), Strategy.makeStrategy(statistic.getStrategy()), statistic.getId());
    }

    @Override
    public void onBackPressed()
    {
        gmAnimated.getGame().cancel();
        super.onBackPressed();
    }

}
