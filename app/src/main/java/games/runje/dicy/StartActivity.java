package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import games.runje.dicy.statistics.NamesDB;
import games.runje.dicy.util.ActivityUtilities;
import games.runje.dicymodel.Utilities;

public class StartActivity extends Activity
{
    private String KEY_FIRST_TIME = "first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.game_file_key), MODE_PRIVATE);
        boolean resumeGame = ActivityUtilities.getGameResumeable(this);
        boolean firstTime = sharedPreferences.getBoolean(KEY_FIRST_TIME, true);

        if (firstTime)
        {
            sharedPreferences.edit().putBoolean(KEY_FIRST_TIME, false).commit();
            clickTutorial(null);
        }

        View resumeButton = findViewById(R.id.button_resume);
        resumeButton.setEnabled(resumeGame);
        try
        {
            NamesDB.createDatabaseIfNotExists(this);
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed()
    {
        // do nothing
    }

    public void clickTutorial(View v)
    {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);

    }

    public void clickStatistics(View v)
    {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }


    public void clickOnlineGame(View v)
    {
        Intent intent = new Intent(this,TourneyActivity.class);
        startActivity(intent);

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
