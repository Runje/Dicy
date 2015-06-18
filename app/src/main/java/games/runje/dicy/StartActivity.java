package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import games.runje.dicy.statistics.GameStatistic;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.ai.Strategy;

public class StartActivity extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);



    }

    public void clickTestarena(View v)
    {
        Intent intent = new Intent(this, TestActivity.class);
        //startActivity(intent);

        Log.d("Start", "Adding Player Thomas");
        StatisticManager manager = new SQLiteHandler(this);
        //manager.recreate();
        manager.createPlayer("Thomas", Strategy.Human);
        Log.d("Start", "Adding Player Thomas");
        manager.createPlayer("Milena", Strategy.Human);
        manager.createPlayer("Max", Strategy.Simple);

        PlayerStatistic Thomas = manager.getPlayer("Thomas");
        PlayerStatistic Milena = manager.getPlayer("Milena");

        //manager.update(new GameStatistic(Thomas, Milena, 1));

        List<PlayerStatistic> players = manager.getAllPlayers();

        for(PlayerStatistic p : players)
        {
            Log.d("Start", p.toString());
        }
    }



    public void clickOnlineGame(View v)
    {
        Intent intent = new Intent(this, OnlineGameActivity.class);
        //startActivity(intent);
        Toast.makeText(this, "Playing online is not implemented yet", Toast.LENGTH_LONG).show();
    }

    public void clickPlay(View v)
    {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

}
