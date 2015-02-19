package games.runje.dicy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.OnlineGamemaster;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.communication.ConnectionToServer;
import games.runje.dicymodel.data.Gravity;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class OnlineGameActivity extends Activity
{
    private ConnectionToServer client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_game);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        TextView status = (TextView) findViewById(R.id.StatusClient);

        ConnectionToServer.connect(OnlineGamemaster.getInstance());

    }

    public void clickFindOpponent(View v)
    {
        RelativeLayout l = new RelativeLayout(this);
        // TODO: create local game here
        OnlineGamemaster.getInstance().createOnlineGame(this);
        AnimatedBoard board = (AnimatedBoard) OnlineGamemaster.getInstance().getBoard();
        board.setGravity(Gravity.Down);
        RelativeLayout b = board.getGameLayout();
        RelativeLayout.LayoutParams pB = (RelativeLayout.LayoutParams) b.getLayoutParams();
        pB.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        b.setId(R.id.board);
        l.addView(b, pB);

        RelativeLayout controls = OnlineGamemaster.getInstance().getControls();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.board);
        params.topMargin = 50;
        l.addView(controls, params);
        setContentView(l);

    }

}
