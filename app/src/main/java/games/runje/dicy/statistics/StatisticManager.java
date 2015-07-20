package games.runje.dicy.statistics;

import java.util.ArrayList;

/**
 * Created by Thomas on 15.06.2015.
 */
public interface StatisticManager
{
    void update(GameStatistic game);

    ArrayList<PlayerStatistic> getAllPlayers();

    void recreate();


    PlayerStatistic getPlayer(String name);

    PlayerStatistic createPlayer(String name, String strategy);

    void getAllGames();
}
