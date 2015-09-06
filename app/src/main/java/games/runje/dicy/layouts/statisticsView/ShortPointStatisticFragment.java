package games.runje.dicy.layouts.statisticsView;

import java.util.List;

import games.runje.dicy.statistics.GameStatistic;
import games.runje.dicy.statistics.PointStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 22.08.2015.
 */
public class ShortPointStatisticFragment extends PointStatisticFragment
{
    public List<PointStatistic> getPoints(RuleVariant ruleVariant)
    {
        StatisticManager manager = new SQLiteHandler(getActivity());
        if (ruleVariant == null)
        {
            List<GameStatistic> games = manager.getGames(GameLength.Short);
            for (GameStatistic game : games)
            {
                Logger.logInfo("Short", game.toString());
            }

            return getPointList(games);
        } else
        {
            List<GameStatistic> games = manager.getGames(GameLength.Short, ruleVariant);

            return getPointList(games);
        }
    }
}
