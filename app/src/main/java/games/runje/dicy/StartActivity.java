package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import games.runje.dicy.statistics.GameStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;

public class StartActivity extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.game_file_key), MODE_PRIVATE);
        boolean resumeGame = sharedPreferences.getBoolean(LocalGameActivity.KEY_RESUME_GAME, false);

        View resumeButton = findViewById(R.id.button_resume);
        resumeButton.setEnabled(resumeGame);

    }

    public void clickTestarena(View v)
    {
        Intent intent = new Intent(this, TestActivity.class);
        //startActivity(intent);

    }



    public void clickOnlineGame(View v)
    {
        Intent intent = new Intent(this, OnlineGameActivity.class);
        startActivity(intent);
        //Toast.makeText(this, "Playing online is not implemented yet", Toast.LENGTH_LONG).show();
    }

    public void clickPlay(View v)
    {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    public void clickResume(View v)
    {
        Intent intent = new Intent(this, LocalGameActivity.class);
        startActivity(intent);
    }
}
