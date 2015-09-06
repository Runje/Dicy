package games.runje.dicy.controller;

import android.app.Activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.HelpSkill;
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
    private Random random = new Random();
    private Skill activeSkill;

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

                    if (game.hasTurn(player) && handler.areControlsEnabled())
                    {
                        if (activeSkill != null && !activeSkill.getName().equals(Skill.Shuffle))
                        {
                            Logger.logDebug(LogKey, "Handling Skill: " + activeSkill.getName());
                            handleSkill();
                        } else
                        {
                            makeStrategyMove();
                        }

                        //makeOldMove();
                    }
                }
            }
        }).start();
    }

    private void handleSkill()
    {
        if (activeSkill.getName().equals(Skill.Help))
        {
            HelpSkill helpSkill = (HelpSkill) activeSkill;
            Move m = helpSkill.getMove();
            makeMove(m);
            activeSkill = null;
        } else if (activeSkill.getName().equals(Skill.Change))
        {
            executeChangeSkill(activeSkill.getLoadValue());
            activeSkill = null;
        }

    }

    private void executeChangeSkill(int loadValue)
    {
        List<Move> moves = BoardChecker.getPossibleChangeSkillMoves(loadValue, handler.getBoard(), handler.getRules());
        Strategy strategy = player.getStrategy();
        if (moves.size() > 0)
        {
            if (getDecision(strategy.getSeeFalling()))
            {
                moves = BoardChecker.getPossibleChangeSkillMovesWithFalling(loadValue, handler.getBoard(), handler.getRules(), moves);
            }

            sortMovesAfterPoints(moves);

            Move move = getMove(moves, strategy.getSeePoints());
            executeChange(move);
        } else
        {
            // TODO:
            executeChange(new Move(new Coords(0, 0), null));
        }

    }

    private void executeChange(final Move move)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                handler.executeOnTouch(move.getFirst());
            }
        });
    }

    private void makeStrategyMove()
    {
        LocalGame game = (LocalGame) handler.getGame();
        Board board = handler.getBoard();
        Rules rules = handler.getRules();
        Player player = game.getPlayingPlayer();
        Strategy strategy = player.getStrategy();
        activeSkill = null;

        boolean canSeeFalling = getDecision(strategy.getSeeFalling());

        List<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

        // TODO: risc factor should depend on the move points
        if (canSeeFalling)
        {
            Logger.logDebug(LogKey, "Can see falling");
            moves = BoardChecker.getPossiblePointMovesGravity(board, rules, moves);
        }

        sortMovesAfterPoints(moves);

        Move move = getMove(moves, strategy.getSeePoints());
        Logger.logDebug(LogKey, "Move " + moves.indexOf(move) + "/" + moves.size() + ". " + move.toString());
        int errorPoints = calcError(strategy.getError());
        Logger.logDebug(LogKey, "ErrorPoints = " + errorPoints);

        if (move.getPoints() + errorPoints >= game.getPointsLimit())
        {
            makeMove(move);
        } else
        {
            boolean skillPossible = player.isSkillPossible();
            boolean willGameBeOverIfStrike = game.willGameBeOverIfStrike();
            boolean willGameBeOver = game.willGameBeOver();
            if (game.getMovePoints() == 0)
            {
                Logger.logDebug(LogKey, "MovePoints = 0");
                if (willGameBeOverIfStrike && skillPossible)
                {
                    Logger.logDebug(LogKey, "GameWillBeOver and Skill is possible");
                    double riskFactor = 0.25;
                    if (getDecision(strategy.getRisk() * riskFactor))
                    {
                        Logger.logDebug(LogKey, "Risc");
                        makeMove(move);
                    } else
                    {
                        executeSkill();
                    }
                } else if (skillPossible)
                {
                    Logger.logDebug(LogKey, "GameWillNotBeOver and Skill is possible");
                    double riskFactor = 0.5;
                    if (getDecision(strategy.getRisk() * riskFactor))
                    {
                        Logger.logDebug(LogKey, "Risc");
                        makeMove(move);
                    } else
                    {
                        executeSkill();
                    }
                } else
                {
                    Logger.logDebug(LogKey, "Skill not possible");
                    makeMove(move);
                }
            } else
            {
                Logger.logDebug(LogKey, "MovePoints > 0");
                if (willGameBeOverIfStrike && skillPossible)
                {
                    Logger.logDebug(LogKey, "GameWillBeOver and Skill is possible");
                    double riskFactor = 0.1;
                    if (getDecision(strategy.getRisk() * riskFactor))
                    {
                        Logger.logDebug(LogKey, "Risc");
                        makeMove(move);
                    } else if (getDecision(strategy.getSkills()) || willGameBeOver)
                    {
                        executeSkill();
                    } else
                    {
                        next();
                    }
                } else if (skillPossible)
                {
                    Logger.logDebug(LogKey, "GameWillNotBeOver and Skill is possible");
                    double riskFactor = 0.75;
                    if (getDecision(strategy.getRisk() * riskFactor))
                    {
                        Logger.logDebug(LogKey, "Risc");
                        makeMove(move);
                    } else if (getDecision(strategy.getSkills()) || willGameBeOver)
                    {
                        executeSkill();
                    } else
                    {
                        next();
                    }
                } else
                {
                    Logger.logDebug(LogKey, "Skill not possible");

                    if (willGameBeOverIfStrike)
                    {
                        double riscFactor = 0.1;
                        if (getDecision(riscFactor * strategy.getRisk()) || willGameBeOver)
                        {
                            Logger.logDebug(LogKey, "Risc");
                            makeMove(move);
                        } else
                        {
                            next();
                        }
                    } else
                    {
                        if (getDecision(strategy.getRisk()))
                        {
                            Logger.logDebug(LogKey, "Risc");
                            makeMove(move);
                        }
                        else
                        {
                            next();
                        }
                    }
                }
            }
        }



    }

    private void executeSkill()
    {
        for (final Skill skill : player.getSkills())
        {
            if (skill.isExecutable())
            {
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activeSkill = skill;
                        handler.executeSkill(skill);
                    }
                });
                break;
            }
        }

    }

    private void next()
    {
        Logger.logDebug(LogKey, "Next");
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                handler.next(false);
            }
        });
    }

    private int calcError(double error)
    {
        boolean positive = getDecision(0.5);

        int points = 0;
        while (true)
        {
            if (getDecision(error))
            {
                int errorPoints = 5;
                points += errorPoints;
                if (points >= 25)
                {
                    break;
                }
            } else
            {
                break;
            }
        }

        if (positive)
        {
            return points;
        } else
        {
            return -points;
        }
    }

    private Move getMove(List<Move> moves, double seePoints)
    {
        int size = moves.size();

        for (int i = 0; i < size; i++)
        {
            if (i + 1 == size)
            {
                return moves.get(i);
            } else
            {
                boolean seeMove = getDecision(seePoints);
                if (seeMove)
                {
                    return moves.get(i);
                }
            }
        }

        return null;
    }


    private void sortMovesAfterPoints(List<Move> moves)
    {
        Collections.sort(moves, new Comparator<Move>()
        {
            @Override
            public int compare(Move move, Move otherMover)
            {
                return ((Integer) move.getPoints()).compareTo(otherMover.getPoints());
            }
        });

        // reverse that the most points are on top
        Collections.reverse(moves);
    }

    public boolean getDecision(double probability)
    {
        if (random.nextDouble() < probability)
        {
            return true;
        }

        return false;
    }

    private void makeOldMove()
    {
        Game game = handler.getGame();
        //Logger.logDebug(LogKey, "Checking, turn: " + game.hasTurn(player) + ", controls enabled: " + handler.areControlsEnabled());

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

        if (!skillExecuted)
        {


            if (l.getMovePoints() == 0 || someOneIsWinning || nextMoveSurePoints)
            {
                makeMove();
            } else
            {
                AnimatedLogger.logDebug(LogKey, "Next");
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        handler.next(false);
                    }
                });
            }
        }
    }

    private int getPointsOfNextMove()
    {
        return player.getStrategy().getNextMove(handler.getRules(), handler.getBoard()).getPoints();
    }
    private void makeMove()
    {
        final Move m = player.getStrategy().getNextMove(handler.getRules(), handler.getBoard());
        AnimatedLogger.logDebug(LogKey, "Making a move: " + m);

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                handler.switchElements(m.getFirst(), m.getSecond());
            }
        });
    }

    private void makeMove(final Move m)
    {
        AnimatedLogger.logDebug(LogKey, "Making move: " + m);
        if (m.getGravity() != null)
        {
            changeGravity(m.getGravity());
        }


        AnimatedLogger.logDebug(LogKey, "Switching elements");

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                handler.switchElements(m.getFirst(), m.getSecond());
            }
        });

    }

    private void changeGravity(final Gravity gravity)
    {
        Logger.logDebug(LogKey, "Changing gravity to " + gravity.toString());
        if (handler.getBoard().getGravity() != gravity)
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    handler.changeGravity(gravity);
                }
            });
        }

        // wait until gravity is changed
        Logger.logInfo(LogKey, "Start waiting");

        do
        {
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        } while (!handler.areControlsEnabled());

        Logger.logInfo(LogKey, "End waiting");
    }


}
