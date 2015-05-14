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
    private AIControllerHandler handler;
    private String LogKey = "AIController";

    public AIController(Player p, Activity a, AIControllerHandler handler)
    {
        this.handler = handler;
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

                    Game game = handler.getGame();
                    if (game.isFinishedOrCancelled())
                    {
                        break;
                    }

                    //Logger.logDebug(LogKey, "Checking, turn: " + game.hasTurn(player) + ", controls enabled: " + handler.areControlsEnabled());
                    if (game.hasTurn(player) && handler.areControlsEnabled())
                    {
                        LocalGame l = (LocalGame) game;

                        boolean someOneIsWinning = false;
                        int i = l.getLastLeadingPlayer();
                        if (i != -1)
                        {
                            Player winner = l.getPlayers().get(i);


                            if (winner.getId() != player.getId())
                            {
                                if (winner.getPoints() >= l.getGameEndPoints() && (l.getMovePoints() + player.getPoints() <= winner.getPoints()))
                                {
                                    someOneIsWinning = true;
                                }
                            }
                        }

                        boolean nextMoveSurePoints = getPointsOfNextMove() >= l.getPointsLimit();
                        if (l.getMovePoints() == 0 || someOneIsWinning || nextMoveSurePoints)
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
                                    handler.next();
                                }
                            });

                        }
                    }
                }
            }
        }).start();
    }

    private int getPointsOfNextMove()
    {
        return player.getStrategy().getNextMove(handler.getRules(), handler.getBoard()).getPoints();
    }
    private void makeMove()
    {
        AnimatedLogger.logInfo(LogKey, "Making a move");
        Move m = player.getStrategy().getNextMove(handler.getRules(), handler.getBoard());
        handler.switchElements(m.getFirst(), m.getSecond());
    }


}
