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
import java.util.Random;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;


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


        View mainView = findViewById(R.id.board);
        mainView.post(new Runnable()
        {
            @Override
            public void run()
            {
                List<Player> playerList = new ArrayList<>();
                Player player1 = new Player("Thomas", 0);
                //player1.addSkill(new ChangeSkill(6,6));
                //player1.addSkill(new HelpSkill(1, 6));
                playerList.add(player1);

                Player player2 = new Player("Milena", 1);
                //player2.addSkill(new ChangeSkill(6,6));
                //player2.addSkill(new HelpSkill(1, 6));
                playerList.add(player2);

                Rules rules = new Rules();
                Board bb = Board.createBoardNoPoints(rules);
                LocalGame game = new LocalGame(playerList, new Random().nextInt(2), rules);
                AnimatedGamemaster gmAnimated = new AnimatedGamemaster(game, rules, GameActivity.this);
                LinearLayout boardContainer = (LinearLayout) findViewById(R.id.board);
                boardContainer.addView(gmAnimated.getAnimatedBoard().getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        });

    }

}
