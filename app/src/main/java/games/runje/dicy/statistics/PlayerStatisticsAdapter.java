package games.runje.dicy.statistics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 18.08.2015.
 */
public class PlayerStatisticsAdapter extends BaseAdapter
{

    private final Context context;
    List<PlayerStatistic> players;
    RuleVariant ruleVariant;
    private boolean winsDescending = true;
    private boolean nameDescending;
    private boolean loosesDescending;
    private boolean gamesDescending;
    private boolean percentDescending;

    public PlayerStatisticsAdapter(Context context, List<PlayerStatistic> players)
    {
        this.context = context;
        this.players = players;
        ruleVariant = null;
        sortAfterWins(winsDescending);
    }

    private void sortAfterWins(boolean descending)
    {
        if (ruleVariant == null)
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Long) p1.getWins()).compareTo(p2.getWins());
                }
            });
        } else
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Long) p1.getWins(ruleVariant)).compareTo(p2.getWins(ruleVariant));
                }
            });
        }

        if (descending)
        {
            Collections.reverse(players);
        }
    }

    private void sortAfterPercentageWins(boolean descending)
    {
        if (ruleVariant == null)
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Double) p1.getPercentageWin()).compareTo(p2.getPercentageWin());
                }
            });
        } else
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Double) p1.getPercentageWin(ruleVariant)).compareTo(p2.getPercentageWin(ruleVariant));
                }
            });
        }

        if (descending)
        {
            Collections.reverse(players);
        }
    }

    private void sortAfterGames(boolean descending)
    {
        if (ruleVariant == null)
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Long) p1.getGames()).compareTo(p2.getGames());
                }
            });
        } else
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Long) p1.getGames(ruleVariant)).compareTo(p2.getGames(ruleVariant));
                }
            });
        }

        if (descending)
        {
            Collections.reverse(players);
        }
    }

    private void sortAfterLooses(boolean descending)
    {
        if (ruleVariant == null)
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return ((Long) (p1.getLooses())).compareTo(p2.getLooses());
                }
            });
        } else
        {
            Collections.sort(players, new Comparator<PlayerStatistic>()
            {
                @Override
                public int compare(PlayerStatistic p1, PlayerStatistic p2)
                {
                    return (((Long) p1.getLooses(ruleVariant)).compareTo(p2.getLooses(ruleVariant)));
                }
            });
        }

        if (descending)
        {
            Collections.reverse(players);
        }
    }

    private void sortAfterName(boolean descending)
    {
        Collections.sort(players, new Comparator<PlayerStatistic>()
        {
            @Override
            public int compare(PlayerStatistic p1, PlayerStatistic p2)
            {
                return p1.getName().compareTo(p2.getName());
            }
        });

        if (descending)
        {
            Collections.reverse(players);
        }
    }


    @Override
    public int getCount()
    {
        // +1 for header
        return players.size();
    }

    @Override
    public Object getItem(int i)
    {
        return players.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return players.indexOf(getItem(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null)
        {
            view = inflater.inflate(R.layout.player_statistics_item, null);
        }

        TextView pos = (TextView) view.findViewById(R.id.stats_pos);
        TextView name = (TextView) view.findViewById(R.id.stats_name);
        TextView games = (TextView) view.findViewById(R.id.stats_games);
        TextView wins = (TextView) view.findViewById(R.id.stats_wins);
        TextView looses = (TextView) view.findViewById(R.id.stats_looses);
        TextView percent = (TextView) view.findViewById(R.id.stats_percent);

        PlayerStatistic player = (PlayerStatistic) getItem(i);
        pos.setText(Integer.toString(i + 1));
        name.setText(player.getName());

        if (ruleVariant == null)
        {
            games.setText(Long.toString(player.getGames()));
            wins.setText(Long.toString(player.getWins()));
            looses.setText(Long.toString(player.getLooses()));
            percent.setText(Utilities.doubleToString(player.getPercentageWin(), 1));
        } else
        {
            games.setText(Long.toString(player.getGames(ruleVariant)));
            wins.setText(Long.toString(player.getWins(ruleVariant)));
            looses.setText(Long.toString(player.getLooses(ruleVariant)));
            percent.setText(Utilities.doubleToString(player.getPercentageWin(ruleVariant), 1));
        }


        if (i % 2 == 0)
        {
            view.findViewById(R.id.stats_layout).setBackgroundColor(Color.DKGRAY);
        } else
        {
            view.findViewById(R.id.stats_layout).setBackgroundColor(context.getResources().getColor(R.color.dicy_green));
        }

        return view;
    }

    public void clickWins()
    {
        winsDescending = !winsDescending;
        sortAfterWins(winsDescending);
        notifyDataSetChanged();
    }

    public void clickPercentageWins()
    {
        percentDescending = !percentDescending;
        sortAfterPercentageWins(percentDescending);
        notifyDataSetChanged();
    }

    public void clickName()
    {
        nameDescending = !nameDescending;
        sortAfterName(nameDescending);
        notifyDataSetChanged();
    }

    public void clickLooses()
    {
        loosesDescending = !loosesDescending;
        sortAfterLooses(loosesDescending);
        notifyDataSetChanged();
    }

    public void clickGames()
    {
        gamesDescending = !gamesDescending;
        sortAfterGames(gamesDescending);
        notifyDataSetChanged();
    }

    public void setRuleVariant(RuleVariant ruleVariant)
    {
        this.ruleVariant = ruleVariant;
        sortAfterWins(true);
        notifyDataSetChanged();
    }
}
