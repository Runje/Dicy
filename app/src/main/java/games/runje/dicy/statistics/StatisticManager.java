package games.runje.dicy.statistics;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameLength;

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

    List<GameStatistic> getAllGames();

    List<GameStatistic> getGames(String player1, String player2);

    List<GameStatistic> getGames(GameLength length);

    boolean addMovePoints(int movePoints, Player playingPlayer);

    boolean addSwitchPoints(int switchPoints, Player playingPlayer);

    List<PointStatistic> getAllMovePoints();

    List<PointStatistic> getAllSwitchPoints();

    void deleteMovePoint(PointStatistic point);

    void deleteSwitchPoint(PointStatistic point);
}
