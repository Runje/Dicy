package games.runje.dicy.statistics;

import android.content.Context;
import android.widget.Toast;

import games.runje.dicy.R;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.StatisticHandler;

/**
 * Created by Thomas on 19.08.2015.
 */
public class LocalGameStatisticsHandler implements StatisticHandler
{
    private final Context context;
    private final StatisticManager manager;

    public LocalGameStatisticsHandler(Context context)
    {
        this.context = context;
        this.manager = new SQLiteHandler(context);
    }

    @Override
    public void movePoints(int movePoints, Player playingPlayer)
    {
        boolean top10 = manager.addMovePoints(movePoints, playingPlayer);

        if (top10)
        {
            Toast.makeText(context, R.string.top_ten_move, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void switchPoints(int switchPoints, Player playingPlayer)
    {
        boolean top10 = manager.addSwitchPoints(switchPoints, playingPlayer);

        if (top10)
        {
            Toast.makeText(context, R.string.top_ten_switch, Toast.LENGTH_SHORT).show();
        }
    }
}
