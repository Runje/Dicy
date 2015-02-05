package games.runje.dicy.controller;

import android.app.Activity;

import games.runje.dicy.game.Game;
import games.runje.dicy.game.LocalGame;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 05.02.2015.
 */
public class AIController
{
    private final Player player;
    private final Activity activity;
    private String LogKey = "AIController";

    public AIController(Player p, Activity a)
    {
        player = p;
        activity = a;
        start();
    }

    private void start()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        // TODO
                        e.printStackTrace();
                        break;
                    }

                    Game game = Gamemaster.getInstance().getGame();
                    if (game.hasTurn(player) && !Gamemaster.getInstance().isAnimationIsRunning())
                    {
                        LocalGame l = (LocalGame) game;

                        if (l.getMovePoints() == 0)
                        {
                            activity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    makeMove();
                                }
                            });

                        }
                        else
                        {
                            Logger.logInfo(LogKey, "Next");
                            activity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Gamemaster.getInstance().next();
                                }
                            });

                        }
                    }
                }
            }
        }).start();
    }

    private void makeMove()
    {
        Logger.logInfo(LogKey, "Making a move");
        Move m = player.getStrategy().getNextMove(Gamemaster.getInstance().getRules(), Gamemaster.getInstance().getBoard());
        Action a = new SwitchAction(m.getFirst(), m.getSecond(), true);
        Gamemaster.getInstance().performAction(a);
    }


}
