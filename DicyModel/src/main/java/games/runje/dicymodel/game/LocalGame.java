package games.runje.dicymodel.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.skills.ChangeSkill;
import games.runje.dicymodel.skills.HelpSkill;

/**
 * Created by Thomas on 17.01.2015.
 */
public class LocalGame extends Game
{
    int gameEndPoints;
    private int lastLeadingPlayer;
    private List<Strategy> strategies;
    private List<Player> players;

    /**
     * Which players turn it is.
     */
    private int turn;
    private int movePoints;
    private int switchPoints;
    public static String LogKey = "LocalGame";
    private int pointsLimit;
    private String winner;
    private boolean cancelled = false;
    private int length;

    public LocalGame(int p, int pLimit, int length)
    {
        pointsLimit = pLimit;
        this.length = length;
        this.gameEndPoints = length * pointsLimit;
        players = new ArrayList<>();
        for (int i = 0; i < p; i++)
        {
            players.add(new Player("Player " + (i + 1), strategies.get(i), 0));
        }

        // random starting player
        lastLeadingPlayer = new Random().nextInt(p);
        turn = lastLeadingPlayer;
    }

    public LocalGame(int pointLimit, int length, List<Player> playerList, int startingPlayer)
    {
        pointsLimit = pointLimit;
        this.length = length;
        gameEndPoints = pointLimit * length;

        this.players = playerList;

        for (int i = 0; i < playerList.size(); i++)
        {
            Player p = playerList.get(i);
            // TODO: skills
            if (p.getSkills().size() < 2)
            {
                p.addSkill(new HelpSkill(1, 6));
                p.addSkill(new ChangeSkill(6, 6));
            }
            //Logger.logDebug(LogKey, playerNames.get(i) + " is AI: " + p.isAi() + ", StrategyIsNull: " + (strategies.get(i) == null));
        }

        // random starting player
        //lastLeadingPlayer = new Random().nextInt(playerNames.size());
        this.lastLeadingPlayer = startingPlayer;
        turn = startingPlayer;
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
        }
        else
        {
            players.get(turn).addStrike();
            getPlayingPlayer().setLastMoveWasStrike(true);
            movePoints = 0;
            moveEnds();
        }

        switchPoints = 0;
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
