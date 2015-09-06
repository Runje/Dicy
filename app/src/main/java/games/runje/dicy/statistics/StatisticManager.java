package games.runje.dicy.statistics;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 15.06.2015.
 */
public interface StatisticManager
{
    void update(GameStatistic game);

    ArrayList<PlayerStatistic> getAllPlayers();

    void recreate();


    PlayerStatistic getPlayer(String name);

    PlayerStatistic createPlayer(String name, Strategy strategy);

    List<GameStatistic> getAllGames();

    List<GameStatistic> getGames(String player1, String player2);

    List<GameStatistic> getGames(GameLength length);

    List<GameStatistic> getGames(GameLength length, RuleVariant ruleVariant);

    boolean addMovePoints(int movePoints, Player playingPlayer, RuleVariant ruleVariant);

    boolean addSwitchPoints(int switchPoints, Player playingPlayer, RuleVariant ruleVariant);

    List<PointStatistic> getAllMovePoints();

    List<PointStatistic> getAllSwitchPoints();

    void deleteMovePoint(PointStatistic point);

    void deleteSwitchPoint(PointStatistic point);

    List<PointStatistic> getMovePoints(RuleVariant ruleVariant);

    List<PointStatistic> getSwitchPoints(RuleVariant ruleVariant);
}
