package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Rules;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LocalGameActivity extends Activity
{



    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        boolean[] playing = intent.getBooleanArrayExtra(OptionActivity.PlayingIntent);
        String[] players = intent.getStringArrayExtra(OptionActivity.PlayerIntent);
        List<String> p = new ArrayList<>();

        for (int i = 0; i < OptionActivity.MaxPlayers; i++)
        {
            if (playing[i])
            {
                p.add(players[i]);
                Logger.logInfo("LocalGameActivity", "adding " + players[i]);
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
        RelativeLayout l = new RelativeLayout(this);
        Gamemaster.createLocalGame(this, p, f, rules);
        AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();
        RelativeLayout b = board.getGameLayout();
        RelativeLayout.LayoutParams pB = (RelativeLayout.LayoutParams) b.getLayoutParams();
        pB.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        b.setId(R.id.board);
        l.addView(b, pB);

        RelativeLayout controls = Gamemaster.getInstance().getControls();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.board);
        params.topMargin = 50;
        l.addView(controls, params);
        setContentView(l);
    }

}
