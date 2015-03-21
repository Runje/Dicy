package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
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
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
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
        Board bb = Board.createBoardNoPoints(5, 5, rules);
        LocalGameControls controls = new LocalGameControls(this, game, null, null, null);
        GamemasterAnimated gmAnimated = new GamemasterAnimated(bb, rules, this, controls, game);

        //RelativeLayout l = gmAnimated.getAnimatedBoard().getGameLayout();
        boolean diagonal = intent.getBooleanExtra(OptionActivity.DiagonalIntent, false);

        RelativeLayout l = new RelativeLayout(this);
        // TODO: create local game here
        AnimatedBoard board = gmAnimated.getAnimatedBoard();
        RelativeLayout b = board.getGameLayout();
        RelativeLayout.LayoutParams pB = (RelativeLayout.LayoutParams) b.getLayoutParams();
        pB.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        b.setId(R.id.board);
        l.addView(b, pB);

        RelativeLayout c = (RelativeLayout) gmAnimated.getControls();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.board);
        params.topMargin = 50;
        l.addView(c, params);

        setContentView(l);
    }

}
