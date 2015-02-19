package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


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
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void clickOnlineGame(View v)
    {
        Intent intent = new Intent(this, OnlineGameActivity.class);
        startActivity(intent);
    }

    public void clickHighscore(View v)
    {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }
}
