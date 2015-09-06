package games.runje.dicy.statistics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.runje.dicy.R;

/**
 * Created by Thomas on 18.08.2015.
 */
public class PointStatisticsAdapter extends BaseAdapter
{

    private final Context context;
    List<PointStatistic> points;

    public PointStatisticsAdapter(Context context, List<PointStatistic> points)
    {
        this.context = context;
        this.points = points;
        sortAfterPoints();
    }

    private void sortAfterPoints()
    {
        Collections.sort(points, new Comparator<PointStatistic>()
        {
            @Override
            public int compare(PointStatistic p1, PointStatistic p2)
            {
                return ((Integer) p1.getPoints()).compareTo(p2.getPoints());
            }
        });

        Collections.reverse(points);
    }

    @Override
    public int getCount()
    {
        return points.size();
    }

    @Override
    public Object getItem(int i)
    {
        return points.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return points.indexOf(getItem(i));
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


        PointStatistic point = (PointStatistic) getItem(i);

        pos.setText(Integer.toString(i + 1));
        name.setText(point.getName());
        games.setText(Integer.toString(point.getPoints()));
        wins.setVisibility(View.GONE);
        looses.setVisibility(View.GONE);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        percent.setText(format.format(point.getDate()));


        if (i % 2 == 0)
        {
            view.findViewById(R.id.stats_layout).setBackgroundColor(Color.DKGRAY);
        } else
        {
            view.findViewById(R.id.stats_layout).setBackgroundColor(context.getResources().getColor(R.color.dicy_green));
        }

        return view;
    }

    public void setPoints(List<PointStatistic> points)
    {
        this.points = points;
        sortAfterPoints();
        notifyDataSetChanged();
    }
}
