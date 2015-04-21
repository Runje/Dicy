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
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.animatedSkills.AnimatedSkill;
import games.runje.dicy.controller.CalcPointLimit;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicy.util.SystemUiHider;
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
public class GameActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);


        View mainView = (View) findViewById(R.id.board);
        mainView.post(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = getIntent();
                List<String> players = new ArrayList<>();
                players.add("Thomas");
                players.add("Milena");
                List<Player> playerList = new ArrayList<>();
                for (int i = 0; i < players.size(); i++)
                {
                    String name = players.get(i);
                    Strategy strategy = null;
                    playerList.add(new Player(name, strategy, 77));
                }

                // TODO: gamemaster
                Rules rules = new Rules();
                LocalGame game = new LocalGame(rules.getPointLimit(), rules.getPointLimit() * 5, playerList, 0);
                for (Player player : game.getPlayers())
                {
                    List<Skill> animatedSkills = new ArrayList<>();
                    for (Skill skill : player.getSkills())
                    {
                        animatedSkills.add(AnimatedSkill.create(skill));
                    }

                    player.setSkills(animatedSkills);
                }
                Board bb = Board.createBoardNoPoints(5, 5, rules);
                LocalGameControls controls = new LocalGameControls(GameActivity.this, game, null, null, null);
                new CalcPointLimit(bb, rules, controls, game).execute();
                GamemasterAnimated gmAnimated = new GamemasterAnimated(bb, rules, GameActivity.this, controls, game);

                boolean diagonal = intent.getBooleanExtra(OptionActivity.DiagonalIntent, false);

                RelativeLayout l = new RelativeLayout(GameActivity.this);
                // TODO: create local game here
                AnimatedBoard board = gmAnimated.getAnimatedBoard();
                RelativeLayout b = board.getBoardLayout();
                RelativeLayout.LayoutParams pB = (RelativeLayout.LayoutParams) b.getLayoutParams();
                pB.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

                LinearLayout boardContainer = (LinearLayout) findViewById(R.id.board);
                boardContainer.addView(b, ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });

    }

}
