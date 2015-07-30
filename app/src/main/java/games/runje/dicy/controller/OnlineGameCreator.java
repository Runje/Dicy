package games.runje.dicy.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import games.runje.dicy.R;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.OnlineStrategy;
import games.runje.dicymodel.communication.MessageHandler;
import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.communication.messages.StartGameMessage;
import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 30.04.2015.
 */
public class OnlineGameCreator implements MessageHandler
{
    public static String LogKey = "OnlineGameCreator";
    AnimatedClientGamemaster gamemaster;
    Activity activity;

    public OnlineGameCreator(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg)
    {
        if (msg.getName().equals(StartGameMessage.Name))
        {
            createGame((StartGameMessage) msg);
        }
        else
        {
            gamemaster.receiveMessage(msg);
        }
    }

    private void createGame(final StartGameMessage msg)
    {

        for (Player p : msg.getGame().getPlayers())
        {
            if (p.getId() != msg.getToId())
            {
                p.setStrategy(new OnlineStrategy());
                Logger.logInfo(LogKey, p.getId() + " is online player");
            }
            else
            {
                p.setStrategy(null);
                Logger.logInfo(LogKey, p.getId() + " is human player");
            }
        }

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                activity.setContentView(R.layout.game);
                View mainView = activity.findViewById(R.id.board);
                mainView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        gamemaster = new AnimatedClientGamemaster(msg.getBoard(), msg.getRules(), activity, msg.getGame());
                        LinearLayout boardContainer = (LinearLayout) activity.findViewById(R.id.board);
                        boardContainer.addView(gamemaster.getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                });

            }
        });

    }
}
