package games.runje.dicy.controls;

import android.app.Activity;
import android.widget.RelativeLayout;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 08.01.2015.
 */
public class ArenaControls extends Controls
{
    public ArenaControls(Activity context, LocalGame g, AnimatedBoard b, AnimatedGamemaster gm)
    {
        super(context, b, gm);
        this.game = g;
        addView(diagonalCheck());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.diagonal);
        addView(minStraight(), params);

        RelativeLayout.LayoutParams pX = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pX.addRule(RelativeLayout.RIGHT_OF, R.id.straight);
        pX.addRule(RelativeLayout.ALIGN_BASELINE, R.id.straight);
        addView(minXOfAKind(), pX);

        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pp.addRule(RelativeLayout.BELOW, R.id.straight);
        addView(playerPoints(), pp);

        RelativeLayout.LayoutParams pR = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pR.addRule(RelativeLayout.RIGHT_OF, R.id.straight);
        pR.addRule(RelativeLayout.ALIGN_BASELINE, R.id.playersPoints);
        addView(restart(), pR);

        RelativeLayout.LayoutParams pA = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pA.addRule(RelativeLayout.BELOW, R.id.playersPoints);
        addView(gravityArrows(), pA);
    }

    public void updatePlayers()
    {
        Logger.logInfo("Arena", "Update: " + Integer.toString(game.getSwitchPoints() + game.getMovePoints() + game.getPlayers().get(0).getPoints()));
        playersView.get(0).setText("Points: " + Integer.toString(game.getSwitchPoints() + game.getMovePoints()));
    }
}
