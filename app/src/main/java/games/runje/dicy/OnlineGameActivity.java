package games.runje.dicy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import games.runje.dicy.controller.AnimatedClientGamemaster;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.util.SystemUiHider;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.communication.ConnectionToServer;
import games.runje.dicymodel.communication.messages.FindOpponentMessage;
import games.runje.dicymodel.communication.messages.IdentifyMessage;
import games.runje.dicymodel.data.Board;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class OnlineGameActivity extends Activity
{
    private ConnectionToServer client;
    private String LogKey = "OnlineGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_online_game);

        EditText editId = (EditText) findViewById(R.id.idEditText);
        editId.setText(Integer.toString(new Random().nextInt(10000)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        TextView status = (TextView) findViewById(R.id.StatusClient);
        AnimatedLogger.logInfo(LogKey, "Connecting...");
        EditText editId = (EditText) findViewById(R.id.idEditText);
        int id = Integer.parseInt(editId.getText().toString());
        AnimatedClientGamemaster gm = new AnimatedClientGamemaster(new Board(5), new Rules(), this, null, null);
        ConnectionToServer.connect(gm, id);

    }

    public void clickFindOpponent(View v)
    {
        ConnectionToServer.sendMessage(new IdentifyMessage());
        ConnectionToServer.sendMessage(new FindOpponentMessage());

    }

}