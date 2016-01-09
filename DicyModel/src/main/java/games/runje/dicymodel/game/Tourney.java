package games.runje.dicymodel.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.skills.ChangeSkill;
import games.runje.dicymodel.skills.HelpSkill;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 16.11.2015.
 */
public class Tourney
{
    private final int numberOfPlayers;
    private final List<Player> players;
    private List<List<TourneyGame>> plan;
    private int activeRound;

    public Tourney(List<Player> players)
    {
        this.players = players;
        this.numberOfPlayers = players.size();
        createTourneyPlan();
        activeRound = 0;
    }

    private void createTourneyPlan()
    {
        plan = new ArrayList<>();
        plan.add(new ArrayList<TourneyGame>());
        for (int j = 0; j < numberOfPlayers / 2; j++)
        {
            plan.get(0).add(new TourneyGame(players.get(j), players.get(numberOfPlayers - j - 1)));
        }
    }

    public List<List<TourneyGame>> getPlan()
    {
        return plan;
    }

    public Tourney(Player player)
    {
        this(createPlayers(8, player));
    }

    private static List<Player> createPlayers(int number, Player player)
    {
        List<Player> players = new ArrayList<>();
        Random r = new Random();
        List<Skill> skills1 = new ArrayList<Skill>();
        skills1.add(new ChangeSkill(1, 6));
        skills1.add(new HelpSkill(6, 6));
        skills1.add(new ShuffleSkill(5, 6));
        for (int i = 0; i < number - 1; i++)
        {
            Strategy strategy = new Strategy(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble());
            players.add(new Player("Player" + i, strategy, i, skills1));
        }

        players.add(player);
        return players;
    }

    public TourneyGame getNextHumanGame()
    {
        List<TourneyGame> games = plan.get(activeRound);
        for (int i = 0; i < numberOfPlayers / 2; i++)
        {
            TourneyGame game = games.get(i);
            if (game.getPlayer1().isHuman() || game.getPlayer2().isHuman())
            {
                return game;
            }
        }

        return null;
    }

    public List<TourneyGame> getRoundPlan()
    {
        return plan.get(activeRound);
    }
}
