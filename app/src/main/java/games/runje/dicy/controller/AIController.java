package games.runje.dicy.controller;

import android.app.Activity;

import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 05.02.2015.
 */
public class AIController
{
    private final Player player;
    private final Activity activity;
    private GamemasterAnimated gmAnimated;
    private String LogKey = "AIController";

    public AIController(Player p, Activity a, GamemasterAnimated gmAnimated)
    {
        this.gmAnimated = gmAnimated;
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
                AnimatedLogger.logInfo(LogKey, "Starting AIController for " + player);
                while (true)
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO
                        e.printStackTrace();
                        break;
                    }

                    Game game = gmAnimated.getGame();
                    if (game.isFinishedOrCancelled())
                    {
                        break;
                    }

                    //Logger.logDebug(LogKey, "Checking, turn: " + game.hasTurn(player) + ", controls enabled: " + gmAnimated.areControlsEnabled());
                    if (game.hasTurn(player) && gmAnimated.areControlsEnabled())
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
                            AnimatedLogger.logInfo(LogKey, "Next");
                            activity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    gmAnimated.next();
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
        AnimatedLogger.logInfo(LogKey, "Making a move");
        Move m = player.getStrategy().getNextMove(gmAnimated.getRules(), gmAnimated.getAnimatedBoard());
        gmAnimated.switchElements(m.getFirst(), m.getSecond());
    }


}
