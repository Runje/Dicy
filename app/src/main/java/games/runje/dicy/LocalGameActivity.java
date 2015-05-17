package games.runje.dicy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
                Rules rules = NewOptionActivity.getRulesFromIntent(getIntent());
                List<Player> players = getPlayersFromIntent();

                LocalGameActivity.this.gmAnimated = new AnimatedGamemaster(players, rules, LocalGameActivity.this);
                LinearLayout boardContainer = (LinearLayout) findViewById(R.id.board);
                boardContainer.addView(gmAnimated.getAnimatedBoard().getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            }
        });

    }

    private List<Player> getPlayersFromIntent()
    {
        Intent intent = getIntent();
        boolean[] playing = intent.getBooleanArrayExtra(NewOptionActivity.PlayingIntent);
        String[] players = intent.getStringArrayExtra(NewOptionActivity.PlayerIntent);
        List<String> p = new ArrayList<>();

        for (int i = 0; i < NewOptionActivity.MaxPlayers; i++)
        {
            if (playing[i])
            {
                p.add(players[i]);
                AnimatedLogger.logDebug("LocalGameActivity", "adding " + players[i]);
            }
        }

        String[] strategies = intent.getStringArrayExtra(NewOptionActivity.StrategyIntent);
        List<Strategy> s = new ArrayList<>();

        for (int i = 0; i < NewOptionActivity.MaxPlayers; i++)
        {
            if (playing[i])
            {
                s.add(Strategy.makeStrategy(strategies[i]));
                AnimatedLogger.logDebug("LocalGameActivity", "adding " + strategies[i]);
            }
        }

        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < p.size(); i++)
        {
            String name = p.get(i);
            Strategy strategy = s.get(i);
            playerList.add(new Player(name, strategy, i));
        }

        return playerList;
    }



    @Override
    public void onBackPressed()
    {
        gmAnimated.getGame().cancel();
        super.onBackPressed();
    }

}
