package games.runje.dicy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.util.SystemUiHider;


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

       RelativeLayout l = new RelativeLayout(this);
        Gamemaster.createLocalGame(this);
        AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();
        RelativeLayout b = board.getGameLayout();
        b.setId(R.id.board);
        l.addView(b);

        RelativeLayout controls = Gamemaster.getInstance().getControls();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.board);
        params.topMargin = 50;
        l.addView(controls, params);
        setContentView(l);
    }

}
