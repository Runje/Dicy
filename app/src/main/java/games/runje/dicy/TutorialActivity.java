package games.runje.dicy;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.tutorial.SwitchTutorialGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.ChangeSkill;
import games.runje.dicymodel.skills.HelpSkill;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 08.06.2015.
 */
public class TutorialActivity extends Activity
{

    private String LogKey = "TestActivity";
    private AnimatedGamemaster gmAnimated;

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

        final LocalGame game;
        final Rules rules;
        final Board board;

        Logger.logInfo(LogKey, "New Game");
        rules = new Rules();
        rules.setPointLimit(40);
        List<Skill> skills1 = new ArrayList<Skill>();
        skills1.add(new ChangeSkill(1, 6));
        skills1.add(new HelpSkill(6, 6));
        skills1.add(new ShuffleSkill(5, 6));

        List<Skill> skills2 = new ArrayList<Skill>();
        skills2.add(new ChangeSkill(1, 6));
        skills2.add(new HelpSkill(5, 6));
        skills2.add(new ShuffleSkill(6, 6));
        List<Player> players = new ArrayList<>();
        players.add(new Player("You", null, 0, skills1));
        // TODO: Make tutorial AI
        players.add(new Player("Opponent", Strategy.makeStrategy(Strategy.Simple), 1, skills2));

        game = new LocalGame(rules.getPointLimit(), rules.getLengthFactor(), players, 0);
        rules.setMinStraight(3);
        board = Board.createBoardNoPoints(rules);

        mainView.post(new Runnable()
        {
            @Override
            public void run()
            {
                TutorialActivity.this.gmAnimated = new SwitchTutorialGamemaster(game, rules, TutorialActivity.this, board);
                ViewGroup boardContainer = (ViewGroup) findViewById(R.id.board);
                boardContainer.addView(gmAnimated.getAnimatedBoard().getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        LocalGameActivity.showQuitDialog(this, gmAnimated.getGame());
    }


}
