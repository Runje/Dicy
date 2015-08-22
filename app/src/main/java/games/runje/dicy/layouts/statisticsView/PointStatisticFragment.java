package games.runje.dicy.layouts.statisticsView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.statistics.GameStatistic;
import games.runje.dicy.statistics.PointStatistic;
import games.runje.dicy.statistics.PointStatisticsAdapter;

/**
 * Created by Thomas on 20.08.2015.
 */
public abstract class PointStatisticFragment extends Fragment
{
    private String LogKey = "PointStatisticFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_statistic, container, false);
        ListView list = (ListView) view.findViewById(R.id.listView);
        View header = view.findViewById(R.id.header);


        TextView name = (TextView) header.findViewById(R.id.stats_name);
        TextView games = (TextView) header.findViewById(R.id.stats_games);
        TextView wins = (TextView) header.findViewById(R.id.stats_wins);
        TextView looses = (TextView) header.findViewById(R.id.stats_looses);
        TextView percent = (TextView) header.findViewById(R.id.stats_percent);

        wins.setVisibility(View.GONE);


        looses.setVisibility(View.GONE);
        name.setText("Name");
        games.setText("Points");
        percent.setText("Date");
        header.setBackgroundColor(Color.BLUE);
        final PointStatisticsAdapter adapter = new PointStatisticsAdapter(getActivity(), getPoints());
        list.setAdapter(adapter);
        return view;
    }

    public abstract List<PointStatistic> getPoints();


    protected List<PointStatistic> getPointList(List<GameStatistic> games)
    {
        List<PointStatistic> points = new ArrayList<>();

        for (GameStatistic game : games)
        {
            if (game.hasP1Won())
            {
                points.add(new PointStatistic(game.getPlayer1().getName(), game.getDate(), game.getP1Points()));
            } else
            {
                points.add(new PointStatistic(game.getPlayer2().getName(), game.getDate(), game.getP2Points()));
            }
        }

        Collections.sort(points, new Comparator<PointStatistic>()
        {
            @Override
            public int compare(PointStatistic p1, PointStatistic p2)
            {
                return ((Integer) p1.getPoints()).compareTo(p2.getPoints());
            }
        });

        Collections.reverse(points);

        return points;
    }
}
