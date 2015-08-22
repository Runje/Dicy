package games.runje.dicy.layouts.statisticsView;

import java.util.List;

import games.runje.dicy.statistics.PointStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;

/**
 * Created by Thomas on 22.08.2015.
 */
public class MovePointStatisticFragment extends PointStatisticFragment
{
    public List<PointStatistic> getPoints()
    {
        StatisticManager manager = new SQLiteHandler(getActivity());
        return manager.getAllMovePoints();
    }
}
