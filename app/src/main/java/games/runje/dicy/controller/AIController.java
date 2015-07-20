package games.runje.dicy.controller;

import android.app.Activity;

import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 05.02.2015.
 */
public class AIController
{
    public static String LogKey = "AIController";
    private final Player player;
    private final Activity activity;
    private AIControllerHandler handler;

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

                        boolean skillExecuted = false;
                        if (!nextMoveSurePoints)
                        {
                            for (final Skill s : player.getSkills())
                            {
                                if (s.getName().equals(Skill.Shuffle) && s.isExecutable())
                                {
                                    activity.runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            handler.executeSkill(s);
                                        }
                                    });

                                    skillExecuted = true;
                                    break;
                                }
                            }
                        }

                        if (skillExecuted)
                        {
                            continue;
                        }

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
                            AnimatedLogger.logDebug(LogKey, "Next");
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
        AnimatedLogger.logDebug(LogKey, "Making a move");
        Move m = player.getStrategy().getNextMove(handler.getRules(), handler.getBoard());
        handler.switchElements(m.getFirst(), m.getSecond());
    }


}
