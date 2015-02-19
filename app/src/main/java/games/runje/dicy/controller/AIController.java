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
    private String LogKey = "AIController";
    private AnimatedGamemaster gamemaster;

    public AIController(Player p, Activity a, AnimatedGamemaster gm)
    {
        gamemaster = gm;
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
                    }
                    catch (InterruptedException e)
                    {
                        // TODO
                        e.printStackTrace();
                        break;
                    }

                    Game game = AnimatedGamemaster.getInstance().getGame();
                    if (game.isFinishedOrCancelled())
                    {
                        break;
                    }
                    if (game.hasTurn(player) && !AnimatedGamemaster.getInstance().isAnimationIsRunning())
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
                                    AnimatedGamemaster.getInstance().next();
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
        Move m = player.getStrategy().getNextMove(AnimatedGamemaster.getInstance().getRules(), gamemaster.getBoard());
        Action a = new SwitchAction(m.getFirst(), m.getSecond(), true, gamemaster);
        AnimatedGamemaster.getInstance().performAction(a);
    }


}
