package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.animatedData.animatedSkills.AnimatedSkill;
import games.runje.dicy.controller.AIController;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.CalcPointLimit;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicy.layouts.GameLayout;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
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


    private String LogKey = "LocalGameActivity";
    private GamemasterAnimated gmAnimated;

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Logger.setInstance(new AnimatedLogger());
        Logger.logInfo(LogKey, "On Post Create");
        Intent intent = getIntent();
        boolean[] playing = intent.getBooleanArrayExtra(OptionActivity.PlayingIntent);
        String[] players = intent.getStringArrayExtra(OptionActivity.PlayerIntent);
        List<String> p = new ArrayList<>();

        for (int i = 0; i < OptionActivity.MaxPlayers; i++)
        {
            if (playing[i])
            {
                p.add(players[i]);
                AnimatedLogger.logInfo("LocalGameActivity", "adding " + players[i]);
            }
        }

        String[] strategies = intent.getStringArrayExtra(OptionActivity.StrategyIntent);
        List<Strategy> s = new ArrayList<>();

        for (int i = 0; i < OptionActivity.MaxPlayers; i++)
        {
            if (playing[i])
            {
                s.add(Strategy.makeStrategy(strategies[i]));
                AnimatedLogger.logInfo("LocalGameActivity", "adding " + strategies[i]);
            }
        }

        String length = intent.getStringExtra(OptionActivity.LengthIntent);
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

        boolean diagonal = intent.getBooleanExtra(OptionActivity.DiagonalIntent, false);

        Rules rules = new Rules();
        rules.setDiagonalActive(diagonal);
        rules.setMinStraight(intent.getIntExtra(OptionActivity.StraightIntent, 7));
        rules.setMinXOfAKind(intent.getIntExtra(OptionActivity.XOfAKindIntent, 11));
        rules.initStraightPoints(4);

        Board bb = Board.createBoardNoPoints(5, 5, rules);

        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < p.size(); i++)
        {
            String name = p.get(i);
            Strategy strategy = s.get(i);
            playerList.add(new Player(name, strategy, 77));
        }

        LocalGame game = new LocalGame(rules.getPointLimit(), f, playerList, 0);
        for (Player player : game.getPlayers())
        {
            List<Skill> animatedSkills = new ArrayList<>();
            for (Skill skill : player.getSkills())
            {
                animatedSkills.add(AnimatedSkill.create(skill));
            }

            player.setSkills(animatedSkills);
        }


        // TODO: gamemaster
        LocalGameControls controls = new LocalGameControls(this, game, null, null, null);
        new CalcPointLimit(bb, rules, controls, game).execute();

        this.gmAnimated = new GamemasterAnimated(bb, rules, this, controls, game);
        LocalGameControls controlsView = (LocalGameControls) gmAnimated.getControls();
        RelativeLayout left = new RelativeLayout(this);

        RelativeLayout.LayoutParams nextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nextParams.topMargin = 20;
        View next = controlsView.getNext();
        next.setId(View.generateViewId());
        left.addView(next, nextParams);

        RelativeLayout.LayoutParams pointListParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointListParams.addRule(RelativeLayout.RIGHT_OF, next.getId());
        pointListParams.topMargin = 20;
        pointListParams.leftMargin = 20;
        left.addView(controlsView.getPointList(), pointListParams);

        GameLayout gameLayout = new GameLayout(this, gmAnimated.getAnimatedBoard().getBoardLayout(), left, controlsView.getPoints(), controlsView.getPlayerLayouts().get(0), controlsView.getPlayerLayouts().get(1));
        setContentView(gameLayout);
        // start AI
        for (Player pl : game.getPlayers())
        {
            if (pl.isAi())
            {
                // TODO: gamemaster
                new AIController(pl, this, null, gmAnimated);
            }
        }

    }

    @Override
    public void onBackPressed()
    {
        gmAnimated.getGame().cancel();
        super.onBackPressed();
    }

}
