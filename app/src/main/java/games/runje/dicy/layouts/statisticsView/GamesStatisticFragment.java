package games.runje.dicy.layouts.statisticsView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.PlayerStatisticsAdapter;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;

/**
 * Created by Thomas on 20.08.2015.
 */
public class GamesStatisticFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_statistic, container, false);
        ListView list = (ListView) view.findViewById(R.id.listView);
        View header = view.findViewById(R.id.header);

        StatisticManager manager = new SQLiteHandler(getActivity());
        List<PlayerStatistic> players = manager.getAllPlayers();


        TextView name = (TextView) header.findViewById(R.id.stats_name);
        TextView games = (TextView) header.findViewById(R.id.stats_games);
        TextView wins = (TextView) header.findViewById(R.id.stats_wins);
        TextView looses = (TextView) header.findViewById(R.id.stats_looses);
        TextView percent = (TextView) header.findViewById(R.id.stats_percent);

        header.setBackgroundColor(Color.BLUE);
        final PlayerStatisticsAdapter adapter = new PlayerStatisticsAdapter(getActivity(), players);
        list.setAdapter(adapter);

        wins.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adapter.clickWins();
            }
        });

        percent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adapter.clickPercentageWins();
            }
        });

        games.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adapter.clickGames();
            }
        });

        looses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adapter.clickLooses();
            }
        });

        name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                adapter.clickName();
            }
        });
        return view;
    }


}
