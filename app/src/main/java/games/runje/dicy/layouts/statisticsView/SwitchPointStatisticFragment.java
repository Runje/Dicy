package games.runje.dicy.layouts.statisticsView;

import java.util.List;

import games.runje.dicy.statistics.PointStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 22.08.2015.
 */
public class SwitchPointStatisticFragment extends PointStatisticFragment
{
    public List<PointStatistic> getPoints(RuleVariant ruleVariant)
    {
        StatisticManager manager = new SQLiteHandler(getActivity());
        if (ruleVariant == null)
        {
            return manager.getAllSwitchPoints();
        } else
        {
            return manager.getSwitchPoints(ruleVariant);
        }
    }
}
