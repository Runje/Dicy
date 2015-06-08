package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        startActivity(intent);
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
