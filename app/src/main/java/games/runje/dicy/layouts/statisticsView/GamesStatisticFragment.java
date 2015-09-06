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

import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.PlayerStatisticsAdapter;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicymodel.game.RuleVariant;

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

                    adapter.setRuleVariant(null);

                }
            }
        });
        return view;
    }

    private class ToggleListener implements CompoundButton.OnCheckedChangeListener
    {
        private final ToggleButton all;
        private final ToggleButton[] others;
        private RuleVariant ruleVariant;
        private PlayerStatisticsAdapter adapter;

        public ToggleListener(RuleVariant ruleVariant, PlayerStatisticsAdapter adapter, ToggleButton all, ToggleButton[] others)
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
                    if (button != compoundButton && button.isChecked())
                    {
                        button.setChecked(false);
                    }
                }

                adapter.setRuleVariant(ruleVariant);
            } else
            {
                all.setChecked(true);
            }
        }
    }



}
