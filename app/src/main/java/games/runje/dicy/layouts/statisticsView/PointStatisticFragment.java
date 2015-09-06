package games.runje.dicy.layouts.statisticsView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.statistics.GameStatistic;
import games.runje.dicy.statistics.PointStatistic;
import games.runje.dicy.statistics.PointStatisticsAdapter;
import games.runje.dicymodel.game.RuleVariant;

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
        final PointStatisticsAdapter adapter = new PointStatisticsAdapter(getActivity(), getPoints(null));
        list.setAdapter(adapter);

        ToggleButton baden = (ToggleButton) view.findViewById(R.id.toggle_Baden);
        ToggleButton basel = (ToggleButton) view.findViewById(R.id.toggle_Basel);
        ToggleButton atlantic = (ToggleButton) view.findViewById(R.id.toggle_Atlantic);
        ToggleButton vegas = (ToggleButton) view.findViewById(R.id.toggle_LasVegas);
        ToggleButton macao = (ToggleButton) view.findViewById(R.id.toggle_Macao);
        ToggleButton monte = (ToggleButton) view.findViewById(R.id.toggle_MonteCarlo);
        ToggleButton all = (ToggleButton) view.findViewById(R.id.toggle_All);
        final ToggleButton[] buttons = new ToggleButton[]{baden, basel, atlantic, vegas, macao, monte};
        baden.setOnCheckedChangeListener(new ToggleListener(RuleVariant.Baden_Baden, adapter, all, buttons));
        basel.setOnCheckedChangeListener(new ToggleListener(RuleVariant.Basel, adapter, all, buttons));
        atlantic.setOnCheckedChangeListener(new ToggleListener(RuleVariant.Atlantic_City, adapter, all, buttons));
        vegas.setOnCheckedChangeListener(new ToggleListener(RuleVariant.Las_Vegas, adapter, all, buttons));
        macao.setOnCheckedChangeListener(new ToggleListener(RuleVariant.Macao, adapter, all, buttons));
        monte.setOnCheckedChangeListener(new ToggleListener(RuleVariant.Monte_Carlo, adapter, all, buttons));

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {

                    for (ToggleButton button : buttons)
                    {
                        if (button.isChecked())
                        {
                            button.setChecked(false);
                        }
                    }

                    adapter.setPoints(getPoints(null));
                }
            }
        });
        return view;
    }

    public abstract List<PointStatistic> getPoints(RuleVariant ruleVariant);

    protected List<PointStatistic> getPointList(List<GameStatistic> games)
    {
        List<PointStatistic> points = new ArrayList<>();

        for (GameStatistic game : games)
        {
            if (game.hasP1Won())
            {
                points.add(new PointStatistic(game.getPlayer1().getName(), game.getDate(), game.getP1Points(), game.getRuleVariant()));
            } else
            {
                points.add(new PointStatistic(game.getPlayer2().getName(), game.getDate(), game.getP2Points(), game.getRuleVariant()));
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

    private class ToggleListener implements CompoundButton.OnCheckedChangeListener
    {
        private final ToggleButton all;
        private final ToggleButton[] others;
        private RuleVariant ruleVariant;
        private PointStatisticsAdapter adapter;

        public ToggleListener(RuleVariant ruleVariant, PointStatisticsAdapter adapter, ToggleButton all, ToggleButton[] others)
        {
            this.ruleVariant = ruleVariant;
            this.adapter = adapter;
            this.all = all;
            this.others = others;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            if (b)
            {

                all.setChecked(false);

                for (ToggleButton button : others)
                {
                    if (button != compoundButton)
                    {
                        if (button.isChecked())
                        {
                            button.setChecked(false);
                        }
                    }
                }

                adapter.setPoints(getPoints(ruleVariant));
            } else
            {
                all.setChecked(true);
            }
        }
    }
}
