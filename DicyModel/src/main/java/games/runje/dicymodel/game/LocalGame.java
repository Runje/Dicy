package games.runje.dicymodel.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import games.runje.dicymodel.Logger;
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
    int gameEndPoints;
    private int lastLeadingPlayer;
    private List<Player> players;
    /**
     * Which players turn it is.
     */
    private int turn;
    private int movePoints;
    private int switchPoints;
    private int pointsLimit;
    private String winner;
    private boolean cancelled = false;
    private int length;
    private int winIndex = -1;
    private Date playerIsPlayingSince;

    public LocalGame(int pointLimit, int length, List<Player> playerList, int startingPlayer)
    {
        pointsLimit = pointLimit;
        this.length = length;
        gameEndPoints = pointLimit * length;

        this.players = playerList;

        this.lastLeadingPlayer = startingPlayer;
        turn = startingPlayer;
        resetTimer();
    }

    public Date getPlayerIsPlayingSince()
    {
        return playerIsPlayingSince;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public int getGameEndPoints()
    {
        return gameEndPoints;
    }

    public void setGameEndPoints(int gameEndPoints)
    {
        this.gameEndPoints = gameEndPoints;
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
                "gameEndPoints=" + gameEndPoints +
                ", lastLeadingPlayer=" + lastLeadingPlayer +
                ", players=" + Arrays.toString(players.toArray()) +
                ", turn=" + turn +
                ", movePoints=" + movePoints +
                ", switchPoints=" + switchPoints +
                ", pointsLimit=" + pointsLimit +
                ", winner='" + winner + '\'' +
                ", cancelled=" + cancelled +
                ", length=" + length +
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
    public void endSwitch()
    {
        //Logger.logDebug(LogKey, "End switch");
        if (switchPoints >= pointsLimit || !isStrikePossible())
        {
            movePoints += switchPoints;
            resetTimer();
        }
        else
        {

            movePoints = 0;
            moveEnds();
        }

        switchPoints = 0;
    }

    private void resetTimer()
    {
        playerIsPlayingSince = new Date();
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
        playerIsPlayingSince = new Date();
        return (players.get(turn).isAi());
    }

    @Override
    public boolean moveEnds()
    {
        players.get(turn).addPoints(movePoints);
        if (movePoints > 0)
        {
            players.get(turn).setStrikes(0);
            getPlayingPlayer().setLastMoveWasStrike(false);
        } else
        {
            players.get(turn).addStrike();
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

        if (maxPoints >= gameEndPoints && index == turn)
        {
            lastLeadingPlayer = index;
            Logger.logInfo(LogKey, "New Suddendeath Leader: " + getPlayingPlayer().getName());
        }
        else if (maxPoints < gameEndPoints)
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


        if (getPlayingPlayer().getPoints() >= gameEndPoints)
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


        if (getNotPlayingPlayer().getPoints() >= gameEndPoints && (getPlayingPlayer().getPoints() + getMovePoints() < getNotPlayingPlayer().getPoints()))
        {
            enoughPoints = true;
        }

        //Logger.logDebug(LogKey, "Last Player: " + lastPlayerTurn + ", MaxPoints; " + maxPoints);
        return lastLeadingPlayerTurn && enoughPoints;
    }

    public int getPointsLimit()
    {
        return pointsLimit;
    }

    public void setPointsLimit(int pointsLimit)
    {
        this.pointsLimit = pointsLimit;
        this.gameEndPoints = length * pointsLimit;
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
}
