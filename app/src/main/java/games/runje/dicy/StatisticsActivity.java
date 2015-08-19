package games.runje.dicy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.PlayerStatisticsAdapter;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;

/**
 * Created by Thomas on 18.08.2015.
 */
public class StatisticsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.statistics_layout);
        ListView list = (ListView) findViewById(R.id.list_statistics);

        StatisticManager manager = new SQLiteHandler(this);
        List<PlayerStatistic> players = manager.getAllPlayers();


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.statistics_item, null);


        TextView name = (TextView) view.findViewById(R.id.stats_name);
        TextView games = (TextView) view.findViewById(R.id.stats_games);
        TextView wins = (TextView) view.findViewById(R.id.stats_wins);
        TextView looses = (TextView) view.findViewById(R.id.stats_looses);
        TextView percent = (TextView) view.findViewById(R.id.stats_percent);

        name.setText("Name");
        games.setText("Games");
        wins.setText("Wins");
        looses.setText("Looses");
        percent.setText("Percent");
        view.findViewById(R.id.stats_layout).setBackgroundColor(Color.BLUE);
        list.addHeaderView(view);
        final PlayerStatisticsAdapter adapter = new PlayerStatisticsAdapter(this, players);
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
    }
}
