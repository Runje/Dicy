package games.runje.dicy.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 17.01.2015.
 */
public class LocalGame extends Game
{
    private final int startingPlayer;
    int gameEndPoints;
    private List<Strategy> strategies;
    private List<Player> players;

    /**
     * Which players turn it is.
     */
    private int turn;
    private int movePoints;
    private int switchPoints;
    private String LogKey = "LocalGame";
    private int pointsLimit;
    private String winner;
    private boolean cancelled = false;

    public LocalGame(int p, int pLimit, int gameLimit)
    {
        pointsLimit = pLimit;
        gameEndPoints = gameLimit;
        players = new ArrayList<>();
        for (int i = 0; i < p; i++)
        {
            players.add(new Player("Player " + (i + 1), strategies.get(i)));
        }

        // random starting player
        startingPlayer = new Random().nextInt(p);
        turn = startingPlayer;
    }

    public LocalGame(int pointLimit, int gameLimit, List<String> playerNames, List<Strategy> s)
    {
        pointsLimit = pointLimit;
        gameEndPoints = gameLimit;

        strategies = s;

        players = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++)
        {
            Player p = new Player(playerNames.get(i), strategies.get(i));
            p.addSkill(new Skill(1, 6, Skill.Help));
            players.add(p);
            Logger.logInfo(LogKey, playerNames.get(i) + " is AI: " + p.isAi() + ", StrategyIsNull: " + (strategies.get(i) == null));
        }

        // random starting player
        startingPlayer = new Random().nextInt(playerNames.size());
        turn = startingPlayer;
    }

    public int getGameEndPoints()
    {
        return gameEndPoints;
    }

    @Override
    public void addPointElements(ArrayList<PointElement> elements)
    {
        int points = Utilities.getPointsFrom(elements);
        Logger.logInfo(LogKey, "Add switch points: " + points);
        switchPoints += points;

        loadSkills(elements);
    }

    private void loadSkills(ArrayList<PointElement> elements)
    {
        //TODO: Move to PointElement Class!?
        AnimatedBoard board = (AnimatedBoard) Gamemaster.getInstance().getBoard();
        //TODO: 7 should be a constant
        int[] count = new int[7];
        for(PointElement pointElement : elements)
        {
            Coords[] coords = pointElement.getCoords();
            for(Coords c : coords)
            {
                count[board.getElement(c).getValue()]++;
            }
        }

        getPlayingPlayer().loadSkills(count);
    }

    @Override
    public void endSwitch()
    {
        Logger.logInfo(LogKey, "End switch");
        if (switchPoints > pointsLimit)
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
        // TODO: if same name?
        return players.get(turn).getName().equals(player.getName());
    }

    @Override
    public boolean hasAIPlayerTurn()
    {
        return players.get(turn).isAi();
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
        nextPlayer();

        return isGameOver();
    }

    public boolean isGameOver()
    {
        boolean lastPlayerTurn = (turn == startingPlayer);
        boolean enoughPoints = false;
        int maxPoints = 0;
        String w = "Unknown";
        // TODO: What if two player with same points?
        for (Player p : players)
        {
            if (p.getPoints() >= maxPoints)
            {
                maxPoints = p.getPoints();
                w = p.getName();
            }
        }

        if (maxPoints >= gameEndPoints)
        {
            enoughPoints = true;
            winner = w;
        }

        Logger.logInfo(LogKey, "Last Player: " + lastPlayerTurn + ", MaxPoints; " + maxPoints);
        return lastPlayerTurn && enoughPoints;
    }

    public int getPointsLimit()
    {
        return pointsLimit;
    }

    public String getWinner()
    {
        return winner;
    }
}
