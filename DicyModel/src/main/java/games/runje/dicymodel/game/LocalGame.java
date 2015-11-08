package games.runje.dicymodel.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 17.01.2015.
 */
public class LocalGame extends Game
{
    public static String LogKey = "LocalGame";
    private int lastLeadingPlayer;
    private List<Player> players;
    /**
     * Which players turn it is.
     */
    private int turn;
    private int movePoints;
    private int switchPoints;
    private String winner;
    private boolean cancelled = false;
    private int winIndex = -1;
    private int playerIsPlayingSince;
    private StatisticHandler statisticsHandler;
    private Rules rules;

    public LocalGame(List<Player> playerList, int startingPlayer, Rules rules)
    {
        this.rules = rules;

        this.players = playerList;

        this.lastLeadingPlayer = startingPlayer;
        turn = startingPlayer;
        resetTimer();
    }

    public int getPlayerIsPlayingSince()
    {
        return playerIsPlayingSince;
    }

    public void setPlayerIsPlayingSince(int playerIsPlayingSince)
    {
        this.playerIsPlayingSince = playerIsPlayingSince;
    }

    @Override
    public void addPointElements(ArrayList<PointElement> elements, Board board, boolean load)
    {
        int points = Utilities.getPointsFrom(elements);
        //Logger.logDebug(LogKey, "Add switch points: " + points);
        switchPoints += points;

        if (load)
        {
            loadSkills(elements, board);
        }
    }

    @Override
    public String toString()
    {
        return "LocalGame{" +
                ", lastLeadingPlayer=" + lastLeadingPlayer +
                ", players=" + Arrays.toString(players.toArray()) +
                ", turn=" + turn +
                ", movePoints=" + movePoints +
                ", switchPoints=" + switchPoints +
                ", winner='" + winner + '\'' +
                ", cancelled=" + cancelled +
                ", winIndex=" + winIndex +
                '}';
    }

    private void loadSkills(ArrayList<PointElement> elements, Board board)
    {
        //TODO: Move to PointElement Class!? In PointElement should be the value!
        //TODO: 7 should be a constant
        int[] count = new int[7];
        for (PointElement pointElement : elements)
        {
            Coords[] coords = pointElement.getCoords();
            for (Coords c : coords)
            {
                count[board.getElement(c).getValue()]++;
            }
        }

        getPlayingPlayer().loadSkills(count);
        for (int i = 0; i < count.length; i++)
        {
            //Logger.logDebug(LogKey, "i: " + i + ", count: " + count[i]);
        }

        //Logger.logDebug(LogKey, "Skillload: " + getPlayingPlayer().getSkill(Skill.Help).getCurrentLoad());
    }

    @Override
    public void endSwitch(int allowedStrikes)
    {
        //Logger.logDebug(LogKey, "End switch");
        if (switchPoints >= rules.getPointLimit() || !isStrikePossible())
        {
            movePoints += switchPoints;
            resetTimer();
            if (statisticsHandler != null)
            {
                statisticsHandler.switchPoints(switchPoints, getPlayingPlayer(), rules.getRuleVariant());
            }
        }
        else
        {
            movePoints = 0;
            moveEnds(allowedStrikes);
        }

        switchPoints = 0;
    }

    public void resetTimer()
    {
        playerIsPlayingSince = 0;
    }

    @Override
    public boolean hasTurn(Player player)
    {
        Logger.logDebug(LogKey, "Has Turn: " + getPlayingPlayer().getId() + ". Player: " + player.getId());
        return getPlayingPlayer().getId() == player.getId();
    }

    @Override
    public boolean hasAIPlayerTurn()
    {
        // TODO: differentiate between AI and Online player
        return !players.get(turn).isHuman();
    }

    @Override
    public Player getPlayingPlayer()
    {
        return players.get(turn);
    }

    public Player getNotPlayingPlayer()
    {
        return players.get((turn + 1) % 2);
    }

    @Override
    public boolean isFinishedOrCancelled()
    {
        return isGameOver() || isCancelled();
    }

    @Override
    public void cancel()
    {
        cancelled = true;
    }

    private boolean isCancelled()
    {
        return cancelled;
    }

    public int getMovePoints()
    {
        return movePoints;
    }

    public void setMovePoints(int movePoints)
    {
        this.movePoints = movePoints;
    }

    public int getSwitchPoints()
    {
        return switchPoints;
    }

    public void setSwitchPoints(int switchPoints)
    {
        this.switchPoints = switchPoints;
    }

    public int getTurn()
    {
        return turn;
    }

    public void setTurn(int turn)
    {
        this.turn = turn;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    /**
     * @return If next player is AI
     */
    private boolean nextPlayer()
    {
        turn = (turn + 1) % players.size();
        resetTimer();
        return (players.get(turn).isAi());
    }

    public void setStatisticsHandler(StatisticHandler statisticsHandler)
    {
        this.statisticsHandler = statisticsHandler;
    }

    @Override
    public boolean moveEnds(int allowedStrikes)
    {
        players.get(turn).addPoints(movePoints);
        if (movePoints > 0)
        {
            players.get(turn).setStrikes(0);
            getPlayingPlayer().setLastMoveWasStrike(false);
            if (statisticsHandler != null)
            {
                statisticsHandler.movePoints(movePoints, getPlayingPlayer(), rules.getRuleVariant());
            }
        } else
        {
            players.get(turn).addStrike(allowedStrikes);
            getPlayingPlayer().setLastMoveWasStrike(true);
        }

        movePoints = 0;
        updateLeader();
        nextPlayer();

        return isGameOver();
    }

    private void updateLeader()
    {
        int maxPoints = 0;
        int index = -1;
        for (int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);
            if (player.getPoints() >= maxPoints)
            {
                maxPoints = player.getPoints();
                index = i;
            }
        }

        if (maxPoints >= rules.getGameEndPoints() && index == turn)
        {
            lastLeadingPlayer = index;
            Logger.logInfo(LogKey, "New Suddendeath Leader: " + getPlayingPlayer().getName());
        } else if (maxPoints < rules.getGameEndPoints())
        {
            Logger.logDebug(LogKey, "Maxpoints: " + maxPoints + ", index " + index);
            lastLeadingPlayer = -1;
        }
    }

    public boolean isGameOver()
    {
        boolean lastLeadingPlayerTurn = (turn == lastLeadingPlayer);
        boolean enoughPoints = false;

        String w = "Unknown";
        // TODO: What if two player with same points?


        if (getPlayingPlayer().getPoints() >= rules.getGameEndPoints())
        {
            enoughPoints = true;
            winner = getPlayingPlayer().getName();
            winIndex = turn;
        }

        //Logger.logDebug(LogKey, "Last Player: " + lastPlayerTurn + ", MaxPoints; " + maxPoints);
        return lastLeadingPlayerTurn && enoughPoints;
    }

    public boolean willGameBeOver()
    {
        boolean lastLeadingPlayerTurn = (((turn + 1) % 2) == lastLeadingPlayer);
        boolean enoughPoints = false;

        // TODO: What if two player with same points?


        if (getNotPlayingPlayer().getPoints() >= rules.getGameEndPoints() && (getPlayingPlayer().getPoints() + getMovePoints() < getNotPlayingPlayer().getPoints()))
        {
            enoughPoints = true;
        }

        //Logger.logDebug(LogKey, "Last Player: " + lastPlayerTurn + ", MaxPoints; " + maxPoints);
        return lastLeadingPlayerTurn && enoughPoints;
    }

    public boolean willGameBeOverIfStrike()
    {
        boolean lastLeadingPlayerTurn = (((turn + 1) % 2) == lastLeadingPlayer);
        boolean enoughPoints = false;

        // TODO: What if two player with same points?


        if (getNotPlayingPlayer().getPoints() >= rules.getGameEndPoints())
        {
            enoughPoints = true;
        }

        //Logger.logDebug(LogKey, "Last Player: " + lastPlayerTurn + ", MaxPoints; " + maxPoints);
        return lastLeadingPlayerTurn && enoughPoints;
    }

    public String getWinner()
    {
        return winner;
    }

    public int getWinningIndex()
    {
        return winIndex;
    }

    public int getLastLeadingPlayer()
    {
        return lastLeadingPlayer;
    }

    public boolean areMostPoints(int points)
    {
        for (int i = 0; i < players.size(); i++)
        {
            Player player = players.get(i);
            if (player.getPoints() > points)
            {
                return false;
            }
        }

        return true;
    }

    public Player getOtherPlayer(Player player)
    {
        // assuming only 2 Players are playing
        if (players.get(0).getId() == player.getId())
        {
            return players.get(1);
        }
        else
        {
            return players.get(0);
        }
    }

    public void increasePlayingTime()
    {
        playerIsPlayingSince++;
    }

    public Rules getRules()
    {
        return rules;
    }
}
